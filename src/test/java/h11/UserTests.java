package h11;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests {
    @Test
    public void testGetPlayedSongs() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300),
            new Song("Song 4", 400),
            new Song("Song 5", 500),
            new Song("Song 6", 600)
        );
        List<PlayedSong> playedSong = List.of(
            new PlayedSong(songs.get(0), new Date()),
            new PlayedSong(songs.get(1), new Date()),
            new PlayedSong(songs.get(2), new Date()),
            new PlayedSong(songs.get(3), new Date()),
            new PlayedSong(songs.get(1), new Date()),
            new PlayedSong(songs.get(2), new Date())
        );

        User user = new User("User", 10, playedSong);

        assertEquals(songs.subList(0, 4), user.getPlayedSongs());
    }

    @Test
    public void testGetPlayCount() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300),
            new Song("Song 4", 400),
            new Song("Song 5", 500),
            new Song("Song 6", 600)
        );
        List<PlayedSong> playedSong = List.of(
            new PlayedSong(songs.get(0), new Date()),
            new PlayedSong(songs.get(1), new Date()),
            new PlayedSong(songs.get(2), new Date()),
            new PlayedSong(songs.get(3), new Date()),
            new PlayedSong(songs.get(1), new Date()),
            new PlayedSong(songs.get(2), new Date())
        );

        User user = new User("User", 10, playedSong);

        assertEquals(2, user.getPlayCount(songs.get(1)));
        assertEquals(1, user.getPlayCount(songs.get(0)));
        assertEquals(0, user.getPlayCount(songs.get(5)));
    }

    @Test
    public void testGetPlayCounts() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300),
            new Song("Song 4", 400),
            new Song("Song 5", 500),
            new Song("Song 6", 600)
        );
        List<PlayedSong> playedSong = List.of(
            new PlayedSong(songs.get(0), new Date()),
            new PlayedSong(songs.get(1), new Date()),
            new PlayedSong(songs.get(2), new Date()),
            new PlayedSong(songs.get(3), new Date()),
            new PlayedSong(songs.get(1), new Date()),
            new PlayedSong(songs.get(2), new Date())
        );

        User user = new User("User", 10, playedSong);

        List<Map.Entry<Song, Long>> expected = List.of(
            Map.entry(songs.get(1), 2L),
            Map.entry(songs.get(2), 2L),
            Map.entry(songs.get(0), 1L),
            Map.entry(songs.get(3), 1L)
        );
        assertEquals(expected, user.getPlayCounts());
    }

    @Test
    public void testGetTopPlayedSongsList() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300),
            new Song("Song 4", 400),
            new Song("Song 5", 500),
            new Song("Song 6", 600)
        );
        List<PlayedSong> playedSong = List.of(
            new PlayedSong(songs.get(0), new Date()),
            new PlayedSong(songs.get(1), new Date()),
            new PlayedSong(songs.get(2), new Date()),
            new PlayedSong(songs.get(3), new Date()),
            new PlayedSong(songs.get(1), new Date()),
            new PlayedSong(songs.get(2), new Date()),
            new PlayedSong(songs.get(5), new Date())
        );

        User user = new User("User", 10, playedSong);

        List<String> expected = List.of(
            "Song 2 (2 plays)",
            "Song 3 (2 plays)",
            "Song 1 (1 plays)"
        );
        assertEquals(expected, user.getTopPlayedSongsList());
    }

    @Test
    public void testGetFavoriteSong() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300),
            new Song("Song 4", 400),
            new Song("Song 5", 500),
            new Song("Song 6", 600)
        );
        List<PlayedSong> playedSong = List.of(
            new PlayedSong(songs.get(0), new Date()),
            new PlayedSong(songs.get(1), new Date()),
            new PlayedSong(songs.get(2), new Date()),
            new PlayedSong(songs.get(3), new Date()),
            new PlayedSong(songs.get(1), new Date()),
            new PlayedSong(songs.get(2), new Date())
        );

        User user = new User("User", 10, playedSong);

        assertEquals(songs.get(1), user.getFavoriteSong());
    }
}
