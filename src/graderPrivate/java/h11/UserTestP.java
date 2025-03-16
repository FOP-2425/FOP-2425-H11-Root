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

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class UserTestP extends H11_TestP {

    @ParameterizedTest
    @MethodSource("provideGetFavoriteSong_empty")
    public void testGetFavoriteSong_empty(ObjectNode node) {
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

                Assertions2.assertEquals(
                    expected,
                    actual.call().returnValue(),
                    context,
                    r -> "GetFavoriteSong() did not return the expected value!"
                );
                return;
            } catch (Throwable e) {
                lastCall = e;
            }
        }
        ReflectionUtils.getUnsafe().throwException(lastCall);
    }

    private static Stream<Arguments> provideGetFavoriteSong_empty() {
        return H11_TestP.parseJsonFile("h11/User_getFavoriteSong_empty.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetFavoriteSong_full")
    public void testGetFavoriteSong_full(ObjectNode node) {
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

                Assertions2.assertEquals(
                    expected,
                    actual.call().returnValue(),
                    context,
                    r -> "GetFavoriteSong() did not return the expected value!"
                );
                return;
            } catch (Throwable e) {
                lastCall = e;
            }
        }
        ReflectionUtils.getUnsafe().throwException(lastCall);
    }

    private static Stream<Arguments> provideGetFavoriteSong_full() {
        return H11_TestP.parseJsonFile("h11/User_getFavoriteSong_full.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetPlayCounts_order")
    public void testGetPlayCounts_order(ObjectNode node) {
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

                assertListEquals((List<?>) expected, (List<?>) actual.call().returnValue(), context);
                return;
            } catch (Throwable e) {
                lastCall = e;
            }
        }
        ReflectionUtils.getUnsafe().throwException(lastCall);
    }

    private static Stream<Arguments> provideGetPlayCounts_order() {
        return H11_TestP.parseJsonFile("h11/User_getPlayCounts_order.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetPlayCounts_return")
    public void testGetPlayCounts_return(ObjectNode node) {
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

    private static Stream<Arguments> provideGetPlayCounts_return() {
        return H11_TestP.parseJsonFile("h11/User_getPlayCounts_return.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetPlayedSongs_general")
    public void testGetPlayedSongs_general(ObjectNode node) {
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

    private static Stream<Arguments> provideGetPlayedSongs_general() {
        return H11_TestP.parseJsonFile("h11/User_getPlayedSongs_general.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetPlayedSongs_unique")
    public void testGetPlayedSongs_unique(ObjectNode node) {
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

    private static Stream<Arguments> provideGetPlayedSongs_unique() {
        return H11_TestP.parseJsonFile("h11/User_getPlayedSongs_unique.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetTopPlayedSongsList_formatting")
    public void testGetTopPlayedSongsList_formatting(ObjectNode node) {
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
                    .add("actual", actual.call().returnValue()).add(
                        "expected", expected)
                    .build();

                List<String> returnedList = ((List<String>) (actual.call().returnValue()));
                assertTrue(
                    ((List) (expected)).isEmpty() || (!returnedList.isEmpty()),
                    context,
                    r -> "getTopPlayedSongsList() returned empty list when a value should have been returned."
                );
                for (int i = 0; i < returnedList.size(); i++) {
                    int finalI = i;
                    assertTrue(
                        returnedList.get(i).matches(".* \\(\\d* plays\\)"),
                        context, r ->
                            ("getTopPlayedSongsList() returned String that does not match expected pattern at index " + finalI)
                                + "!"
                    );

                }
                ;
                return;
            } catch (Throwable e) {
                lastCall = e;
            }
        }
        ReflectionUtils.getUnsafe().throwException(lastCall);
    }

    private static Stream<Arguments> provideGetTopPlayedSongsList_formatting() {
        return H11_TestP.parseJsonFile("h11/User_getTopPlayedSongsList_formatting.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetTopPlayedSongsList_size")
    public void testGetTopPlayedSongsList_size(ObjectNode node) {
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
                    .add("actual", actual.call().returnValue()).add(
                        "expected", expected)
                    .build();

                List<String> returnedList = ((List<String>) (actual.call().returnValue()));
                assertTrue(
                    ((List) (expected)).isEmpty() || (!returnedList.isEmpty()),
                    context,
                    r -> "getTopPlayedSongsList() returned empty list when a value should have been returned."
                );
                assertTrue(
                    returnedList.size() <= 3,
                    context, r ->
                        "Expected getTopPlayedSongsList() to return list of length <= 3. But returned list of length " +
                            returnedList.size()
                );
                return;
            } catch (Throwable e) {
                lastCall = e;
            }
        }
        ReflectionUtils.getUnsafe().throwException(lastCall);
    }

    private static Stream<Arguments> provideGetTopPlayedSongsList_size() {
        return H11_TestP.parseJsonFile("h11/User_getTopPlayedSongsList_size.json");
    }

}
