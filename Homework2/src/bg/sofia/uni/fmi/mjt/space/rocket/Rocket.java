package bg.sofia.uni.fmi.mjt.space.rocket;

import java.util.Optional;

public record Rocket(String id, String name, Optional<String> wiki, Optional<Double> height) {

    private static final int ID_INDEX = 0;
    private static final int NAME_INDEX = 1;
    private static final int WIKI_INDEX = 2;
    private static final int HEIGHT_INDEX = 3;

    public Rocket {
        if (id == null || name == null || wiki == null || height == null) {
            throw new IllegalArgumentException("Cannot create Rocket from null...");
        }
    }

    public static Rocket of(String[] parts) {
        return new Rocket(parts[ID_INDEX],
            parts[NAME_INDEX],
            (parts.length < WIKI_INDEX + 1 || parts[WIKI_INDEX].isBlank() ? Optional.empty() :
                Optional.of(parts[WIKI_INDEX])),
            (parts.length < HEIGHT_INDEX + 1 || parts[HEIGHT_INDEX].isBlank() ? Optional.empty() :
                Optional.of(Double.parseDouble(parts[HEIGHT_INDEX].replaceAll(" m", "")))));
    }
}
