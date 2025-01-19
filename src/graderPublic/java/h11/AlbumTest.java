package h11;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kotlin.Pair;
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
public class AlbumTest extends H11_Test {


    @ParameterizedTest
    @MethodSource("provideGetSongsLongerThan")
    public void testGetSongsLongerThan(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        assertContainsAll((List<?>) expected, (List<?>) actual.getSecond().returnValue(), context);
    }

    private static Stream<Arguments> provideGetSongsLongerThan() {
        return H11_Test.parseJsonFile("h11/Album_getSongsLongerThan.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetAverageDuration_empty")
    public void testGetAverageDuration_empty(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .add("expected", expected)
            .add("actual", actual.getSecond().returnValue())
            .build();

        Assertions2.assertTrue( Math.abs((double) actual.getSecond().returnValue() - (double)expected) < 1E-10 , context, r -> "GetAverageDuration() did not return the expected value!");
    }

    private static Stream<Arguments> provideGetAverageDuration_empty() {
        return H11_Test.parseJsonFile("h11/Album_getAverageDuration_empty.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetAverageDuration_full")
    public void testGetAverageDuration_full(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .add("expected", expected)
            .add("actual", actual.getSecond().returnValue())
            .build();

        Assertions2.assertTrue( Math.abs((double) actual.getSecond().returnValue() - (double)expected) < 1E-10 , context, r -> "GetAverageDuration() did not return the expected value!");
    }

    private static Stream<Arguments> provideGetAverageDuration_full() {
        return H11_Test.parseJsonFile("h11/Album_getAverageDuration_full.json");
    }

}
