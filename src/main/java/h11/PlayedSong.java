package h11;

import java.util.Date;

/**
 * Represents a song that was played at a specific time.
 *
 * @param song     the song that was played
 * @param playedAt the date and time when the song was played
 */
public record PlayedSong(Song song, Date playedAt) {
}
