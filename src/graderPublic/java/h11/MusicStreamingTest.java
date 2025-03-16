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
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.contextBuilder;

@TestForSubmission
public class MusicStreamingTest extends H11_Test {

    @ParameterizedTest
    @MethodSource("provideAdjustPrice_general")
    public void testAdjustPrice_general(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        Assertions2.assertEquals(expected, actual.getFirst(), context, r -> "AdjustPrice() did not return the expected value!");
    }

    private static Stream<Arguments> provideAdjustPrice_general() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_adjustPrice_general.json");
    }


    @ParameterizedTest
    @MethodSource("provideAdjustPrice_zero")
    public void testAdjustPrice_zero(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        Assertions2.assertEquals(expected, actual.getFirst(), context, r -> "AdjustPrice() did not return the expected value!");
    }

    private static Stream<Arguments> provideAdjustPrice_zero() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_adjustPrice_zero.json");
    }


    @ParameterizedTest
    @MethodSource("provideGenerateRandomPlaylist")
    public void testGenerateRandomPlaylist(ObjectNode node) throws NoSuchMethodException {
        MusicStreaming invoked = MockConverter.recreateObjectsAndCalls(node, MusicStreaming.class.getDeclaredMethod("getRandomSong"));

        Context context = contextBuilder()
            .add("invoked", invoked)
            .build();

        doAnswer(CALLS_REAL_METHODS).when(invoked).getRandomSong();

        Optional<?> returned = invoked.generateRandomPlaylist().skip(10000).findFirst();
        assertTrue(returned.isPresent(), context, r -> "generateRandomPlaylist() does not return infinite stream");

        verify(invoked, atLeast(5)).getRandomSong();

        Song newSong = new Song("", 75489);
        doReturn(newSong).when(invoked).getRandomSong();

        assertTrue(invoked.generateRandomPlaylist().limit(10).allMatch(newSong::equals), context, r -> "Objects returned by generateRandomPlaylist() are not selected by getRandomSong()");

    }

    private static Stream<Arguments> provideGenerateRandomPlaylist() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_generateRandomPlaylist.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetAlbumsByGenre")
    public void testGetAlbumsByGenre(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        assertContainsAll((Map<?, ?>) expected, (Map<?, ?>) actual.getSecond().returnValue(), context);
    }

    private static Stream<Arguments> provideGetAlbumsByGenre() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_getAlbumsByGenre.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetAllGenres_general")
    public void testGetAllGenres_general(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        assertContainsAll((List<?>) expected, (List<?>) actual.getSecond().returnValue(), context);
    }

    private static Stream<Arguments> provideGetAllGenres_general() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_getAllGenres_general.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetAllGenres_unique")
    public void testGetAllGenres_unique(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        assertContainsAll((List<?>) expected, (List<?>) actual.getSecond().returnValue(), context);
    }

    private static Stream<Arguments> provideGetAllGenres_unique() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_getAllGenres_unique.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetAllSongs_general")
    public void testGetAllSongs_general(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        assertContainsAll((List<?>) expected, (List<?>) actual.getSecond().returnValue(), context);
    }

    private static Stream<Arguments> provideGetAllSongs_general() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_getAllSongs_general.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetAllSongs_unique")
    public void testGetAllSongs_unique(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        assertContainsAll((List<?>) expected, (List<?>) actual.getSecond().returnValue(), context);
    }

    private static Stream<Arguments> provideGetAllSongs_unique() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_getAllSongs_unique.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetArtistPlaytime")
    public void testGetArtistPlaytime(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        Assertions2.assertEquals(expected, actual.getSecond().returnValue(), context, r -> "GetArtistPlaytime() did not return the expected value!");
    }

    private static Stream<Arguments> provideGetArtistPlaytime() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_getArtistPlaytime.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetArtistPlaytimes")
    public void testGetArtistPlaytimes(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        assertContainsAll((Map<?, ?>) expected, (Map<?, ?>) actual.getSecond().returnValue(), context);
    }

    private static Stream<Arguments> provideGetArtistPlaytimes() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_getArtistPlaytimes.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetGlobalPlayCounts_general")
    public void testGetGlobalPlayCounts_general(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        assertContainsAll((List<?>) expected, (List<?>) actual.getSecond().returnValue(), context);
    }

    private static Stream<Arguments> provideGetGlobalPlayCounts_general() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_getGlobalPlayCounts_general.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetGlobalPlayCounts_order")
    public void testGetGlobalPlayCounts_order(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        assertContainsAll((List<?>) expected, (List<?>) actual.getSecond().returnValue(), context);
    }

    private static Stream<Arguments> provideGetGlobalPlayCounts_order() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_getGlobalPlayCounts_order.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetMostPlayedArtist_empty")
    public void testGetMostPlayedArtist_empty(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        Assertions2.assertEquals(expected, actual.getSecond().returnValue(), context, r -> "GetMostPlayedArtist() did not return the expected value!");
    }

    private static Stream<Arguments> provideGetMostPlayedArtist_empty() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_getMostPlayedArtist_empty.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetMostPlayedArtist_full")
    public void testGetMostPlayedArtist_full(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        Assertions2.assertEquals(expected, actual.getSecond().returnValue(), context, r -> "GetMostPlayedArtist() did not return the expected value!");
    }

    private static Stream<Arguments> provideGetMostPlayedArtist_full() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_getMostPlayedArtist_full.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetSongsLongerThan_general")
    public void testGetSongsLongerThan_general(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        assertContainsAll((List<?>) expected, (List<?>) actual.getSecond().returnValue(), context);
    }

    private static Stream<Arguments> provideGetSongsLongerThan_general() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_getSongsLongerThan_general.json");
    }


    @ParameterizedTest
    @MethodSource("provideGetTopPlayedSongsList")
    public void testGetTopPlayedSongsList(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .add("actual", actual.getSecond().returnValue()).add(
                "expected", expected)
            .build();

        List<String> returnedList = ((List<String>) (actual.getSecond().returnValue()));
        assertTrue(
            returnedList.size() <= 5,
            context, r ->
                "Expected getTopPlayedSongsList() to return list of length <= 3. But returned list of length " +
                    returnedList.size());
        for (int i = 0; i < returnedList.size(); i++) {
            int finalI = i;
            assertTrue(
                returnedList.get(i).matches(".* ?\\(\\d* plays\\)"),
                context, r ->
                    ("getTopPlayedSongsList() returned String that does not match expected pattern at index " + finalI) + "!");

        };
    }

    private static Stream<Arguments> provideGetTopPlayedSongsList() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_getTopPlayedSongsList.json");
    }


    @ParameterizedTest
    @MethodSource("provideSearchSongs")
    public void testSearchSongs(ObjectNode node) {
        Object expected = new JsonConverter().fromJsonNode((ObjectNode) node.get("expected"), null);
        Pair<Object, Invocation> actual = MockConverter.recreateCallAndInvoke(node);

        Context context = contextBuilder()
            .add("invoked", actual.getFirst())
            .add("parameters", actual.getSecond().arguments())
            .build();

        assertContainsAll((List<?>) expected, (List<?>) actual.getSecond().returnValue(), context);
    }

    private static Stream<Arguments> provideSearchSongs() {
        return H11_Test.parseJsonFile("h11/MusicStreaming_searchSongs.json");
    }

}
