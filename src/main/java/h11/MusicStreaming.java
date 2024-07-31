package h11;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a music streaming service with a list of artists and a list of users.
 *
 * @param artists the list of artists
 * @param users   the list of users
 */
public record MusicStreaming(List<Artist> artists, List<User> users) {
    /**
     * Retrieves all the songs from all the artists on the platform.
     *
     * @return a list of all songs available on the platform
     */
    @StudentImplementationRequired
    public List<Song> getAllSongs() {
        return artists.stream()
            .flatMap(artist -> artist.getAllSongs().stream())
            .toList();
    }

    /**
     * Generates a stream of random songs from the platform.
     *
     * @return a stream of random songs
     */
    @StudentImplementationRequired
    public Stream<Song> generateRandomPlaylist() {
        return Stream.generate(this::getRandomSong);
    }

    /**
     * Retrieves a random song from the platform's song list.
     *
     * @return a random song, or null if there are no songs available
     */
    @DoNotTouch
    public Song getRandomSong() {
        List<Song> allSongs = getAllSongs();
        if (allSongs.isEmpty()) {
            return null;
        }
        return allSongs.get((int) (Math.random() * allSongs.size()));
    }

    /**
     * Retrieves a list of songs that are longer than the specified duration.
     *
     * @param durationInSeconds the duration in seconds to compare the song lengths against
     * @return a list of songs that are longer than the specified duration
     */
    @StudentImplementationRequired
    public List<Song> getSongsLongerThan(int durationInSeconds) {
        return getAllSongs().stream()
            .filter(song -> song.isLongerThan(durationInSeconds))
            .toList();
    }

    /**
     * Retrieves all unique genres available on the platform.
     *
     * @return a list of all unique genres available on the platform
     */
    @StudentImplementationRequired
    public List<Genre> getAllGenres() {
        return artists.stream()
            .flatMap(artist -> artist.getAllGenres().stream())
            .distinct()
            .toList();
    }

    /**
     * Groups all albums by their genre.
     *
     * @return a map where the key is the genre and the value is a list of albums of that genre
     */
    @StudentImplementationRequired
    public Map<Genre, List<Album>> getAlbumsByGenre() {
        return artists.stream()
            .flatMap(artist -> artist.albums().stream())
            .collect(Collectors.groupingBy(
                Album::genre,
                Collectors.toList()
            ));
    }

    /**
     * Calculates the global play counts for all songs by summing the play counts from all users.
     *
     * @return a list of entries where each entry is a song and its total play count, sorted by play count in descending order.
     * Songs with the same play count are ordered alphabetically by the song title.
     */
    @StudentImplementationRequired
    public List<Map.Entry<Song, Long>> getGlobalPlayCounts() {
        return users.stream()
            .flatMap(user -> user.getPlayCounts().stream())
            .collect(Collectors.groupingBy(
                Map.Entry::getKey,
                Collectors.summingLong(Map.Entry::getValue)
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<Song, Long>comparingByValue()
                .reversed()
                .thenComparing(Map.Entry.comparingByKey(Comparator.comparing(Song::title)))
            )
            .toList();
    }

    /**
     * Retrieves a list of the top played songs formatted as strings.
     *
     * @return a list of strings representing the top 5 most played songs and their play counts in the format "[title] ([count] plays)"
     */
    @StudentImplementationRequired
    public List<String> getTopPlayedSongsList() {
        return getGlobalPlayCounts().stream()
            .limit(5)
            .map(entry -> String.format("%s (%d plays)", entry.getKey().title(), entry.getValue()))
            .toList();
    }

    /**
     * Retrieves the total playtime of an artist's songs based on global play counts.
     *
     * @param artist the artist whose playtime is to be calculated
     * @return the total playtime of the artist's songs in seconds
     */
    @StudentImplementationRequired
    public long getArtistPlaytime(Artist artist) {
        // Alternative 1:
        /*
        List<Map.Entry<Song, Long>> globalPlayCounts = getGlobalPlayCounts();

        return artist.getAllSongs().stream()
            .mapToLong(song -> globalPlayCounts.stream()
                .filter(entry -> entry.getKey().equals(song))
                .mapToLong(Map.Entry::getValue)
                .sum() * song.durationInSeconds()
            )
            .sum();
        */

        // Alternative 2:
        Map<Song, Long> globalPlayCounts = getGlobalPlayCounts()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return artist.getAllSongs().stream()
            .mapToLong(song -> globalPlayCounts.getOrDefault(song, 0L) * song.durationInSeconds())
            .sum();
    }

    /**
     * Retrieves the total playtimes for all artists.
     *
     * @return a map where the key is the artist and the value is their total playtime in seconds
     */
    @StudentImplementationRequired
    public Map<Artist, Long> getArtistPlaytimes() {
        return artists.stream()
            .collect(Collectors.toMap(
                artist -> artist,
                this::getArtistPlaytime
            ));
    }

    /**
     * Retrieves the most played artist based on the total playtime of their songs.
     *
     * @return the most played artist, or null if there are no artists
     */
    @StudentImplementationRequired
    public Artist getMostPlayedArtist() {
        return getArtistPlaytimes().entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    /**
     * Searches for songs that match the given predicate.
     *
     * @param predicate the predicate to filter songs
     * @return a list of songs that match the predicate
     */
    @StudentImplementationRequired
    public List<Song> searchSongs(Predicate<? super Song> predicate) {
        return getAllSongs().stream()
            .filter(predicate)
            .toList();
    }

    /**
     * Adjusts the monthly subscription price for all users by a given percentage.
     *
     * @param percentage the percentage to adjust the price by (e.g., 0.10 for a 10% increase)
     */
    @StudentImplementationRequired
    public void adjustPrice(double percentage) {
        users.forEach(user -> user.setPricePerMonth(
            user.getPricePerMonth() * (1 + percentage)
        ));
    }
}
