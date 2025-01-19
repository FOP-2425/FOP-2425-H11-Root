package h11;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.commons.lang3.stream.Streams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.provider.Arguments;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import spoon.reflect.code.CtDo;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtForEach;
import spoon.reflect.code.CtWhile;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertEquals;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.emptyContext;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions4.assertIsNotRecursively;

public class H11_TestP {

    public void assertContainsAll(List<?> expected, List<?> actual, Context context) {
        assertEquals(expected.size(), actual.size(), context, r -> "List does not contain same amount of items.");

        Context contextComplete = contextBuilder()
            .add(context)
            .add("actual", actual)
            .add("expected", expected)
            .build();

        for (int i = 0; i < expected.size(); i++) {
            int finalI = i;

            assertTrue(
                expected.stream().anyMatch(
                    exp -> {
                        if (exp == null){
                            return actual.get(finalI) == null;
                        }
                        return exp.equals(actual.get(finalI));
                    }),
                contextComplete,
                r -> "Actual List does not contain all expected Elements. Actual " + finalI + " not found!"
            );
        }
    }

    public void assertContainsAll(Map<?, ?> expected, Map<?, ?> actual, Context context) {
        assertEquals(expected.size(), actual.size(), context, r -> "Map does not contain same amount of items.");

        Context contextComplete = contextBuilder()
            .add(context)
            .add("actual", actual)
            .add("expected", expected)
            .build();

        List<Map.Entry<?, ?>> actualEntrys = new ArrayList<>(actual.entrySet());
        for (int i = 0; i < expected.size(); i++) {
            int finalI = i;

            Map.Entry<?, ?> actualEntry = actualEntrys.get(i);

            assertTrue(
                expected.entrySet().stream().anyMatch(
                    e -> {
                        Object key = e.getKey();
                        Object value = e.getValue();
                        if (!key.equals(actualEntry.getKey())) {
                            return false;
                        }
                        if (value == null) {
                            return actualEntry.getValue() == null;
                        } else {
                            return value.equals(actualEntry.getValue());
                        }
                    }),
                contextComplete,
                r -> "Actual Map does not contain all expected Elements. Actual " + finalI + " not found!"
            );
        }
    }

    public void assertListEquals(List<?> expected, List<?> actual, Context context) {
        assertEquals(expected.size(), actual.size(), context, r -> "List does not contain same amount of items.");

        Context contextComplete = contextBuilder()
            .add(context)
            .add("actual", actual)
            .add("expected", expected)
            .build();

        for (int i = 0; i < expected.size(); i++) {
            int finalI = i;

            boolean equals;
            if (expected.get(i) == null) {
                equals =  actual.get(finalI) == null;
            } else {
                equals =  expected.get(i).equals(actual.get(i));
            }

            assertTrue(equals, contextComplete, r -> "Actual List does not match expected Elements. Actual at " + finalI + " is not the same as expected!");
        }
    }

    public void assertNoLoopOrRecursion(Method methodToCheck) {
        assertIsNotRecursively(BasicMethodLink.of(methodToCheck).getCtElement(), emptyContext(), r -> "Method %s uses recursion.".formatted(methodToCheck.getName()));
        if (BasicMethodLink.of(methodToCheck).getCtElement().getElements(e-> e instanceof CtFor
            || e instanceof CtForEach
            || e instanceof CtWhile
            || e instanceof CtDo).isEmpty()) {
            return;
        }
        Assertions2.fail(
            emptyContext(),
            r -> "Method %s uses loops.".formatted(methodToCheck.getName())
        );
    }

    public static Stream<Arguments> parseJsonFile(String filename){
        final ArrayNode rootNode;
        if (TestCycleResolver.getTestCycle() != null){

            URL url = H11_TestP.class.getResource(filename.replaceFirst(ReflectionUtils.getExercisePrefix(H11_TestP.class) + "/", ""));
            Assertions.assertNotNull(url, "Could not find JSON file: " + filename);
            try {
                rootNode = (ArrayNode) new ObjectMapper().readTree(url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                rootNode = (ArrayNode) new ObjectMapper().readTree(Files.readString(Path.of("src/graderPrivate/resources/" + filename)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return Streams.of(rootNode).map(Arguments::of);

    }
}
