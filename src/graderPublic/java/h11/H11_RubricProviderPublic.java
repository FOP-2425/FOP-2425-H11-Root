package h11;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.sourcegrade.jagr.api.rubric.*;

import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.criterion;
import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.graderPrivateOnly;

public class H11_RubricProviderPublic implements RubricProvider {

    private static final Criterion H11_1_1 = Criterion.builder()
        .shortDescription("H11.1.1 | Lieder länger als ...")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            criterion(
                "Die Methode isLongerThan(int durationInSeconds) von Song gibt true zurück, wenn die Dauer des Songs länger als durationInSeconds ist, sonst false.",
                JUnitTestRef.ofMethod(() -> SongTest.class.getDeclaredMethod("testIsLongerThan", ObjectNode.class))
            ),
            criterion(
                "Die Methode getSongsLongerThan(int durationInSeconds) von Album gibt alle Songs des Albums zurück, die länger als durationInSeconds sind.",
                JUnitTestRef.ofMethod(() -> AlbumTest.class.getDeclaredMethod("testGetSongsLongerThan", ObjectNode.class))
            )
        )
        .build();

    
    private static final Criterion H11_1_2 = Criterion.builder()
        .shortDescription("H11.1.2 | Durchschnittliche Spieldauer eines Albums")
        .maxPoints(2)
        .minPoints(0)
        .addChildCriteria(
            criterion(
                "Die Methode getAverageDuration() von Album gibt die durchschnittliche Dauer aller Songs des Albums zurück.",
                JUnitTestRef.ofMethod(() -> AlbumTest.class.getDeclaredMethod("testGetAverageDuration_full", ObjectNode.class))
            ),
            criterion(
                "Die Methode getAverageDuration() von Album gibt für ein Album ohne Lieder 0 zurück.",
                JUnitTestRef.ofMethod(() -> AlbumTest.class.getDeclaredMethod("testGetAverageDuration_empty", ObjectNode.class))
            )
        )
        .build();

    private static final Criterion H11_1 = Criterion.builder()
        .shortDescription("H11.1 | Alben")
        .addChildCriteria(
            H11_1_1,
            H11_1_2
        )
        .build();

    
    private static final Criterion H11_2_1 = Criterion.builder()
        .shortDescription("H11.2.1 |  Lieder eines Künstlers")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            criterion(
                "Die Methode getAllSongs() von Artist gibt eine Liste aller Songs des Künstlers zurück.",
                JUnitTestRef.ofMethod(() -> ArtistTest.class.getDeclaredMethod("testGetAllSongs", ObjectNode.class))
            )
        )
        .build();

    
    private static final Criterion H11_2_2 = Criterion.builder()
        .shortDescription("H11.2.2 | Alle Genres eines Künstlers")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            criterion(
                "Die Methode getAllGenres() von Artist gibt eine Liste aller Genres der Alben des Künstlers zurück.",
                JUnitTestRef.ofMethod(() -> ArtistTest.class.getDeclaredMethod("testGetAllGenres_general", ObjectNode.class))
            ),
            criterion(
                "Jedes Genre kommt nur einmal in der Liste vor.",
                JUnitTestRef.ofMethod(() -> ArtistTest.class.getDeclaredMethod("testGetAllGenres_unique", ObjectNode.class))
            )
        )
        .build();

    private static final Criterion H11_2 = Criterion.builder()
        .shortDescription("H11.2 | Künstler")
        .addChildCriteria(
            H11_2_1,
            H11_2_2
        )
        .build();

    
    public static final Criterion H11_3_1 = Criterion.builder()
        .shortDescription("H11.3.1 | Abgespielte Lieder")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            criterion(
                "Die Methode getPlayedSongs() von User gibt eine Liste aller abgespielten Lieder des Benutzers zurück.",
                JUnitTestRef.ofMethod(() -> UserTest.class.getDeclaredMethod("testGetPlayedSongs_general", ObjectNode.class))
            ),
            criterion(
                "Jedes Lied kommt nur einmal in der Liste vor.",
                JUnitTestRef.ofMethod(() -> UserTest.class.getDeclaredMethod("testGetPlayedSongs_unique", ObjectNode.class))
            )
        )
        .build();

    public static final Criterion H11_3_2 = Criterion.builder()
        .shortDescription("H11.3.2 | Wie oft wurde ein Lied abgespielt?")
        .maxPoints(3)
        .minPoints(0)
        .addChildCriteria(
            criterion( 
                "Die Methode getPlayCounts() von User gibt eine Liste aller abgespielten Lieder des Benutzers zurück, zusammen mit der Anzahl der Wiedergaben.",
                2,
                JUnitTestRef.ofMethod(() -> UserTest.class.getDeclaredMethod("testGetPlayCounts_return", ObjectNode.class))
            ),
            privateCriterion(
                "Die Liste ist absteigend nach der Anzahl der Wiedergaben sortiert. Bei gleicher Anzahl von Wiedergaben ist die Reihenfolge alphabetisch nach dem Titel des Liedes sortiert."
            )
        )
        .build();

    public static final Criterion H11_3_3 = Criterion.builder()
        .shortDescription("H11.3.3 | Was ist das Lieblingslied?")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            privateCriterion(
                "Die Methode getFavoriteSong() von User gibt das am häufigsten abgespielte Lied des Benutzers zurück."
            ),
            privateCriterion(
                "Hat der Benutzer noch kein Lied abgespielt, wird null zurückgegeben."
            )
        )
        .build();

    public static final Criterion H11_3_4 = Criterion.builder()
        .shortDescription("H11.3.4 | Ausgabe der meistgespielten Lieder")
        .maxPoints(2)
        .minPoints(0)
        .addChildCriteria(
            privateCriterion(
                "Die Methode getTopPlayedSongsList() von User gibt eine Liste von maximal drei Liedern zurück."
            ),
            privateCriterion(
                "Das Format der Ausgabe ist \"<Titel> (<Anzahl> plays)\" pro Song."
            )
        )
        .build();

    public static final Criterion H11_3 = Criterion.builder()
        .shortDescription("H11.3 | Analysieren von Nutzern")
        .addChildCriteria(
            H11_3_1,
            H11_3_2,
            H11_3_3,
            H11_3_4
        )
        .build();

    public static final Criterion H11_4_1 = Criterion.builder()
        .shortDescription("H11.4.1 | Alle Lieder")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            privateCriterion(
                "Die Methode getAllSongs() von MusicStreaming gibt eine Liste aller Lieder zurück."
            ),
            privateCriterion(
                "Jedes Lied kommt nur einmal in der Liste vor."
            )
        )
        .build();

    
    public static final Criterion H11_4_2 = Criterion.builder()
        .shortDescription("H11.4.2 | Eine neue zufällige Playlist")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            criterion(
                "Die Methode generateRandomPlaylist() von MusicStreaming erstellt einen unendlichen Stream von zufälligen Liedern.",
                JUnitTestRef.ofMethod(() -> MusicStreamingTest.class.getDeclaredMethod("testGenerateRandomPlaylist", ObjectNode.class))
            )
        )
        .build();

    public static final Criterion H11_4_3 = Criterion.builder()
        .shortDescription("H11.4.3 | Nur Lieder bestimmter Länge")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            privateCriterion(
                "Die Methode getSongsLongerThan(int durationInSeconds) von MusicStreaming gibt nur Lieder zurück, die länger als durationInSeconds sind."
            )
        )
        .build();

    public static final Criterion H11_4_4 = Criterion.builder()
        .shortDescription("H11.4.4 | Alle Genres")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            privateCriterion(
                "Die Methode getAllGenres() von MusicStreaming gibt eine Liste von in Alben verwendeten Genres zurück."
            ),
            privateCriterion(
                "Jedes Genre kommt nur einmal in der Liste vor."
            )
        )
        .build();

    public static final Criterion H11_4_5 = Criterion.builder()
        .shortDescription("H11.4.5 | Nur Alben eines Genres")
        .maxPoints(2)
        .minPoints(0)
        .addChildCriteria(
            privateCriterion(
                "Die Methode getAlbumsByGenre() von MusicStreaming gibt eine Map zurück, die jedes Genre auf eine Liste von Alben dieses Genres abbildet.",
                2
            )
        )
        .build();

    public static final Criterion H11_4_6 = Criterion.builder()
        .shortDescription("H11.4.6 | Meistgespielte Lieder")
        .maxPoints(4)
        .minPoints(0)
        .addChildCriteria(
            criterion(
                "Die Methode getGlobalPlayCounts() von MusicStreaming gibt eine Liste aller Lieder und ihrer Gesamtanzahl von Wiedergaben zurück.",
                JUnitTestRef.ofMethod(() -> MusicStreamingTest.class.getDeclaredMethod("testGetGlobalPlayCounts_general", ObjectNode.class)),
                2
            ),
            privateCriterion(
                "Die Liste ist absteigend nach der Anzahl der Wiedergaben sortiert. Bei gleicher Anzahl von Wiedergaben ist die Reihenfolge alphabetisch nach dem Titel des Liedes sortiert."
            ),
            privateCriterion(
                "Die Methode getTopPlayedSongsList() von MusicStreaming gibt eine Liste von maximal fünf Liedern zurück. Das Format der Ausgabe ist \"<Titel> (<Anzahl> plays)\" pro Song."
            )
        )
        .build();

    public static final Criterion H11_4_7 = Criterion.builder()
        .shortDescription("H11.4.7 | Spielzeit eines Künstlers")
        .maxPoints(4)
        .minPoints(0)
        .addChildCriteria(
            privateCriterion( 
                "Die Methode getArtistPlayTime(Artist artist) von MusicStreaming gibt die Gesamtspielzeit in Sekunden aller Lieder des Künstlers zurück.",
                3
            ),
            privateCriterion(
                "Die Methode getArtistPlayTimes() von MusicStreaming gibt eine Map zurück, die jeden Künstler auf die Gesamtspielzeit in Sekunden seiner Lieder abbildet."
            )
        )
        .build();

    public static final Criterion H11_4_8 = Criterion.builder()
        .shortDescription("H11.4.8 | Welcher Künstler wurde am meisten gehört?")
        .maxPoints(2)
        .minPoints(0)
        .addChildCriteria(
            criterion( 
                "Die Methode getMostPlayedArtist() von MusicStreaming gibt den Künstler zurück, dessen Gesamtspielzeit am größten ist.",
                JUnitTestRef.ofMethod(() -> MusicStreamingTest.class.getDeclaredMethod("testGetMostPlayedArtist_full", ObjectNode.class))
            ),
            criterion(
                "Hat noch kein Künstler Lieder, wird null zurückgegeben.",
                JUnitTestRef.ofMethod(() -> MusicStreamingTest.class.getDeclaredMethod("testGetMostPlayedArtist_empty", ObjectNode.class))
            )
        )
        .build();

    
    public static final Criterion H11_4_9 = Criterion.builder()
        .shortDescription("H11.4.9 | Suchen von Liedern")
        .maxPoints(2)
        .minPoints(0)
        .addChildCriteria(
            criterion(
                "Die Methode searchSongs(Predicate<? super Song> predicate) von MusicStreaming gibt eine Liste von Liedern zurück, die das gegebene Predicate erfüllen.",
                JUnitTestRef.ofMethod(() -> MusicStreamingTest.class.getDeclaredMethod("testSearchSongs", ObjectNode.class)),
                2
            )
        )
        .build();

    public static final Criterion H11_4_10 = Criterion.builder()
        .shortDescription("H11.4.10 | Preiserhöhung")
        .maxPoints(2)
        .minPoints(0)
        .addChildCriteria(
            criterion( 
                "Die Methode adjustPrice(double percentage) von MusicStreaming erhöht oder senkt den Preis für alle Benutzer bei einem postiven bzw. negativen Prozentsatz.",
                JUnitTestRef.ofMethod(() -> MusicStreamingTest.class.getDeclaredMethod("testAdjustPrice_general", ObjectNode.class))
            ),
            privateCriterion(
                "Die Methode adjustPrice(double percentage) von MusicStreaming erhält den Preis für alle Benutzer bei einem Prozentsatz von 0."
            )
        )
        .build();

    public static final Criterion H11_4 = Criterion.builder()
        .shortDescription("H11.4 | Musikstreaming")
        .addChildCriteria(
            H11_4_1,
            H11_4_2,
            H11_4_3,
            H11_4_4,
            H11_4_5,
            H11_4_6,
            H11_4_7,
            H11_4_8,
            H11_4_9,
            H11_4_10
        )
        .build();

    public static final Rubric RUBRIC = Rubric.builder()
        .title("H11 | Musikstreaming")
        .addChildCriteria(
            H11_1,
            H11_2,
            H11_3,
            H11_4
        )
        .build();

    public static Criterion privateCriterion(String message) {
        return privateCriterion(message, 0, 1);
    }

    public static Criterion privateCriterion(String message, int max) {
        return privateCriterion(message, 0, max);
    }

    public static Criterion privateCriterion(String message, int min, int max) {
        return Criterion.builder()
            .shortDescription(message)
            .grader(graderPrivateOnly(max))
            .minPoints(min)
            .maxPoints(max)
            .build();
    }

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}
