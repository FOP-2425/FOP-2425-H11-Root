package h11;

import com.fasterxml.jackson.databind.node.ObjectNode;
import kotlin.Pair;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.assertions.Context;

import java.util.List;
import java.util.stream.Stream;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class ArtistTestP extends H11_TestP {

    @ParameterizedTest
    @MethodSource("provideGetAllGenres_general")
    public void testGetAllGenres_general(ObjectNode node) {
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

    private static Stream<Arguments> provideGetAllGenres_general() {
        return H11_TestP.parseJsonFile("h11/Artist_getAllGenres_general.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetAllGenres_unique")
    public void testGetAllGenres_unique(ObjectNode node) {
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

    private static Stream<Arguments> provideGetAllGenres_unique() {
        return H11_TestP.parseJsonFile("h11/Artist_getAllGenres_unique.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetAllSongs")
    public void testGetAllSongs(ObjectNode node) {
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

    private static Stream<Arguments> provideGetAllSongs() {
        return H11_TestP.parseJsonFile("h11/Artist_getAllSongs.json");
    }

}
