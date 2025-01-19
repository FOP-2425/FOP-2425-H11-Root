package h11;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kotlin.Pair;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Assertions2;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.util.stream.Stream;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class SongTest {

    @ParameterizedTest
    @MethodSource("provideIsLongerThan")
    public void testIsLongerThan(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        Assertions2.assertEquals(expected, actual.getSecond().returnValue(), context, r -> "IsLongerThan() did not return the expected value!");
    }

    private static Stream<Arguments> provideIsLongerThan() {
        return H11_Test.parseJsonFile("h11/Song_isLongerThan.json");
    }

}
