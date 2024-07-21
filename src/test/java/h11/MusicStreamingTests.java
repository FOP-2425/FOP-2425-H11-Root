package h11;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MusicStreamingTests {
    @Test
    public void testGetAllSongs() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300),
            new Song("Song 4", 400),
            new Song("Song 5", 500),
            new Song("Song 6", 600),
            new Song("Song 7", 700),
            new Song("Song 8", 800),
            new Song("Song 9", 900),
            new Song("Song 10", 1000),
            new Song("Song 11", 1100),
            new Song("Song 12", 1200)
        );
        Album album1 = new Album("Album 1", Genre.ROCK, songs.subList(0, 3));
        Album album2 = new Album("Album 2", Genre.ROCK, songs.subList(3, 6));
        Album album3 = new Album("Album 3", Genre.POP, songs.subList(6, 9));
        Album album4 = new Album("Album 4", Genre.POP, songs.subList(9, 12));

        Artist artist1 = new Artist("Artist 1", List.of(album1, album2));
        Artist artist2 = new Artist("Artist 2", List.of(album3));
        Artist artist3 = new Artist("Artist 3", List.of(album4));

        MusicStreaming ms = new MusicStreaming(List.of(artist1, artist2, artist3), List.of());

        assertEquals(songs, ms.getAllSongs());
    }

    @Test
    public void testGetSongsLongerThan() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300),
            new Song("Song 4", 400),
            new Song("Song 5", 500),
            new Song("Song 6", 600),
            new Song("Song 7", 700),
            new Song("Song 8", 800),
            new Song("Song 9", 900),
            new Song("Song 10", 1000),
            new Song("Song 11", 1100),
            new Song("Song 12", 1200)
        );
        Album album1 = new Album("Album 1", Genre.ROCK, songs.subList(0, 3));
        Album album2 = new Album("Album 2", Genre.ROCK, songs.subList(3, 6));
        Album album3 = new Album("Album 3", Genre.POP, songs.subList(6, 9));
        Album album4 = new Album("Album 4", Genre.POP, songs.subList(9, 12));

        Artist artist1 = new Artist("Artist 1", List.of(album1, album2));
        Artist artist2 = new Artist("Artist 2", List.of(album3));
        Artist artist3 = new Artist("Artist 3", List.of(album4));

        MusicStreaming ms = new MusicStreaming(List.of(artist1, artist2, artist3), List.of());

        assertEquals(songs.subList(9, 12), ms.getSongsLongerThan(900));
    }

    @Test
    public void testGetAllGenres() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300),
            new Song("Song 4", 400),
            new Song("Song 5", 500),
            new Song("Song 6", 600),
            new Song("Song 7", 700),
            new Song("Song 8", 800),
            new Song("Song 9", 900),
            new Song("Song 10", 1000),
            new Song("Song 11", 1100),
            new Song("Song 12", 1200)
        );
        Album album1 = new Album("Album 1", Genre.ROCK, songs.subList(0, 3));
        Album album2 = new Album("Album 2", Genre.ROCK, songs.subList(3, 6));
        Album album3 = new Album("Album 3", Genre.POP, songs.subList(6, 9));
        Album album4 = new Album("Album 4", Genre.POP, songs.subList(9, 12));

        Artist artist1 = new Artist("Artist 1", List.of(album1, album2));
        Artist artist2 = new Artist("Artist 2", List.of(album3));
        Artist artist3 = new Artist("Artist 3", List.of(album4));

        MusicStreaming ms = new MusicStreaming(List.of(artist1, artist2, artist3), List.of());

        assertEquals(List.of(Genre.ROCK, Genre.POP), ms.getAllGenres());
    }

    @Test
    public void testGetSongsByGenre() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300),
            new Song("Song 4", 400),
            new Song("Song 5", 500),
            new Song("Song 6", 600),
            new Song("Song 7", 700),
            new Song("Song 8", 800),
            new Song("Song 9", 900),
            new Song("Song 10", 1000),
            new Song("Song 11", 1100),
            new Song("Song 12", 1200)
        );
        Album album1 = new Album("Album 1", Genre.ROCK, songs.subList(0, 3));
        Album album2 = new Album("Album 2", Genre.ROCK, songs.subList(3, 6));
        Album album3 = new Album("Album 3", Genre.POP, songs.subList(6, 9));
        Album album4 = new Album("Album 4", Genre.POP, songs.subList(9, 12));

        Artist artist1 = new Artist("Artist 1", List.of(album1, album2));
        Artist artist2 = new Artist("Artist 2", List.of(album3));
        Artist artist3 = new Artist("Artist 3", List.of(album4));

        MusicStreaming ms = new MusicStreaming(List.of(artist1, artist2, artist3), List.of());

        Map<Genre, List<Album>> expected = Map.of(
            Genre.ROCK, List.of(album1, album2),
            Genre.POP, List.of(album3, album4)
        );
        assertEquals(expected, ms.getAlbumsByGenre());
    }

    @Test
    public void testGetGlobalPlayCounts() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300),
            new Song("Song 4", 400),
            new Song("Song 5", 500),
            new Song("Song 6", 600),
            new Song("Song 7", 700),
            new Song("Song 8", 800),
            new Song("Song 9", 900),
            new Song("Song 10", 1000),
            new Song("Song 11", 1100),
            new Song("Song 12", 1200)
        );
        Album album1 = new Album("Album 1", Genre.ROCK, songs.subList(0, 3));
        Album album2 = new Album("Album 2", Genre.ROCK, songs.subList(3, 6));
        Album album3 = new Album("Album 3", Genre.POP, songs.subList(6, 9));
        Album album4 = new Album("Album 4", Genre.POP, songs.subList(9, 12));

        Artist artist1 = new Artist("Artist 1", List.of(album1, album2));
        Artist artist2 = new Artist("Artist 2", List.of(album3));
        Artist artist3 = new Artist("Artist 3", List.of(album4));

        User user1 = new User("User 1", 10, List.of(
            new PlayingHistory(songs.get(0), new Date()),
            new PlayingHistory(songs.get(1), new Date()),
            new PlayingHistory(songs.get(1), new Date()),
            new PlayingHistory(songs.get(2), new Date()),
            new PlayingHistory(songs.get(3), new Date()),
            new PlayingHistory(songs.get(4), new Date())
        ));
        User user2 = new User("User 2", 10, List.of(
            new PlayingHistory(songs.get(0), new Date()),
            new PlayingHistory(songs.get(1), new Date()),
            new PlayingHistory(songs.get(2), new Date()),
            new PlayingHistory(songs.get(3), new Date()),
            new PlayingHistory(songs.get(4), new Date())
        ));

        MusicStreaming ms = new MusicStreaming(List.of(artist1, artist2, artist3), List.of(user1, user2));

        List<Map.Entry<Song, Long>> expected = List.of(
            Map.entry(songs.get(1), 3L),
            Map.entry(songs.get(0), 2L),
            Map.entry(songs.get(2), 2L),
            Map.entry(songs.get(3), 2L),
            Map.entry(songs.get(4), 2L)
        );
        assertEquals(expected, ms.getGlobalPlayCounts());
    }

    @Test
    public void testGetTopPlayedSongsList() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300),
            new Song("Song 4", 400),
            new Song("Song 5", 500),
            new Song("Song 6", 600),
            new Song("Song 7", 700),
            new Song("Song 8", 800),
            new Song("Song 9", 900),
            new Song("Song 10", 1000),
            new Song("Song 11", 1100),
            new Song("Song 12", 1200)
        );
        Album album1 = new Album("Album 1", Genre.ROCK, songs.subList(0, 3));
        Album album2 = new Album("Album 2", Genre.ROCK, songs.subList(3, 6));
        Album album3 = new Album("Album 3", Genre.POP, songs.subList(6, 9));
        Album album4 = new Album("Album 4", Genre.POP, songs.subList(9, 12));

        Artist artist1 = new Artist("Artist 1", List.of(album1, album2));
        Artist artist2 = new Artist("Artist 2", List.of(album3));
        Artist artist3 = new Artist("Artist 3", List.of(album4));

        User user1 = new User("User 1", 10, List.of(
            new PlayingHistory(songs.get(0), new Date()),
            new PlayingHistory(songs.get(1), new Date()),
            new PlayingHistory(songs.get(1), new Date()),
            new PlayingHistory(songs.get(2), new Date()),
            new PlayingHistory(songs.get(3), new Date()),
            new PlayingHistory(songs.get(4), new Date())
        ));
        User user2 = new User("User 2", 10, List.of(
            new PlayingHistory(songs.get(0), new Date()),
            new PlayingHistory(songs.get(1), new Date()),
            new PlayingHistory(songs.get(2), new Date()),
            new PlayingHistory(songs.get(3), new Date()),
            new PlayingHistory(songs.get(4), new Date())
        ));

        MusicStreaming ms = new MusicStreaming(List.of(artist1, artist2, artist3), List.of(user1, user2));

        List<String> expected = List.of(
            "Song 2 (3 plays)",
            "Song 1 (2 plays)",
            "Song 3 (2 plays)",
            "Song 4 (2 plays)",
            "Song 5 (2 plays)"
        );
        assertEquals(expected, ms.getTopPlayedSongsList());
    }

    @Test
    public void testGetArtistPlaytime() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300),
            new Song("Song 4", 400),
            new Song("Song 5", 500),
            new Song("Song 6", 600),
            new Song("Song 7", 700),
            new Song("Song 8", 800),
            new Song("Song 9", 900),
            new Song("Song 10", 1000),
            new Song("Song 11", 1100),
            new Song("Song 12", 1200)
        );
        Album album1 = new Album("Album 1", Genre.ROCK, songs.subList(0, 3));
        Album album2 = new Album("Album 2", Genre.ROCK, songs.subList(3, 6));
        Album album3 = new Album("Album 3", Genre.POP, songs.subList(6, 9));
        Album album4 = new Album("Album 4", Genre.POP, songs.subList(9, 12));

        Artist artist1 = new Artist("Artist 1", List.of(album1, album2));
        Artist artist2 = new Artist("Artist 2", List.of(album3));
        Artist artist3 = new Artist("Artist 3", List.of(album4));

        User user1 = new User("User 1", 10, List.of(
            new PlayingHistory(songs.get(0), new Date()),
            new PlayingHistory(songs.get(1), new Date()),
            new PlayingHistory(songs.get(1), new Date()),
            new PlayingHistory(songs.get(2), new Date()),
            new PlayingHistory(songs.get(3), new Date()),
            new PlayingHistory(songs.get(4), new Date())
        ));
        User user2 = new User("User 2", 10, List.of(
            new PlayingHistory(songs.get(0), new Date()),
            new PlayingHistory(songs.get(1), new Date()),
            new PlayingHistory(songs.get(2), new Date()),
            new PlayingHistory(songs.get(3), new Date()),
            new PlayingHistory(songs.get(4), new Date()),
            new PlayingHistory(songs.get(6), new Date())
        ));

        MusicStreaming ms = new MusicStreaming(List.of(artist1, artist2, artist3), List.of(user1, user2));

        Map<Artist, Long> expected = Map.of(
            artist1, 3200L,
            artist2, 700L,
            artist3, 0L
        );
        assertEquals(expected.get(artist1), ms.getArtistPlaytime(artist1));
        assertEquals(expected.get(artist2), ms.getArtistPlaytime(artist2));
        assertEquals(expected.get(artist3), ms.getArtistPlaytime(artist3));
        assertEquals(expected, ms.getArtistPlaytime());
    }

    @Test
    public void testGetMostPlayedArtist() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300),
            new Song("Song 4", 400),
            new Song("Song 5", 500),
            new Song("Song 6", 600),
            new Song("Song 7", 700),
            new Song("Song 8", 800),
            new Song("Song 9", 900),
            new Song("Song 10", 1000),
            new Song("Song 11", 1100),
            new Song("Song 12", 1200)
        );
        Album album1 = new Album("Album 1", Genre.ROCK, songs.subList(0, 3));
        Album album2 = new Album("Album 2", Genre.ROCK, songs.subList(3, 6));
        Album album3 = new Album("Album 3", Genre.POP, songs.subList(6, 9));
        Album album4 = new Album("Album 4", Genre.POP, songs.subList(9, 12));

        Artist artist1 = new Artist("Artist 1", List.of(album1, album2));
        Artist artist2 = new Artist("Artist 2", List.of(album3));
        Artist artist3 = new Artist("Artist 3", List.of(album4));

        User user1 = new User("User 1", 10, List.of(
            new PlayingHistory(songs.get(0), new Date()),
            new PlayingHistory(songs.get(1), new Date()),
            new PlayingHistory(songs.get(1), new Date()),
            new PlayingHistory(songs.get(2), new Date()),
            new PlayingHistory(songs.get(3), new Date()),
            new PlayingHistory(songs.get(4), new Date())
        ));
        User user2 = new User("User 2", 10, List.of(
            new PlayingHistory(songs.get(0), new Date()),
            new PlayingHistory(songs.get(1), new Date()),
            new PlayingHistory(songs.get(2), new Date()),
            new PlayingHistory(songs.get(3), new Date()),
            new PlayingHistory(songs.get(4), new Date()),
            new PlayingHistory(songs.get(6), new Date())
        ));

        MusicStreaming ms = new MusicStreaming(List.of(artist1, artist2, artist3), List.of(user1, user2));

        assertEquals(artist1, ms.getMostPlayedArtist());
    }

    @Test
    public void testSearchSongs() {
        List<Song> songs = List.of(
            new Song("Song 1", 100),
            new Song("Song 2", 200),
            new Song("Song 3", 300),
            new Song("Song 4", 400),
            new Song("Song 5", 500),
            new Song("Song 6", 600),
            new Song("Song 7", 700),
            new Song("Song 8", 800),
            new Song("Song 9", 900),
            new Song("Song 10", 1000),
            new Song("Song 11", 1100),
            new Song("Song 12", 1200)
        );
        Album album1 = new Album("Album 1", Genre.ROCK, songs.subList(0, 3));
        Album album2 = new Album("Album 2", Genre.ROCK, songs.subList(3, 6));
        Album album3 = new Album("Album 3", Genre.POP, songs.subList(6, 9));
        Album album4 = new Album("Album 4", Genre.POP, songs.subList(9, 12));

        Artist artist1 = new Artist("Artist 1", List.of(album1, album2));
        Artist artist2 = new Artist("Artist 2", List.of(album3));
        Artist artist3 = new Artist("Artist 3", List.of(album4));

        MusicStreaming ms = new MusicStreaming(List.of(artist1, artist2, artist3), List.of());

        assertEquals(List.of(songs.get(0)), ms.searchSongs(Predicate.isEqual(songs.get(0))));
        assertEquals(List.of(songs.get(0), songs.get(1)), ms.searchSongs(song -> song.durationInSeconds() < 300));
    }

    @Test
    public void testAdjustPrice() {
        User user1 = new User("User 1", 20, List.of());
        User user2 = new User("User 2", 10, List.of());

        MusicStreaming ms = new MusicStreaming(List.of(), List.of(user1, user2));

        ms.adjustPrice(10);
        assertEquals(22, user1.pricePerMonth());
        assertEquals(11, user2.pricePerMonth());

        ms.adjustPrice(-20);
        assertEquals(17.6, user1.pricePerMonth());
        assertEquals(8.8, user2.pricePerMonth());
    }
}
