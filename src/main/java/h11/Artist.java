package h11;

import java.util.List;

public record Artist(String name, List<Album> albums) {
    public List<Song> getAllSongs() {
        return albums.stream()
                .flatMap(album -> album.songs().stream())
                .toList();
    }

    public List<Genre> getGenres() {
        return albums.stream()
                .map(Album::genre)
                .distinct()
                .toList();
    }
}
