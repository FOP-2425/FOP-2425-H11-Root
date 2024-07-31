package h11;

import org.tudalgo.algoutils.student.annotation.StudentImplementationRequired;

public record Song(String title, int durationInSeconds) {
    @StudentImplementationRequired
    public boolean isLongerThan(int durationInSeconds) {
        return this.durationInSeconds > durationInSeconds;
    }
}
