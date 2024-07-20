package h11;

public record Song(String name, int durationInSeconds) {
    public boolean isLongerThan(int durationInSeconds) {
        return this.durationInSeconds > durationInSeconds;
    }
}
