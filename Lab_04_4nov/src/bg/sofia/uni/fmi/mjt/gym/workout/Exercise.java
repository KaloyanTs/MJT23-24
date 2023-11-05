package bg.sofia.uni.fmi.mjt.gym.workout;

public record Exercise(String name, int sets, int repetitions) {

    public Exercise {
        if (sets <= 0 || repetitions <= 0) {
            throw new IllegalArgumentException("Sets and repetitions must be positive count...");
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return this.getClass().getTypeName() == other.getClass().getTypeName() &&
            hashCode() == other.hashCode();
    }
}