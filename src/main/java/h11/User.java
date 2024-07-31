package h11;

import org.tudalgo.algoutils.student.annotation.DoNotTouch;
import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class User {
    private final String username;
    private double pricePerMonth;
    private List<PlayedSong> playingHistory;

    @DoNotTouch
    public User(String username, double pricePerMonth, List<PlayedSong> playingHistory) {
        this.username = username;
        this.pricePerMonth = pricePerMonth;
        this.playingHistory = playingHistory;
    }

    @DoNotTouch
    public String getUsername() {
        return username;
    }
    @DoNotTouch
    public double getPricePerMonth() {
        return pricePerMonth;
    }
    @DoNotTouch
    public List<PlayedSong> getPlayingHistory() {
        return playingHistory;
    }

    @DoNotTouch
    public void setPricePerMonth(double pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
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
                .thenComparing(Map.Entry.comparingByKey(Comparator.comparing(Song::title)))
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
                .map(entry -> String.format("%s (%d plays)", entry.getKey().title(), entry.getValue()))
                .toList();
    }
}
