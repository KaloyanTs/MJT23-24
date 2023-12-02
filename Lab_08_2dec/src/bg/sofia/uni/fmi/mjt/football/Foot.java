package bg.sofia.uni.fmi.mjt.football;

public enum Foot {
    LEFT, RIGHT;

    public static Foot fromString(String str) {
        return switch (str) {
            case "Left" -> LEFT;
            case "Right" -> RIGHT;
            default -> throw new IllegalArgumentException("String " + str + " cannot be converted to Foot...");
        };
    }
}
