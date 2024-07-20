package h11;

import java.util.List;

public record Album(String name, Genre genre, List<Song> songs) {
    public double getAverageDuration() {
        return songs.stream()
                .mapToInt(Song::durationInSeconds)
                .average()
                .orElse(0);
    }

    public List<Song> getSongsLongerThan(int durationInSeconds) {
        return songs.stream()
                .filter(song -> song.isLongerThan(durationInSeconds))
                .toList();
    }
}
