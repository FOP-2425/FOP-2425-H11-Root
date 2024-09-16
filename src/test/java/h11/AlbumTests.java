package h11;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlbumTests {

    @Test
    public void testGetAverageDuration() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300)
        );
        Album album = new Album("Album", Genre.ROCK, songs);

        assertEquals(200, album.getAverageDuration());

        Album album2 = new Album("Album 2", Genre.ROCK, List.of());
        assertEquals(0, album2.getAverageDuration());
    }

    @Test
    public void testGetSongsLongerThan() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300)
        );
        Album album = new Album("Album", Genre.ROCK, songs);

        assertEquals(List.of(songs.get(1), songs.get(2)), album.getSongsLongerThan(150));
    }
}
