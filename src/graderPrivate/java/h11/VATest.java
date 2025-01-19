package h11;

import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.match.BasicStringMatchers;
import org.tudalgo.algoutils.tutor.general.reflections.BasicTypeLink;

import java.lang.reflect.Method;

@TestForSubmission
public class VATest extends H11_TestP{

    @Test
    public void testAlbum_getSongsLongerThan_va() {
        Method method = BasicTypeLink.of(Album.class).getMethod(BasicStringMatchers.identical("getSongsLongerThan")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testAlbum_getAverageDuration_va() {
        Method method = BasicTypeLink.of(Album.class).getMethod(BasicStringMatchers.identical("getAverageDuration")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testArtist_getAllSongs_va() {
        Method method = BasicTypeLink.of(Artist.class).getMethod(BasicStringMatchers.identical("getAllSongs")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testArtist_getAllGenres_va() {
        Method method = BasicTypeLink.of(Artist.class).getMethod(BasicStringMatchers.identical("getAllGenres")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testMusicStreaming_getAllSongs_va() {
        Method method = BasicTypeLink.of(MusicStreaming.class).getMethod(BasicStringMatchers.identical("getAllSongs")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testMusicStreaming_getAllGenres_va() {
        Method method = BasicTypeLink.of(MusicStreaming.class).getMethod(BasicStringMatchers.identical("getAllGenres")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testMusicStreaming_getSongsLongerThan_va() {
        Method method = BasicTypeLink.of(MusicStreaming.class).getMethod(BasicStringMatchers.identical("getSongsLongerThan")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testMusicStreaming_getTopPlayedSongsList_va() {
        Method method = BasicTypeLink.of(MusicStreaming.class).getMethod(BasicStringMatchers.identical("getTopPlayedSongsList")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testMusicStreaming_getAlbumsByGenre_va() {
        Method method = BasicTypeLink.of(MusicStreaming.class).getMethod(BasicStringMatchers.identical("getAlbumsByGenre")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testMusicStreaming_searchSongs_va() {
        Method method = BasicTypeLink.of(MusicStreaming.class).getMethod(BasicStringMatchers.identical("searchSongs")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testMusicStreaming_adjustPrice_va() {
        Method method = BasicTypeLink.of(MusicStreaming.class).getMethod(BasicStringMatchers.identical("adjustPrice")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testMusicStreaming_getArtistPlaytimes_va() {
        Method method = BasicTypeLink.of(MusicStreaming.class).getMethod(BasicStringMatchers.identical("getArtistPlaytimes")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testMusicStreaming_getGlobalPlayCounts_va() {
        Method method = BasicTypeLink.of(MusicStreaming.class).getMethod(BasicStringMatchers.identical("getGlobalPlayCounts")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testMusicStreaming_generateRandomPlaylist_va() {
        Method method = BasicTypeLink.of(MusicStreaming.class).getMethod(BasicStringMatchers.identical("generateRandomPlaylist")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testMusicStreaming_getArtistPlaytime_va() {
        Method method = BasicTypeLink.of(MusicStreaming.class).getMethod(BasicStringMatchers.identical("getArtistPlaytime")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testMusicStreaming_getMostPlayedArtist_va() {
        Method method = BasicTypeLink.of(MusicStreaming.class).getMethod(BasicStringMatchers.identical("getMostPlayedArtist")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testSong_isLongerThan_va() {
        Method method = BasicTypeLink.of(Song.class).getMethod(BasicStringMatchers.identical("isLongerThan")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testUser_getPlayedSongs_va() {
        Method method = BasicTypeLink.of(User.class).getMethod(BasicStringMatchers.identical("getPlayedSongs")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testUser_getPlayCounts_va() {
        Method method = BasicTypeLink.of(User.class).getMethod(BasicStringMatchers.identical("getPlayCounts")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testUser_getFavoriteSong_va() {
        Method method = BasicTypeLink.of(User.class).getMethod(BasicStringMatchers.identical("getFavoriteSong")).reflection();
        assertNoLoopOrRecursion(method);
    }

    @Test
    public void testUser_getTopPlayedSongsList_va() {
        Method method = BasicTypeLink.of(User.class).getMethod(BasicStringMatchers.identical("getTopPlayedSongsList")).reflection();
        assertNoLoopOrRecursion(method);
    }

}
