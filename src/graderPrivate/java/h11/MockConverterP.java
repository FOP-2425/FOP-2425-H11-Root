package h11;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Streams;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.opentest4j.AssertionFailedError;
import org.tudalgo.algoutils.student.CrashException;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers;
import org.tudalgo.algoutils.tutor.general.match.MatchingUtils;
import org.tudalgo.algoutils.tutor.general.reflections.PackageLink;
import org.tudalgo.algoutils.tutor.general.reflections.TypeLink;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static h11.ReflectionUtilsP.actsLikePrimitive;
import static h11.ReflectionUtilsP.formatStackTrace;
import static h11.ReflectionUtilsP.isObjectMethod;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.fail;

public class MockConverterP extends JsonConverterP {

    public static String SOLUTION_PACKAGE_INFIX = "solution";

    protected Map<Class<?>, Function<Object, Object>> solutionMapper = new HashMap<>() {{
        put(
            List.class,
            (list) -> ((List<?>) list).stream()
                .map(e -> getStudentObjectForSolution(e))
                .collect(Collectors.toCollection(ArrayList::new))
        );
        put(
            Map.class,
            (map) -> ((Map<?, ?>) map).entrySet()
                .stream()
                .map(e -> Map.entry(getStudentObjectForSolution(e.getKey()), getStudentObjectForSolution(e.getValue())))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue))
        );
        put(
            Map.Entry.class,
            (entry) -> Map.entry(
                getStudentObjectForSolution(((Entry<?, ?>) entry).getKey()),
                getStudentObjectForSolution(((Entry<?, ?>) entry).getValue())
            )
        );
    }};

    private static Boolean hasSolution;
    private BiMap<Integer, Object> objects = HashBiMap.create();
    private Map<Integer, ObjectNode> mapped = new HashMap<>();
    private BiMap<Object, Object> solutionMocks = HashBiMap.create();
    private boolean remap = false;

    public MockConverterP() {
    }

    public MockConverterP(boolean shouldApplyRemapping) {
        remap = shouldApplyRemapping;
    }

    @Override
    public ObjectNode toJsonNode(Object toMap) {

        int identityHashCode = System.identityHashCode(toMap);
        if (mapped.containsKey(identityHashCode)) {
            return mapped.get(identityHashCode);
        }
        if (objects.containsValue(toMap) && ReflectionUtilsP.actsLikePrimitive(toMap.getClass())) {
            return mapped.get(System.identityHashCode(objects.get(objects.inverse().get(toMap))));
        }

        ObjectNode rootNode = super.toJsonNode(toMap);

        if (mapped.containsKey(identityHashCode)) {
            return mapped.get(identityHashCode);
        }

        rootNode.put("id", objects.size());

        if (!objects.containsValue(toMap)) {
            objects.put(objects.size(), toMap);
        }
        mapped.put(identityHashCode, rootNode);

        return rootNode;
    }

    @Override
    public <T> T fromJsonNode(ObjectNode nodeToConvert, Answer<?> defaultAnswer) {
        if (objects.containsKey(nodeToConvert.get("id").asInt())) {
            return (T) objects.get(nodeToConvert.get("id").asInt());
        }

        T constructed = super.fromJsonNode(nodeToConvert, defaultAnswer);
        if (constructed == null) {
            return null;
        }

        if (!objects.containsValue(constructed)) {
            objects.put(nodeToConvert.get("id").asInt(), constructed);
        }

        return (T) constructed;
    }

    public void reset() {
        objects = HashBiMap.create();
        mapped = new HashMap<>();
        solutionMocks = HashBiMap.create();
    }

    public static ObjectNode mapCall(Object objectToCall, Method method, boolean includeObjectMethods, Object... arguments)
        throws InvocationTargetException, IllegalAccessException {
        MockConverterP converter = new MockConverterP();

        Map<Object, Map<Method, Set<Invocation>>> calls = new HashMap<>();

        AtomicBoolean stopRecordingCalls = new AtomicBoolean(false);

        Answer<?> answer = invocationOnMock -> {
            var returnValue = invocationOnMock.callRealMethod();
            if (Arrays.stream(invocationOnMock.getMethod().getAnnotations()).anyMatch(ann -> ann.annotationType() == DoNotTouch.class)) {
                return returnValue;
            }
            if (!stopRecordingCalls.get()) {
                calls
                    .computeIfAbsent(invocationOnMock.getMock(), (m) -> new HashMap<>())
                    .computeIfAbsent(invocationOnMock.getMethod(), (m) -> new HashSet<>())
                    .add(new Invocation(invocationOnMock.getArguments(), returnValue));
            }
            return returnValue;
        };

        Object converted = deepConvertToMocks(objectToCall, answer);
        for (int i = 0; i < arguments.length; i++) {
            Class argClass = arguments[i].getClass();
            if (ReflectionUtilsP.isLambda(argClass)) {
                Object original = arguments[i];
                arguments[i] = mock(
                    argClass.getInterfaces()[0], invocationOnMock -> {
                        var returnValue = invocationOnMock.getMethod().invoke(original, invocationOnMock.getArguments());
                        if (Arrays.stream(invocationOnMock.getMethod().getAnnotations()).anyMatch(ann -> ann.annotationType() == DoNotTouch.class)) {
                            return returnValue;
                        }
                        if (!stopRecordingCalls.get()) {
                            calls
                                .computeIfAbsent(invocationOnMock.getMock(), (m) -> new HashMap<>())
                                .computeIfAbsent(invocationOnMock.getMethod(), (m) -> new HashSet<>())
                                .add(new Invocation(invocationOnMock.getArguments(), returnValue));
                        }
                        return returnValue;
                    }
                );
            } else {
                Object existingMock = ReflectionUtilsP.findInFields(arguments[i], converted);
                if (existingMock != null) {
                    arguments[i] = existingMock;
                } else {
                    arguments[i] = deepConvertToMocks(arguments[i], answer);
                }

            }
        }

        converter.toJsonNode(converted);

        Object expected;
        try {
            if (!method.getReturnType().equals(void.class)) {
                expected = method.invoke(converted, arguments);
            } else {
                method.invoke(converted, arguments);
                expected = converted;
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Can not invoke %s on Class %s with parameters of type %s".formatted(
                    method.getName(),
                    objectToCall.getClass(),
                    Arrays.stream(arguments).map(Object::getClass).toList()
                ), e
            );
        }

        for (Object argument : arguments) {
            converter.toJsonNode(argument);
        }
        for (Object called : calls.keySet()) {
            converter.toJsonNode(called);
        }

        ObjectNode rootNode = MAPPER.createObjectNode();

        converter.setupEntryPoint(arguments, rootNode, converted, objectToCall.getClass(), method);

        converter.setupMethodCalls(method, includeObjectMethods, calls, rootNode);

        converter.setUpObjects(rootNode);

        rootNode.set("expected", new JsonConverterP().toJsonNode(expected));

        return rootNode;
    }

    private void setupEntryPoint(Object[] arguments, ObjectNode rootNode, Object converted, Class<?> invokedClass,
                                 Method invokedMethod) {
        rootNode.put("invoked", getID(converted));
        String className = invokedClass.getName();
        String methodName = invokedMethod.getName();
        String methodParameters = Arrays.stream(invokedMethod.getParameters())
            .map(Parameter::getType)
            .map(Class::getName)
            .collect(Collectors.joining(", "));
        rootNode.put("entryPoint", className + "#" + methodName + "(" + methodParameters + ")");
        ArrayNode argumentsJson = MAPPER.createArrayNode();
        for (Object argument : arguments) {
            argumentsJson.add(getID(argument));
        }
        rootNode.set("arguments", argumentsJson);
    }

    private void setUpObjects(ObjectNode rootNode) {
        ArrayNode jsonObjects = MAPPER.createArrayNode();
        mapped.values().stream().sorted((a, b) -> -(a.toString().length() - b.toString().length())).forEach(object -> {
            if (containsNode(jsonObjects, object)) {
                return;
            }
            jsonObjects.add(object);
        });

        rootNode.set("objects", jsonObjects);
    }

    private void setupMethodCalls(Method method, boolean includeObjectMethods, Map<Object, Map<Method, Set<Invocation>>> calls,
                                  ObjectNode rootNode) {
        ArrayNode jsonCalls = MAPPER.createArrayNode();
        for (Map.Entry<Object, Map<Method, Set<Invocation>>> called : calls.entrySet()) {
            Object invokedObject = called.getKey();
            ObjectNode objectNode = MAPPER.createObjectNode();

            objectNode.put("id", getID(invokedObject));
            ArrayNode methodCalls = MAPPER.createArrayNode();
            for (Map.Entry<Method, Set<Invocation>> methods : called.getValue().entrySet()) {
                ObjectNode methodNode = MAPPER.createObjectNode();

                if (!includeObjectMethods && isObjectMethod(methods.getKey()) ||
                    (method.getName().equals(methods.getKey().getName()) &&
                        Arrays.equals(method.getParameters(), methods.getKey().getParameters()
                        )
                    )
                ) {
                    continue;
                }

                methodNode.put("methodName", methods.getKey().getName());

                ArrayNode invocations = MAPPER.createArrayNode();

                for (Invocation invocation : methods.getValue()) {
                    ObjectNode invocationNode = MAPPER.createObjectNode();

                    if (getID(invocation.returnValue()) == -1) {
                        toJsonNode(invocation.returnValue());
                    }
                    invocationNode.put("return", getID(invocation.returnValue()));

                    ArrayNode parameters = MAPPER.createArrayNode();
                    for (Object parameter : invocation.arguments()) {
                        if (getID(parameter) == -1) {
                            toJsonNode(parameter);
                        }
                        parameters.add(getID(parameter));
                    }
                    invocationNode.set("parameter", parameters);

                    invocations.add(invocationNode);
                }

                methodNode.set("invocations", invocations);

                methodCalls.add(methodNode);
            }
            objectNode.set("methodCalls", methodCalls);

            if (!objectNode.get("methodCalls").isEmpty()) {
                jsonCalls.add(objectNode);
            }
        }
        rootNode.set("calls", jsonCalls);
    }

    public static List<StudentMethodCall> recreateCallAndInvoke(ObjectNode node) {
        return List.of(
            recreateCallAndInvokeUnMocked(node),
            recreateCallAndInvokeWithMock(node, false),
            recreateCallAndInvokeWithMock(node, true)
        );
    }

    private static StudentMethodCall recreateCallAndInvokeUnMocked(ObjectNode node) {
        MockConverterP converter = new MockConverterP(false);

        Method entryPoint = getEntryPoint(node);

        MockConverterP.createObjects(node, converter, CALLS_REAL_METHODS);

        Object invoked = converter.objects.get(node.get("invoked").asInt());

        Object[] arguments = Streams.stream(node.get("arguments")).map(id -> converter.objects.get(id.asInt())).toArray();

        Object returnValue = null;
        Throwable exception = null;
        try {
            returnValue = ReflectionUtilsP.callMethod(invoked, entryPoint, arguments);
        } catch (Throwable t) {
            exception = t;
        }

        return new StudentMethodCall(invoked, new Invocation(arguments, returnValue), exception);
    }

    private static StudentMethodCall recreateCallAndInvokeWithMock(ObjectNode node, boolean useFullSolution) {
        MockConverterP converter = new MockConverterP(useFullSolution);

        Method entryPoint = getEntryPoint(node);

        Object invoked = recreateObjectsAndCalls(node, converter, entryPoint);

        Object[] arguments = Streams.stream(node.get("arguments")).map(id -> converter.objects.get(id.asInt())).toArray();

        Object returnValue = null;
        Throwable exception = null;
        try {
            returnValue = ReflectionUtilsP.callMethod(invoked, entryPoint, arguments);
        } catch (Throwable t) {
            exception = t;
        }

        return new StudentMethodCall(invoked, new Invocation(arguments, returnValue), exception);
    }

    private static Method getEntryPoint(ObjectNode node) {
        String[] entryPointString = node.get("entryPoint").asText().split("#");
        String clazz = entryPointString[0];
        String[] method = entryPointString[1].replaceFirst(".$", "").split("\\(");

        try {
            return Class.forName(clazz).getMethod(
                method[0], Arrays.stream(method).skip(1).map(param -> {
                    try {
                        return Class.forName(param);
                    } catch (ClassNotFoundException e) {
                        return ReflectionUtilsP.getClassFromPrimitiveString(param);
                    }
                }).toArray(Class[]::new)
            );
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T recreateObjectsAndCalls(ObjectNode node, Method entryPoint) {
        return recreateObjectsAndCalls(node, new MockConverterP(true), entryPoint);
    }

    public static <T> T recreateObjectsAndCalls(ObjectNode node, MockConverterP converter, Method entryPoint) {
        Map<Integer, Map<Method, List<Invocation>>> calls = new HashMap<>();

        Answer<?> defaultAnswer = converter.createDefaultAnswer(calls, entryPoint);

        MockConverterP.createObjects(node, converter, defaultAnswer);

        if (converter.remap) {
            Pattern valuePattern = Pattern.compile("\"type\":\"(?<className>[a-zA-Z0-9.-]*)\"");

            var matcher = valuePattern.matcher(node.toString());

            StringBuffer remapped = new StringBuffer();
            while (matcher.find()) {
                String className = matcher.group("className");
                if (className.contains(SOLUTION_PACKAGE_INFIX) || className.contains("null")) {
                    continue;
                }
                try {
                    Class<?> studentClass = Class.forName(className);
                    Class<?> solClass = getSolution(studentClass);
                    if (solClass != null) {
                        matcher.appendReplacement(remapped, "\"type\":\"" + solClass.getName() + "\"");
                    }
                } catch (ClassNotFoundException e) {
                    // can be ignored as this should never happen
                    throw new RuntimeException(e);
                }
            }
            matcher.appendTail(remapped);

            ObjectNode mockNode;
            try {
                mockNode = (ObjectNode) MAPPER.readTree(remapped.toString());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            MockConverterP solConverter = new MockConverterP(false);
            solConverter.createObjects(mockNode, solConverter, CALLS_REAL_METHODS);

            solConverter.objects.forEach((id, solMock) -> {
                if (solMock.getClass().getName().contains("org.mockito.codegen")) {
                    return;
                }
                converter.solutionMocks.put(converter.objects.get(id), solMock);
            });
        }

        converter.createCalls(node, calls);

        return (T) converter.objects.get(node.get("invoked").asInt());
    }

    private void createCalls(ObjectNode node, Map<Integer, Map<Method, List<Invocation>>> calls) {
        ArrayNode callNode = (ArrayNode) node.get("calls");
        for (JsonNode call : callNode) {
            int objectID = call.get("id").asInt();
            Object object = objects.get(objectID);
            var objectMap = calls.computeIfAbsent(objectID, (id) -> new HashMap<>());

            ArrayNode methodNode = (ArrayNode) call.get("methodCalls");
            for (JsonNode method : methodNode) {
                Method methodObject = null;

                for (JsonNode invocation : method.get("invocations")) {
                    ArrayNode parameterNode = (ArrayNode) invocation.get("parameter");
                    List<Object> parameters = new ArrayList<>();

                    for (JsonNode parameter : parameterNode) {
                        parameters.add(objects.get(parameter.asInt()));
                    }

                    try {
                        if (methodObject == null) {
                            methodObject = ReflectionUtilsP.getMethodForParameters(
                                method.get("methodName").asText(),
                                object.getClass(),
                                parameters,
                                false
                            );
                        }

                        var methodMap = objectMap.computeIfAbsent(methodObject, (m) -> new ArrayList<>());
                        methodMap.add(new Invocation(
                            parameters.toArray(),
                            objects.get(invocation.get("return").asInt())
                        ));

                    } catch (NoSuchMethodException ignored) {
                    }
                }
            }
        }
    }

    private static void createObjects(ObjectNode node, MockConverterP converter, Answer<?> defaultAnswer) {
        ArrayNode objects = (ArrayNode) node.get("objects");
        for (JsonNode object : objects) {
            converter.fromJsonNode((ObjectNode) object, defaultAnswer);
        }
    }

    private @NotNull Answer<Object> createDefaultAnswer(Map<Integer, Map<Method, List<Invocation>>> calls, Method entryPoint) {
        return (mockInvocation) -> {

            if (mockInvocation.getMethod().equals(entryPoint)) {
                return callRealMethod(mockInvocation);
            }

            //replace call with call to solution
            if (solutionMocks.containsKey(mockInvocation.getMock())) {
                return replaceCallWithSolution(mockInvocation);
            }

            int objectID = getID(mockInvocation.getMock());
            Map<Method, List<Invocation>> objectCalls = calls.get(objectID);

            if (objectCalls == null || !objectCalls.containsKey(mockInvocation.getMethod())) {
                return callRealMethod(mockInvocation);
            }

            List<Invocation> invocations = objectCalls.get(mockInvocation.getMethod());
            Optional<Invocation> invocation = invocations.stream()
                .filter((inv) -> Arrays.deepEquals(inv.arguments(), mockInvocation.getArguments()))
                .findFirst();

            if (invocation.isPresent()) {
                return invocation.get().returnValue();
            }

            return callRealMethod(mockInvocation);
        };
    }

    private Object callRealMethod(InvocationOnMock mockInvocation) {
        try {
            return mockInvocation.callRealMethod();
        } catch (CrashException e) {
            throw e;
        } catch (MockitoException e) {
            System.err.println("Tried to call \"" + mockInvocation.getMethod() + "\" on class " + mockInvocation.getMock()
                .getClass());
            e.printStackTrace();
        } catch (AssertionFailedError e) {
            throw e;
        } catch (Throwable e) {
            String stacktrace = ReflectionUtilsP.formatStackTrace(e);

            Context context = contextBuilder()
                .add("Object", mockInvocation.getMock())
                .add("Parameters", Arrays.stream(mockInvocation.getArguments()).map(Object::toString).toList())
                .add("Exception Class", e.getClass())
                .add("Message", e.getMessage())
                .add("Stacktrace", stacktrace)
                .build();
            fail(context, r -> "Method " + mockInvocation.getMethod().getName() + "() threw an exception!");
        }
        //should never happen here to appease compiler
        throw new RuntimeException("fail did not trigger correctly!");
    }

    private Object replaceCallWithSolution(InvocationOnMock mockInvocation)
        throws IllegalAccessException {
        Object solMock = solutionMocks.get(mockInvocation.getMock());

        //map all parameters to solution mocks if available
        Object[] params = Arrays.stream(mockInvocation.getArguments())
            .map(obj -> {
                if (solutionMocks.containsKey(obj)) {
                    return solutionMocks.get(obj);
                }
                return obj;
            }).toArray();

        Object returnedObject;
        try {
            returnedObject =
                ReflectionUtilsP.getMethodForParameters(mockInvocation.getMethod().getName(), solMock.getClass(), List.of(params))
                    .invoke(solMock, params);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Could not find Method %s in Class %s".formatted(
                mockInvocation.getMethod().getName(),
                solMock.getClass()
            ));
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof CrashException crash) {
                throw crash;
            }
            String stacktrace = formatStackTrace(cause);

            Context context = contextBuilder()
                .add("Class", mockInvocation.getMock().getClass())
                .add("Method", mockInvocation.getMethod().getName())
                .add("Object", mockInvocation.getMock())
                .add(
                    "Parameters",
                    Arrays.stream(mockInvocation.getArguments()).map(Object::toString).collect(Collectors.joining(", "))
                )
                .add("Exception Class", cause.getClass())
                .add("Exception message", cause.getMessage())
                .add("Stacktrace", stacktrace)
                .build();
            fail(context, r -> "Could not correctly invoke Solution as it threw an exception!");
            throw new RuntimeException();
        }

        return getStudentObjectForSolution(returnedObject);
    }

    private @Nullable Object getStudentObjectForSolution(Object returnedObject) {

        if (returnedObject == null) {
            return null;
        } else if (solutionMocks.inverse().containsKey(returnedObject)) {
            return solutionMocks.inverse().get(returnedObject);
        } else if (actsLikePrimitive(returnedObject.getClass())) {
            return returnedObject;
        } else if (typeMapperJSON.keySet()
            .stream()
            .anyMatch(mappedClazz -> mappedClazz.isAssignableFrom(returnedObject.getClass()))) {

            Function<Object, Object> solMapper = solutionMapper.entrySet()
                .stream()
                .filter(mappedClazz -> mappedClazz.getKey().isAssignableFrom(returnedObject.getClass()))
                .findFirst()
                .get()
                .getValue();

            return solMapper.apply(returnedObject);
        } else if (getStudentClass(returnedObject.getClass()) == null) {
            return returnedObject;
        } else {
            for (Map.Entry<Object, Object> mockEntry : solutionMocks.entrySet()) {
                if (ReflectionUtilsP.equalsForMocks(mockEntry.getValue(), returnedObject)) {
                    return mockEntry.getKey();
                }
            }

            Object studentObject = mock(getStudentClass(returnedObject.getClass()));

            ReflectionUtilsP.copyFields(returnedObject, studentObject);
            solutionMocks.put(studentObject, returnedObject);
            System.out.println("returned new mock");
            return studentObject;
        }
    }

    private Integer getID(Object object) {
        if (!objects.containsValue(object)) {
            return -1;
        }
        return objects.inverse().get(object);
    }

    public static <T> T deepConvertToMocks(T toConvert, Answer<?> defaultAnswer) {
        var json = new MockConverterP().toJsonNode(toConvert);
        return new MockConverterP().fromJsonNode(json, defaultAnswer);
    }

    private static BiMap<Class<?>, Class<?>> solutions = HashBiMap.create();

    @SuppressWarnings("removal")
    public static Class<?> getSolution(Class<?> studentClass) {
        if (solutions.containsKey(studentClass)) {
            return solutions.get(studentClass);
        }

        String classPackageName = studentClass.getPackageName();
        //test if class is even part of exercise

        if (!classPackageName.matches("h\\d\\d(\\.|$).*")) {
            return null;
        }

        if (hasSolution != null && !hasSolution) {
            return null;
        }

        Package closestPackage = ReflectionUtilsP.getAllPackagesInExercise(studentClass).stream()
            .min((a, b) -> {
                    double simA = MatchingUtils.similarity(a.getName(), classPackageName);
                    double simB = MatchingUtils.similarity(b.getName(), classPackageName);

                    if (simA > simB) {
                        return -1;
                    } else if (simB > simA) {
                        return 1;
                    }
                    return 0;
                }
            ).orElse(null);

        if (closestPackage == null) {
            return null;
        }

        Pattern pattern = Pattern.compile("(?<exercise>[a-zA-Z0-9]{3})(\\.(?<package>.*))?");
        Matcher matcher = pattern.matcher(closestPackage.getName());

        //needed for match to work. return may be ignored
        matcher.matches();

        String exercise = matcher.group("exercise") + ".";

        String pack = matcher.group("package") != null ? "." + matcher.group("package") : "";

        String solutionPackage = exercise + SOLUTION_PACKAGE_INFIX + pack;

        PackageLink packageLink = ReflectionUtilsP.getPackageLink(solutionPackage);

        TypeLink link = packageLink.getType(BasicStringMatchers.similar(studentClass.getSimpleName(), 0.75));
        if (link == null) {
            hasSolution = false;
            return null;
        }

        solutions.put(studentClass, link.reflection());

        return link.reflection();
    }

    public static Class<?> getStudentClass(Class<?> solutionClass) {
        return solutions.inverse().get(solutionClass);
    }

    public static boolean containsNode(JsonNode object, JsonNode toFind) {
        for (JsonNode inner : object) {
            if (inner.equals(toFind) || inner.get("id") != null && inner.get("id").asInt() == toFind.get("id").asInt()) {
                return true;
            }
            if (containsNode(inner, toFind)) {
                return true;
            }
        }
        return false;
    }
}
