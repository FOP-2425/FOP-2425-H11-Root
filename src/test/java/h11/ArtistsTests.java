package h11;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArtistsTests {
    @Test
    public void testGetAllSongs() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300),
            new Song("Song 4", 400),
            new Song("Song 5", 500),
            new Song("Song 6", 600)
        );
        Album album1 = new Album("Album 1", Genre.ROCK, songs.subList(0, 3));
        Album album2 = new Album("Album 2", Genre.ROCK, songs.subList(3, 6));
        Artist artist = new Artist("Artist", List.of(album1, album2));

        assertEquals(songs, artist.getAllSongs());
    }

    @Test
    public void testGetAllGenres() {
        Album album1 = new Album("Album 1", Genre.ROCK, List.of());
        Album album2 = new Album("Album 2", Genre.POP, List.of());
        Album album3 = new Album("Album 3", Genre.ROCK, List.of());
        Artist artist = new Artist("Artist", List.of(album1, album2, album3));

        assertEquals(List.of(Genre.ROCK, Genre.POP), artist.getAllGenres());
    }
}
