package h11;

import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

import java.util.List;

public record Artist(String name, List<Album> albums) {
    @StudentImplementationRequired
    public List<Song> getAllSongs() {
        return albums.stream()
                .flatMap(album -> album.songs().stream())
                .toList();
    }

    @StudentImplementationRequired
    public List<Genre> getGenres() {
        return albums.stream()
                .map(Album::genre)
                .distinct()
                .toList();
    }
}
