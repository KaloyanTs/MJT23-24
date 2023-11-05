package bg.sofia.uni.fmi.mjt.gym.member;

public record Address(double longitude, double latitude) {

    private double square(double x) {
        return x * x;
    }

    public double getDistanceTo(Address other) {
        return Math.sqrt(square((longitude - other.longitude)) + square(latitude - other.latitude));
    }

}
