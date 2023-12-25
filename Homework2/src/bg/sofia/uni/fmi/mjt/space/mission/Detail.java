package bg.sofia.uni.fmi.mjt.space.mission;

public record Detail(String rocketName, String payload) {

    public static Detail of(String together) {
        String[] separate = together.split(" \\| ");
        return new Detail(separate[0], separate[1]);
    }
}