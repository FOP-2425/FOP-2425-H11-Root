package h11;

import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.List;

/**
 * Represents an album with a title, genre, and a list of songs.
 *
 * @param title the title of the album
 * @param genre the genre of the album
 * @param songs the list of songs on the album
 */
public record Album(String title, Genre genre, List<Song> songs) {
    /**
     * Returns a list of songs that are longer than the specified duration.
     *
     * @param durationInSeconds the duration in seconds to compare the song lengths against
     * @return a list of songs that are longer than the specified duration
     */
    @StudentImplementationRequired
    public List<Song> getSongsLongerThan(int durationInSeconds) {
        return songs.stream()
            .filter(song -> song.isLongerThan(durationInSeconds))
            .toList();
    }

    /**
     * Calculates the average duration of the songs on the album.
     *
     * @return the average duration of the songs in seconds. If there are no songs, returns 0.
     */
    @StudentImplementationRequired
    public double getAverageDuration() {
        return songs.stream()
            .mapToInt(Song::durationInSeconds)
            .average()
            .orElse(0);
    }
}
