package bg.sofia.uni.fmi.mjt.space.mission;

public enum MissionStatus {
    SUCCESS("Success"),
    FAILURE("Failure"),
    PARTIAL_FAILURE("Partial Failure"),
    PRELAUNCH_FAILURE("Prelaunch Failure");

    private final String value;

    MissionStatus(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public static MissionStatus of(String str) {
        return switch (str) {
            case "Success" -> SUCCESS;
            case "Failure" -> FAILURE;
            case "Partial Failure" -> PARTIAL_FAILURE;
            case "Prelaunch Failure" -> PRELAUNCH_FAILURE;
            default -> throw new IllegalArgumentException("MissionStatus not recognized...");
        };
    }
}