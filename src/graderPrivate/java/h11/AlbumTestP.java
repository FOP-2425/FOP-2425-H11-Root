package h11;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kotlin.Pair;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.util.List;
import java.util.stream.Stream;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class AlbumTestP extends H11_TestP {

    @ParameterizedTest
    @MethodSource("provideGetAverageDuration_empty")
    public void testGetAverageDuration_empty(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        List<StudentMethodCall> results = MockConverterP.recreateCallAndInvoke(node);

        if (results.stream().allMatch(it -> it.exception() != null)) {
            ReflectionUtils.getUnsafe().throwException(results.getLast().exception());
        }

        Throwable lastCall = null;
        for (StudentMethodCall actual : results) {
            try {
                Context context = contextBuilder()
                    .add("invoked", actual.invoked())
                    .add("parameters", actual.call().arguments())
                    .add("expected", expected)
                    .add("actual", actual.call().returnValue())
                    .build();

                Assertions2.assertTrue(
                    Math.abs((double) actual.call().returnValue() - (double) expected) < 1E-10,
                    context,
                    r -> "GetAverageDuration() did not return the expected value!"
                );
                return;
            } catch (Throwable e) {
                lastCall = e;
            }
        }
        ReflectionUtils.getUnsafe().throwException(lastCall);
    }

    private static Stream<Arguments> provideGetAverageDuration_empty() {
        return H11_TestP.parseJsonFile("h11/Album_getAverageDuration_empty.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetAverageDuration_full")
    public void testGetAverageDuration_full(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        List<StudentMethodCall> results = MockConverterP.recreateCallAndInvoke(node);

        if (results.stream().allMatch(it -> it.exception() != null)) {
            ReflectionUtils.getUnsafe().throwException(results.getLast().exception());
        }

        Throwable lastCall = null;
        for (StudentMethodCall actual : results) {
            try {
                Context context = contextBuilder()
                    .add("invoked", actual.invoked())
                    .add("parameters", actual.call().arguments())
                    .add("expected", expected)
                    .add("actual", actual.call().returnValue())
                    .build();

                Assertions2.assertTrue(
                    Math.abs((double) actual.call().returnValue() - (double) expected) < 1E-10,
                    context,
                    r -> "GetAverageDuration() did not return the expected value!"
                );
                return;
            } catch (Throwable e) {
                lastCall = e;
            }
        }
        ReflectionUtils.getUnsafe().throwException(lastCall);
    }

    private static Stream<Arguments> provideGetAverageDuration_full() {
        return H11_TestP.parseJsonFile("h11/Album_getAverageDuration_full.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetSongsLongerThan")
    public void testGetSongsLongerThan(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        List<StudentMethodCall> results = MockConverterP.recreateCallAndInvoke(node);

        if (results.stream().allMatch(it -> it.exception() != null)) {
            ReflectionUtils.getUnsafe().throwException(results.getLast().exception());
        }

        Throwable lastCall = null;
        for (StudentMethodCall actual : results) {
            try {
                Context context = contextBuilder()
                    .add("invoked", actual.invoked())
                    .add("parameters", actual.call().arguments())
                    .build();

                assertContainsAll((List<?>) expected, (List<?>) actual.call().returnValue(), context);
                return;
            } catch (Throwable e) {
                lastCall = e;
            }
        }
        ReflectionUtils.getUnsafe().throwException(lastCall);
    }

    private static Stream<Arguments> provideGetSongsLongerThan() {
        return H11_TestP.parseJsonFile("h11/Album_getSongsLongerThan.json");
    }

}
