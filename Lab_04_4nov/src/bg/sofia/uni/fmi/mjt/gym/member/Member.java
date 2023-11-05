package bg.sofia.uni.fmi.mjt.gym.member;

import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.List;
import java.util.EnumMap;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Map;

public class Member implements GymMember, Comparable<Member> {

    private final String personalIdNumber;
    private Address address;
    private String name;
    private int age;
    private Gender gender;
    private EnumMap<DayOfWeek, Workout> plan;

    public Member(Address address, String name, int age, String personalIdNumber, Gender gender) {
        this.personalIdNumber = personalIdNumber;
        this.address = address;
        this.name = name;
        this.age = age;
        this.gender = gender;
        plan = new EnumMap<>(DayOfWeek.class);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public String getPersonalIdNumber() {
        return personalIdNumber;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public Map<DayOfWeek, Workout> getTrainingProgram() {
        return new EnumMap<>(plan);
    }

    @Override
    public void setWorkout(DayOfWeek day, Workout workout) {
        if (day == null || workout == null) {
            throw new IllegalArgumentException("Given null day or null workout...");
        }
        plan.put(day, workout);
    }

    @Override
    public Collection<DayOfWeek> getDaysFinishingWith(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("ExerciseName is null...");
        }
        Collection<DayOfWeek> res = new ArrayList<>();
        for (DayOfWeek d : plan.keySet()) {
            if (plan.get(d).exercises().getLast().name().equals(exerciseName)) {
                res.add(d);
            }
        }
        return res;
    }

    @Override
    public void addExercise(DayOfWeek day, Exercise exercise) {
        if (day == null || exercise == null) {
            throw new IllegalArgumentException("Day or exercise is null...");
        }
        if (plan.get(day) == null) {
            throw new DayOffException();
        }
        plan.get(day).exercises().add(exercise);
    }

    @Override
    public void addExercises(DayOfWeek day, List<Exercise> exercises) {
        if (day == null || exercises == null || exercises.isEmpty()) {
            throw new IllegalArgumentException("Day or exercise is null...");
        }
        if (plan.get(day) == null) {
            throw new DayOffException();
        }
//        if (plan.get(day) == null || plan.get(day).isEmpty()) {
//            throw new DayOffException();
//        }
        plan.get(day).exercises().addAll(exercises);
    }

    @Override
    public int hashCode() {
        return personalIdNumber.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other.getClass() == this.getClass() && hashCode() == other.hashCode();
    }

    @Override
    public int compareTo(Member other) {
        return name.compareTo(other.name);
    }
}
