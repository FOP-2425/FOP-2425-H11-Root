package h11;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class User {
    private final String username;
    private double pricePerMonth;
    private List<PlayingHistory> playingHistory;

    public User(String username, double pricePerMonth, List<PlayingHistory> playingHistory) {
        this.username = username;
        this.pricePerMonth = pricePerMonth;
        this.playingHistory = playingHistory;
    }

    public String username() {
        return username;
    }

    public double pricePerMonth() {
        return pricePerMonth;
    }

    public List<Song> getPlayedSongs() {
        return playingHistory.stream()
                .map(PlayingHistory::song)
                .distinct()
                .toList();
    }

    public long getPlayCount(Song song) {
        return playingHistory.stream()
                .filter(playingHistory -> playingHistory.song().equals(song))
                .count();
    }

    public List<Map.Entry<Song, Long>> getPlayCounts() {
        return playingHistory.stream()
            .collect(Collectors.groupingBy(PlayingHistory::song, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<Song, Long>comparingByValue().reversed()
                .thenComparing(Map.Entry.comparingByKey(Comparator.comparing(Song::name))))
            .toList();

    }

    public List<String> getTopPlayedSongsList() {
        return getPlayCounts().stream()
                .limit(3)
                .map(entry -> String.format("%s (%d plays)", entry.getKey().name(), entry.getValue()))
                .toList();
    }

    public Song getFavoriteSong() {
        return getPlayCounts().stream()
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    public Stream<Song> generateRandomPlaylist() {
        return Stream.generate(this::getRandomSong);
    }

    @DoNotTouch
    public Song getRandomSong() {
        // TODO: implement this method
        return null;
    }

    @DoNotTouch
    public void setPricePerMonth(double pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (User) obj;
        return Objects.equals(this.username, that.username) &&
            Double.doubleToLongBits(this.pricePerMonth) == Double.doubleToLongBits(that.pricePerMonth) &&
            Objects.equals(this.playingHistory, that.playingHistory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, pricePerMonth, playingHistory);
    }

    @Override
    public String toString() {
        return "User[" +
            "username=" + username + ", " +
            "pricePerMonth=" + pricePerMonth + ", " +
            "playingHistory=" + playingHistory + ']';
    }

}
