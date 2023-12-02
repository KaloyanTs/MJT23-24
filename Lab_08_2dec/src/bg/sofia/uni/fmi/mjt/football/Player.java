package bg.sofia.uni.fmi.mjt.football;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public record Player(String name, String fullName, LocalDate birthDate, int age, double heightCm, double weightKg,
                     List<Position> positions, String nationality, int overallRating, int potential, long valueEuro,
                     long wageEuro, Foot preferredFoot) {


    public static Player of(String line) {
        String[] components = line.split(";");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        return new Player(components[0],
            components[1],
            LocalDate.parse(components[2], formatter),
            Integer.parseInt(components[3]),
            Double.parseDouble(components[4]),
            Double.parseDouble(components[5]),
            Arrays.stream(components[6].split(",")).map(pos -> Position.fromString(pos)).toList(),
            components[7],
            Integer.parseInt(components[8]),
            Integer.parseInt(components[9]),
            Long.parseLong(components[10]),
            Long.parseLong(components[11]),
            Foot.fromString(components[12]));
    }
}
