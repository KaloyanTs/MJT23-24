package bg.sofia.uni.fmi.mjt.space.mission;

public record Detail(String rocketName, String payload) {

    public Detail {
        if (rocketName == null || payload == null) {
            throw new IllegalArgumentException("Cannot create Detail from null...");
        }
    }

    public static Detail of(String together) {
        String[] separate = together.split(" \\| ");
        return new Detail(separate[0], separate[1]);
    }
}