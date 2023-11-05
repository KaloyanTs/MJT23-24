package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;
import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Comparator;
import java.util.List;
import java.util.EnumMap;
import java.util.Map;

public class Gym implements GymAPI {

    private final Set<GymMember> members;
    private final int capacity;
    private final Address address;

    public Gym(int capacity, Address address) {
        this.capacity = capacity;
        this.address = address;
        members = new HashSet<>();
    }

    @Override
    public SortedSet<GymMember> getMembers() {
        return new TreeSet<>(members);
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByProximityToGym() {
        SortedSet<GymMember> res = new TreeSet<>(new Comparator<>() {
            @Override
            public int compare(GymMember a, GymMember b) {
                return (a.getAddress().getDistanceTo(address) < b.getAddress().getDistanceTo(address) ? -1 : 1);
            }
        }
        );
        res.addAll(members);
        return res;
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByName() {
        SortedSet<GymMember> res = new TreeSet<>(new Comparator<>() {
            @Override
            public int compare(GymMember a, GymMember b) {
                return a.getName().compareTo(b.getName());
            }
        }
        );
        res.addAll(members);
        return res;
    }

    public boolean isFull() {
        return members.size() == capacity;
    }

    @Override
    public void addMember(GymMember member) throws GymCapacityExceededException {
        if (member == null) {
            throw new IllegalArgumentException("Given member is null...");
        }

        if (isFull()) {
            throw new GymCapacityExceededException("Gym is full already...");
        }

        members.add(member);
    }

    @Override
    public void addMembers(Collection<GymMember> members) throws GymCapacityExceededException {
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("Given members are null or empty...");
        }

        if (capacity < members.size() + this.members.size()) {
            throw new GymCapacityExceededException("Not enough capacity for all the members...");
        }

        this.members.addAll(members);
    }

    @Override
    public boolean isMember(GymMember member) {
        if (member == null) {
            throw new IllegalArgumentException();
        }
        return members.contains(member);
    }

    @Override
    public boolean isExerciseTrainedOnDay(String exerciseName, DayOfWeek day) {
        if (day == null || exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException();
        }
        for (GymMember m : members) {
            if (m.getTrainingProgram().get(day) != null && m.getTrainingProgram().get(day).hasExercise(exerciseName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<DayOfWeek, List<String>> getDailyListOfMembersForExercise(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException();
        }

        Map<DayOfWeek, List<String>> res = new EnumMap<>(DayOfWeek.class);

        for (DayOfWeek d : DayOfWeek.values()) {
            if (isExerciseTrainedOnDay(exerciseName, d)) {
                res.put(d, new ArrayList<>());
            }
            for (GymMember m : members) {
                if (m.getTrainingProgram().get(d) == null) continue;
                for (Exercise e : m.getTrainingProgram().get(d).exercises()) {
                    if (e.name().equals(exerciseName)) {
                        res.get(d).add(m.getName());
                        break;
                    }
                }
            }
        }
        return res;
    }
}
