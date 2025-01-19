package h11;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Streams;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.opentest4j.AssertionFailedError;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers;
import org.tudalgo.algoutils.tutor.general.match.MatchingUtils;
import org.tudalgo.algoutils.tutor.general.reflections.BasicPackageLink;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static h11.ReflectionUtils.isObjectMethod;
import static org.mockito.Mockito.mock;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.fail;

public class MockConverter extends JsonConverter {

    public static String SOLUTION_PACKAGE_INFIX = "solution";

    private static Boolean hasSolution;
    private BiMap<Integer, Object> objects = HashBiMap.create();
    private Map<Integer, ObjectNode> mapped = new HashMap<>();
    private BiMap<Object, Object> solutionMocks = HashBiMap.create();
    private boolean remap = false;

    public MockConverter() {
    }

    public MockConverter(boolean shouldApplyRemapping) {
        remap = shouldApplyRemapping;
    }

    @Override
    public ObjectNode toJsonNode(Object toMap) {

        int identityHashCode = System.identityHashCode(toMap);
        if (mapped.containsKey(identityHashCode)) {
            return mapped.get(identityHashCode);
        }
        if (objects.containsValue(toMap) && ReflectionUtils.actsLikePrimitive(toMap.getClass())) {
            return mapped.get(System.identityHashCode(objects.get(objects.inverse().get(toMap))));
        }

        ObjectNode rootNode = super.toJsonNode(toMap);

        if (mapped.containsKey(identityHashCode)) {
            return mapped.get(identityHashCode);
        }

        rootNode.put("id", objects.size());

        if (!objects.containsValue(toMap)) {
            objects.put(objects.size(), toMap);
        } //else {
//            throw new IllegalStateException("Could not map object " + toMap + " mush have been created unexpectedly elsewhere");
//        }
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

        if (remap) {
            Class<?> solClass = getSolution(getTypeFromNode(nodeToConvert));

            if (solClass != null) {
                System.out.println("Replacing %s with %s as solution".formatted(getTypeFromNode(nodeToConvert), solClass));
                String originalType = nodeToConvert.get("type").asText();
                nodeToConvert.put("type", solClass.getName());

                Pattern valuePattern = Pattern.compile("\"type\":\"%s\"".formatted(originalType));

                var matcher = valuePattern.matcher(nodeToConvert.toString());

                StringBuffer remapped = new StringBuffer();
                while (matcher.find()) {
                    matcher.appendReplacement(remapped, "\"type\":\"" + solClass.getName() + "\"");
                }
                matcher.appendTail(remapped);

                ObjectNode mockNode;
                try {
                    mockNode = (ObjectNode) MAPPER.readTree(remapped.toString());
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                Object mock = new JsonConverter().fromJsonNode(mockNode, Mockito.CALLS_REAL_METHODS);

                solutionMocks.put(constructed, mock);
            }
        }

        if (constructed.getClass().getName().contains("sol")) {
            throw new RuntimeException("AutoConverter should not create classes from Solution. Use JsonConverter instead.");
        }
        if (!objects.containsValue(constructed)) {
            objects.put(nodeToConvert.get("id").asInt(), constructed);
        } //else {
//            System.out.println(constructed);
//            throw new IllegalStateException("Could not create object " + constructed + " must have been created unexpectedly
//            elsewhere");
//        }

        return (T) constructed;
    }

    public void reset() {
        objects = HashBiMap.create();
        mapped = new HashMap<>();
        solutionMocks = HashBiMap.create();
    }

    public static ObjectNode mapCall(Object objectToCall, Method method, boolean includeObjectMethods, Object... arguments)
        throws InvocationTargetException, IllegalAccessException {
        MockConverter converter = new MockConverter();

        Map<Object, Map<Method, Set<Invocation>>> calls = new HashMap<>();

        AtomicBoolean stopRecordingCalls = new AtomicBoolean(false);

        Answer<?> answer = invocationOnMock -> {
            var returnValue = invocationOnMock.callRealMethod();
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
            if (ReflectionUtils.isLambda(argClass)) {
                Object original = arguments[i];
                arguments[i] = mock(
                    argClass.getInterfaces()[0], invocationOnMock -> {
                        var returnValue = invocationOnMock.getMethod().invoke(original, invocationOnMock.getArguments());
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
                Object existingMock = ReflectionUtils.findInFields(arguments[i], converted);
                if (existingMock != null) {
                    arguments[i] = existingMock;
                } else {
                    arguments[i] = deepConvertToMocks(arguments[i], answer);
                }

            }
        }

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

        stopRecordingCalls.set(true);

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

        rootNode.set("expected", new JsonConverter().toJsonNode(expected));

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

    public static Pair<Object, Invocation> recreateCallAndInvoke(ObjectNode node) {
        MockConverter converter = new MockConverter(true);

        String[] entryPointString = node.get("entryPoint").asText().split("#");
        String clazz = entryPointString[0];
        String[] method = entryPointString[1].replaceFirst(".$", "").split("\\(");

        Method entryPoint;
        try {
            entryPoint = Class.forName(clazz).getMethod(
                method[0], Arrays.stream(method).skip(1).map(param -> {
                    try {
                        return Class.forName(param);
                    } catch (ClassNotFoundException e) {
                        return ReflectionUtils.getClassFromPrimitiveString(param);
                    }
                }).toArray(Class[]::new)
            );
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        Object invoked = recreateObjectsAndCalls(node, converter, entryPoint);

        Object[] arguments = Streams.stream(node.get("arguments")).map(id -> converter.objects.get(id.asInt())).toArray();

        return new Pair<>(invoked, new Invocation(arguments, ReflectionUtils.callMethod(invoked, entryPoint, arguments)));
    }

    public static <T> T recreateObjectsAndCalls(ObjectNode node, Method entryPoint) {
        return recreateObjectsAndCalls(node, new MockConverter(true), entryPoint);
    }

    public static <T> T recreateObjectsAndCalls(ObjectNode node, MockConverter converter, Method entryPoint) {
        Map<Integer, Map<Method, List<Invocation>>> calls = new HashMap<>();

        Answer<?> defaultAnswer = converter.createDefaultAnswer(calls, entryPoint);

        converter.createObjects(node, converter, defaultAnswer);

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
                            methodObject = ReflectionUtils.getMethodForParameters(
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

    private void createObjects(ObjectNode node, MockConverter converter, Answer<?> defaultAnswer) {
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
        } catch (MockitoException e) {
            System.err.println("Tried to call \"" + mockInvocation.getMethod() + "\" on class " + mockInvocation.getMock()
                .getClass());
            e.printStackTrace();
        } catch (AssertionFailedError e) {
            throw e;
        } catch (Throwable e) {

            String stacktrace = Arrays.stream(e.getStackTrace())
                .map(Object::toString)
                .takeWhile(s -> !s.contains("java.base") && !s.contains("org.junit.jupiter"))
                .collect(Collectors.joining("\n                 "));
            if (stacktrace.isBlank()) {
                stacktrace = Arrays.stream(e.getStackTrace())
                    .map(Object::toString)
                    .takeWhile(s -> !s.contains("org.junit.jupiter"))
                    .collect(Collectors.joining("\n                 "));
            }

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
        throws IllegalAccessException, InvocationTargetException {
        Object solMock = solutionMocks.get(mockInvocation.getMock());

        //map all parameters to solution mocks if available
        Object[] params = Arrays.stream(mockInvocation.getArguments())
            .map(obj -> {
                if (solutionMocks.containsKey(obj)) {
                    return solutionMocks.get(obj);
                }
                return obj;
            }).toArray();

        Object returnedObject = null;
        try {
            returnedObject =
                ReflectionUtils.getMethodForParameters(mockInvocation.getMethod().getName(), solMock.getClass(), List.of(params))
                    .invoke(solMock, params);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        //map returned object back to student object
        if (solutionMocks.inverse().containsKey(returnedObject)) {
            return solutionMocks.inverse().get(returnedObject);
        } else {
//            boolean needsRemapping = typeMapperJSON.keySet().stream().anyMatch(unMappedClazz -> unMappedClazz
//            .isAssignableFrom(returnedObject.getClass()));

            //TODO remapping with newly created solution object and non json mockable classes
        }
        return returnedObject;
    }

    private Integer getID(Object object) {
        if (!objects.containsValue(object)) {
            return -1;
        }
        return objects.inverse().get(object);
    }

    public static <T> T deepConvertToMocks(T toConvert, Answer<?> defaultAnswer) {
        var json = new MockConverter().toJsonNode(toConvert);
        return new MockConverter().fromJsonNode(json, defaultAnswer);
    }

    @SuppressWarnings("removal")
    public static Class<?> getSolution(Class<?> studentClass) {
        String classPackageName = studentClass.getPackageName();
        //test if class is even part of exercise

        if (!classPackageName.matches("h\\d\\d(\\.|$).*")) {
            return null;
        }

        if (hasSolution != null && !hasSolution) {
            return null;
        }

        Package closestPackage = ReflectionUtils.getAllPackagesInExercise(studentClass).stream()
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

        TypeLink
            link = BasicPackageLink.of(solutionPackage).getType(BasicStringMatchers.similar(studentClass.getSimpleName(), 0.75));
        if (link == null) {
            hasSolution = false;
            return null;
        }

        return link.reflection();
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
