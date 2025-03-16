package h11;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kotlin.Pair;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static h11.ReflectionUtilsP.actsLikePrimitive;
import static h11.ReflectionUtilsP.getFieldValue;
import static h11.ReflectionUtilsP.getSuperClassesIncludingSelf;
import static h11.ReflectionUtilsP.setFieldValue;
import static org.mockito.Mockito.mock;

public class JsonConverterP {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    protected Map<Class<?>, Function<Object, JsonNode>> typeMapperJSON = new HashMap<>() {{
        put(List.class, (list) -> toJsonNode((List<?>) list));
        put(Map.class, (map) -> toJsonNode((Map<?, ?>) map));
        put(
            Map.Entry.class,
            (entry) -> MAPPER.createArrayNode()
                .add(toJsonNode(((Map.Entry) entry).getKey()))
                .add(toJsonNode(((Map.Entry) entry).getValue()))
        );
    }};

    protected Map<Class<?>, BiFunction<JsonNode, Answer<?>, Object>> typeMapperObject = new HashMap<>() {{
        put(List.class, (node, answer) -> toList(node, answer));
        put(Map.class, (node, answer) -> toMap(node, answer));
        put(
            Map.Entry.class,
            (node, answer) -> Map.entry(
                fromJsonNode((ObjectNode) node.get(0), answer),
                fromJsonNode((ObjectNode) node.get(1), answer)
            )
        );
    }};

    public <T> List<T> toList(final JsonNode jsonNode, Answer<?> defaultAnswer) {
        if (jsonNode instanceof ArrayNode arrayNode && arrayNode.isEmpty()) {
            return new ArrayList<>();
        }
        return (List<T>) StreamSupport.stream(jsonNode.spliterator(), false)
            .map(node -> {
                try {
                    return fromJsonNode((ObjectNode) node, defaultAnswer);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            })
            .collect(Collectors.toCollection(ArrayList::new));
    }

    public <K, V> Map<K, V> toMap(final JsonNode jsonNode, Answer<?> defaultAnswer) {
        if (jsonNode instanceof ArrayNode arrayNode && arrayNode.isEmpty()) {
            return new HashMap<>();
        }
        Map<K, V> returnMap = new HashMap<>();
        StreamSupport.stream(jsonNode.spliterator(), false)
            .map(node -> {
                try {
                    Object key = fromJsonNode((ObjectNode) node.get(0), defaultAnswer);
                    Object value = fromJsonNode((ObjectNode) node.get(1), defaultAnswer);

                    return new Pair<>(key, value);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            })
            .forEach(entry -> returnMap.put((K) entry.component1(), (V) entry.component2()));
        return returnMap;
    }

    public ObjectNode toJsonNode(Object toMap) {
        ObjectNode rootNode = MAPPER.createObjectNode();

        if (toMap == null) {
            rootNode.put("type", "null");
            return rootNode;
        }

        Class<?> objectClass = toMap.getClass();
        List<Field> fields = List.of(objectClass.getDeclaredFields());

        //test if class is a lambda
        if (ReflectionUtilsP.isLambda(objectClass)) {
            rootNode.put("type", objectClass.getInterfaces()[0].getName());
        } else if (ReflectionUtilsP.isSyntheticMock(objectClass)) {
            rootNode.put("type", objectClass.getInterfaces()[0].getName());
        } else {
            rootNode.put("type", objectClass.getName());
        }


        if (toMap.getClass().isArray()) {
            toMap = List.of((Object[]) toMap);
        }

        if (actsLikePrimitive(toMap.getClass())) {
            rootNode.put("value", toMap.toString());
            return rootNode;
        } else if (getSuperClassesIncludingSelf(toMap.getClass()).stream().anyMatch(type -> typeMapperJSON.containsKey(type))) {
            rootNode.set(
                "value",
                typeMapperJSON.get(getSuperClassesIncludingSelf(toMap.getClass()).stream()
                    .filter(type -> typeMapperJSON.containsKey(type))
                    .findFirst()
                    .orElseThrow()).apply(toMap)
            );
            return rootNode;
        }

        ArrayNode fieldsJSON = MAPPER.createArrayNode();

        //Skip fields for mocks
        if (!ReflectionUtilsP.isSyntheticMock(objectClass)) {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                ObjectNode fieldJSON = MAPPER.createObjectNode();

                String name = field.getName();
                Object fieldValue = getFieldValue(toMap, name);

                fieldJSON.put("name", name);

                for (Map.Entry<String, JsonNode> value : toJsonNode(fieldValue).properties()) {
                    fieldJSON.set(value.getKey(), value.getValue());
                }

                fieldsJSON.add(fieldJSON);
            }
        }

        rootNode.set("fields", fieldsJSON);

        return rootNode;
    }

    public static <T> Class<T> getTypeFromNode(ObjectNode nodeToConvert) {
        String className = nodeToConvert.get("type").asText();

        if (className.equals("null")) {
            return null;
        }

        try {
            return (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T fromJsonNode(ObjectNode nodeToConvert, Answer<?> defaultAnswer) {
        Class<T> objectClass = getTypeFromNode(nodeToConvert);

        if (objectClass == null) {
            return null;
        }

        //needs to be here for primitive parsing
        if (actsLikePrimitive(objectClass)) {
            return extractPrimitiveValue(objectClass, nodeToConvert);
        } else if (getSuperClassesIncludingSelf(objectClass).stream().anyMatch(type -> typeMapperObject.containsKey(type))) {
            return (T) typeMapperObject.get(getSuperClassesIncludingSelf(objectClass).stream()
                .filter(type -> typeMapperObject.containsKey(type))
                .findFirst()
                .orElseThrow()).apply(nodeToConvert.get("value"), defaultAnswer);
        }

        T constructed;
        if (defaultAnswer != null) {
            constructed = mock(objectClass, defaultAnswer);
        } else {
            try {
                constructed = (T) ReflectionUtilsP.getUnsafe().allocateInstance(objectClass);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            }
        }

        for (JsonNode fieldNode : nodeToConvert.get("fields")) {
            ObjectNode field = (ObjectNode) fieldNode;
            String fieldName = field.get("name").asText();

            try {
                Object fieldValue = fromJsonNode(field, defaultAnswer);
                ;

                if (fieldValue != null) {
                    setFieldValue(constructed, fieldName, fieldValue);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return constructed;
    }

    public static <T> T extractPrimitiveValue(Class<T> objectClass, ObjectNode node) {
        String value = node.get("value").asText();
        Object fieldValue = null;

        if (String.class == objectClass) {
            fieldValue = value;
        } else if (boolean.class == objectClass || Boolean.class == objectClass) {
            fieldValue = Boolean.parseBoolean(value);
        } else if (byte.class == objectClass || Byte.class == objectClass) {
            fieldValue = Byte.parseByte(value);
        } else if (short.class == objectClass || Short.class == objectClass) {
            fieldValue = Short.parseShort(value);
        } else if (int.class == objectClass || Integer.class == objectClass) {
            fieldValue = Integer.parseInt(value);
        } else if (long.class == objectClass || Long.class == objectClass) {
            fieldValue = Long.parseLong(value);
        } else if (float.class == objectClass || Float.class == objectClass) {
            fieldValue = Float.parseFloat(value);
        } else if (double.class == objectClass || Double.class == objectClass) {
            fieldValue = Double.parseDouble(value);
        } else if (char.class == objectClass || Character.class == objectClass) {
            fieldValue = value.charAt(0);
        } else if (Enum.class.isAssignableFrom(objectClass)) {
            try {
                return (T) objectClass.getDeclaredMethod("valueOf", String.class).invoke(null, node.get("value").asText());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        return (T) fieldValue;
    }

    public JsonNode toJsonNode(List<?> toMap) {
        ArrayNode rootNode = MAPPER.createArrayNode();
        for (Object object : toMap) {
            rootNode.add(toJsonNode(object));
        }
        return rootNode;
    }

    public JsonNode toJsonNode(Map<?, ?> toMap) {
        ArrayNode rootNode = MAPPER.createArrayNode();
        for (Map.Entry<?, ?> object : toMap.entrySet()) {
            ArrayNode vals = MAPPER.createArrayNode();
            vals.add(toJsonNode(object.getKey()));
            vals.add(toJsonNode(object.getValue()));
            rootNode.add(vals);
        }
        return rootNode;
    }
}
