package h11;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record MusicStreaming(List<Artist> artists, List<User> users) {
    @StudentImplementationRequired
    public List<Song> getAllSongs() {
        return artists.stream()
            .flatMap(artist -> artist.getAllSongs().stream())
            .toList();
    }

    @StudentImplementationRequired
    public Stream<Song> generateRandomPlaylist() {
        return Stream.generate(this::getRandomSong);
    }

    @DoNotTouch
    public Song getRandomSong() {
        return getAllSongs().get((int) (Math.random() * getAllSongs().size()));
    }

    @StudentImplementationRequired
    public List<Song> getSongsLongerThan(int durationInSeconds) {
        return getAllSongs().stream()
            .filter(song -> song.isLongerThan(durationInSeconds))
            .toList();
    }

    @StudentImplementationRequired
    public List<Genre> getAllGenres() {
        return artists.stream()
            .flatMap(artist -> artist.getGenres().stream())
            .distinct()
            .toList();
    }

    @StudentImplementationRequired
    public Map<Genre, List<Album>> getAlbumsByGenre() {
        return artists.stream()
            .flatMap(artist -> artist.albums().stream())
            .collect(Collectors.groupingBy(
                Album::genre,
                Collectors.toList()
            ));
    }

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

    @StudentImplementationRequired
    public List<String> getTopPlayedSongsList() {
        return getGlobalPlayCounts().stream()
            .limit(5)
            .map(entry -> String.format("%s (%d plays)", entry.getKey().title(), entry.getValue()))
            .toList();
    }

    @StudentImplementationRequired
    public long getArtistPlaytime(Artist artist) {
        // Alternative 1:
//        List<Map.Entry<Song, Long>> globalPlayCounts = getGlobalPlayCounts();
//
//        return artist.getAllSongs().stream()
//            .mapToLong(song -> globalPlayCounts.stream()
//                .filter(entry -> entry.getKey().equals(song))
//                .mapToLong(Map.Entry::getValue)
//                .sum() * song.durationInSeconds()
//            )
//            .sum();

        // Alternative 2:
        Map<Song, Long> globalPlayCounts = getGlobalPlayCounts()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return artist.getAllSongs().stream()
            .mapToLong(song -> globalPlayCounts.getOrDefault(song, 0L) * song.durationInSeconds())
            .sum();
    }

    @StudentImplementationRequired
    public Map<Artist, Long> getArtistPlaytimes() {
        return artists.stream()
            .collect(Collectors.toMap(
                artist -> artist,
                this::getArtistPlaytime
            ));
    }

    @StudentImplementationRequired
    public Artist getMostPlayedArtist() {
        return getArtistPlaytimes().entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    @StudentImplementationRequired
    public List<Song> searchSongs(Predicate<? super Song> predicate) {
        return getAllSongs().stream()
            .filter(predicate)
            .toList();
    }

    @StudentImplementationRequired
    public void adjustPrice(double percentage) {
        users.forEach(user -> user.setPricePerMonth(
            user.pricePerMonth() * (1 + percentage / 100)
        ));
    }
}
