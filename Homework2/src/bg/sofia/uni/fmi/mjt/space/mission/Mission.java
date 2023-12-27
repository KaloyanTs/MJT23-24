package bg.sofia.uni.fmi.mjt.space.mission;

import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

public record Mission(String id, String company, String location, LocalDate date, Detail detail,
                      RocketStatus rocketStatus,
                      Optional<Double> cost, MissionStatus missionStatus) {

    public Mission {
        if (id == null || company == null || location == null || date == null || detail == null ||
            rocketStatus == null || cost == null || missionStatus == null) {
            throw new IllegalArgumentException("Cannot create mission from null...");
        }
    }

    private static final int ID_INDEX = 0;
    private static final int COMPANY_INDEX = 1;
    private static final int LOCATION_INDEX = 2;
    private static final int DATE_INDEX = 3;
    private static final int DETAIL_INDEX = 4;
    private static final int ROCKET_STATUS_INDEX = 5;
    private static final int COST_INDEX = 6;
    private static final int MISSION_STATUS_INDEX = 7;

    public String getCountry() {
        String[] parts = location.split(", ");
        return parts[parts.length - 1];
    }

    public static Mission of(String[] parts) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("\"EEE MMM dd, yyyy\"");
        formatter = formatter.withLocale(Locale.ENGLISH);
        parts[COST_INDEX] = parts[COST_INDEX].replaceAll("(\")|( )|(,)", "");
        return new Mission(
            parts[ID_INDEX],
            parts[COMPANY_INDEX],
            parts[LOCATION_INDEX].replaceAll("\"", ""),
            LocalDate.parse(parts[DATE_INDEX], formatter),
            Detail.of(parts[DETAIL_INDEX]),
            RocketStatus.of(parts[ROCKET_STATUS_INDEX]),
            (parts[COST_INDEX].isBlank() ? Optional.empty() : Optional.of(Double.parseDouble(parts[COST_INDEX]))),
            MissionStatus.of(parts[MISSION_STATUS_INDEX]));
    }
}
