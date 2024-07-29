package h11;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class User {
    private final String username;
    private double pricePerMonth;
    private List<PlayedSong> playingHistory;

    public User(String username, double pricePerMonth, List<PlayedSong> playingHistory) {
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

    @StudentImplementationRequired
    public List<Song> getPlayedSongs() {
        return playingHistory.stream()
                .map(PlayedSong::song)
                .distinct()
                .toList();
    }

    @StudentImplementationRequired
    public long getPlayCount(Song song) {
        return playingHistory.stream()
                .filter(playedSong -> playedSong.song().equals(song))
                .count();
    }

    @StudentImplementationRequired
    public List<Map.Entry<Song, Long>> getPlayCounts() {
        return playingHistory.stream()
            .collect(Collectors.groupingBy(
                PlayedSong::song,
                Collectors.counting()
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<Song, Long>comparingByValue()
                .reversed()
                .thenComparing(Map.Entry.comparingByKey(Comparator.comparing(Song::name)))
            )
            .toList();
    }

    @StudentImplementationRequired
    public Song getFavoriteSong() {
        return getPlayCounts().stream()
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);
    }

    @StudentImplementationRequired
    public List<String> getTopPlayedSongsList() {
        return getPlayCounts().stream()
                .limit(3)
                .map(entry -> String.format("%s (%d plays)", entry.getKey().name(), entry.getValue()))
                .toList();
    }

    @DoNotTouch
    public void setPricePerMonth(double pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }
}
