package h11;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class MusicStreaming {
    private final List<Artist> artists;
    private final List<User> users;

    public MusicStreaming(List<Artist> artists, List<User> users) {
        this.artists = artists;
        this.users = users;
    }

    public List<Song> getAllSongs() {
        return artists.stream()
            .flatMap(artist -> artist.getAllSongs().stream())
            .toList();
    }

    public List<Song> getSongsLongerThan(int durationInSeconds) {
        return getAllSongs().stream()
            .filter(song -> song.isLongerThan(durationInSeconds))
            .toList();
    }

    public List<Genre> getAllGenres() {
        return artists.stream()
            .flatMap(artist -> artist.getGenres().stream())
            .distinct()
            .toList();
    }

    public Map<Genre, List<Song>> getSongsByGenre() {
        return artists.stream()
            .flatMap(artist -> artist.albums().stream())
            .collect(Collectors.groupingBy(
                Album::genre,
                Collectors.flatMapping(album -> album.songs().stream(), Collectors.toList()))
            );
    }

    public Map<Song, Long> getGlobalPlayCounts() {
        return users.stream()
            .flatMap(user -> user.getPlayCounts().stream())
            .collect(Collectors.groupingBy(
                Map.Entry::getKey,
                Collectors.summingLong(Map.Entry::getValue))
            );
    }

    public List<String> getTopSongs() {
        return getGlobalPlayCounts().entrySet().stream()
            .sorted(Map.Entry.<Song, Long>comparingByValue().reversed())
            .limit(20)
            .map(entry -> String.format("%s (%d plays)", entry.getKey().name(), entry.getValue()))
            .toList();
    }

    public Map<Artist, Long> getArtistPlaytime() {
        Map<Song, Long> globalPlayCounts = getGlobalPlayCounts();
        return artists.stream()
            .collect(Collectors.toMap(
                artist -> artist,
                artist -> artist.getAllSongs().stream()
                    .mapToLong(song -> globalPlayCounts.getOrDefault(song, 0L) * song.durationInSeconds())
                    .sum()
            ));
    }

    public Artist getMostPlayedArtist() {
        return getArtistPlaytime().entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    public List<Song> searchSongs(Predicate<? super Song> predicate) {
        return getAllSongs().stream()
            .filter(predicate)
            .toList();
    }

    public void adjustPrice(double percentage) {
        users.forEach(user -> user.setPricePerMonth(
            user.pricePerMonth() * (1 + percentage / 100)
        ));
    }
}
