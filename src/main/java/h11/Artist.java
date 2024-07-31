package h11;

import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.List;

/**
 * Represents an artist with a name and a list of albums.
 *
 * @param name   the name of the artist
 * @param albums the list of albums released by the artist
 */
public record Artist(String name, List<Album> albums) {
    /**
     * Retrieves all the songs from the artist's albums.
     *
     * @return a list of all songs from the artist's albums
     */
    @StudentImplementationRequired
    public List<Song> getAllSongs() {
        return albums.stream()
                .flatMap(album -> album.songs().stream())
                .toList();
    }

    /**
     * Retrieves all unique genres from the artist's albums.
     *
     * @return a list of all unique genres from the artist's albums
     */
    @StudentImplementationRequired
    public List<Genre> getAllGenres() {
        return albums.stream()
                .map(Album::genre)
                .distinct()
                .toList();
    }
}
