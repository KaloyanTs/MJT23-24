package bg.sofia.uni.fmi.mjt.football;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public record Player(String name, String fullName, LocalDate birthDate, int age, double heightCm, double weightKg,
                     List<Position> positions, String nationality, int overallRating, int potential, long valueEuro,
                     long wageEuro, Foot preferredFoot) {

    private static final int NAME = 0;
    private static final int FULL_NAME = 1;
    private static final int BIRTH_DATE = 2;
    private static final int AGE = 3;
    private static final int HEIGHT = 4;
    private static final int WEIGHT = 5;
    private static final int POSITIONS = 6;
    private static final int NATIONALITY = 7;
    private static final int RATING = 8;
    private static final int POTENTIAL = 9;
    private static final int VALUE = 10;
    private static final int WAGE = 11;
    private static final int PREF_FOOT = 12;

    public static Player of(String line) {
        String[] components = line.split(";");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        return new Player(components[NAME],
            components[FULL_NAME],
            LocalDate.parse(components[BIRTH_DATE], formatter),
            Integer.parseInt(components[AGE]),
            Double.parseDouble(components[HEIGHT]),
            Double.parseDouble(components[WEIGHT]),
            Arrays.stream(components[POSITIONS].split(",")).map(Position::fromString).toList(),
            components[NATIONALITY],
            Integer.parseInt(components[RATING]),
            Integer.parseInt(components[POTENTIAL]),
            Long.parseLong(components[VALUE]),
            Long.parseLong(components[WAGE]),
            Foot.fromString(components[PREF_FOOT].trim()));
    }
}
