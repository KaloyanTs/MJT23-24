package bg.sofia.uni.fmi.mjt.gym.workout;

import java.util.SequencedCollection;

public record Workout(SequencedCollection<Exercise> exercises) {
    public boolean isEmpty() {
        return exercises.isEmpty();
    }

    public boolean hasExercise(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        for (Exercise e : exercises) {
            if (e.name().equals(name)) {
                return true;
            }
        }
        return false;
    }
}