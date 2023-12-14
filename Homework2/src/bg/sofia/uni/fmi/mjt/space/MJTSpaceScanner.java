package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionCostComparator;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketBiggestHeightComparator;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;
import javax.crypto.SecretKey;

public class MJTSpaceScanner implements SpaceScannerAPI {

    List<Mission> missions;

    List<Rocket> rockets;

    private boolean isBetween(LocalDate from, LocalDate to, LocalDate date) {
        return date.isEqual(from) || date.isEqual(to) || (date.isAfter(from) && date.isBefore(to));
    }

    public MJTSpaceScanner(Reader missionsReader, Reader rocketsReader, SecretKey secretKey) {
        BufferedReader reader = new BufferedReader(missionsReader);
        //todo read line by line
    }

    public Collection<Mission> getAllMissions() {
        return List.copyOf(missions);
    }

    @Override
    public Collection<Mission> getAllMissions(MissionStatus missionStatus) {
        return missions.stream().filter((mission -> mission.missionStatus() == missionStatus)).toList();
    }

    private int countStatus(List<Mission> l, MissionStatus status) {
        return Math.toIntExact(l.stream().filter(m -> m.missionStatus() == status).count());
    }

    @Override
    public String getCompanyWithMostSuccessfulMissions(LocalDate from, LocalDate to) {
        Map<String, List<Mission>> grouped =
            missions.stream().filter(mission -> isBetween(from, to, mission.date()))
                .collect(Collectors.groupingBy(Mission::company));

        return grouped.entrySet().stream().
            max((e1, e2) -> countStatus(e1.getValue(), MissionStatus.SUCCESS) -
                countStatus(e2.getValue(), MissionStatus.SUCCESS))
            .get().getKey();
    }

    @Override
    public Map<String, Collection<Mission>> getMissionsPerCountry() {
        return missions.stream().collect(Collectors.groupingBy(Mission::location)).entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            ));
    }

    @Override
    public List<Mission> getTopNLeastExpensiveMissions(int n, MissionStatus missionStatus, RocketStatus rocketStatus) {
        return missions.stream().filter(m -> m.missionStatus() == missionStatus && m.rocketStatus() == rocketStatus)
            .sorted(new MissionCostComparator(true)).limit(n).toList();
    }

    private String mostDesiredLocation(List<Mission> l) {
        return l.stream().collect(Collectors.groupingBy(Mission::location)).entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().size())).entrySet().stream()
            .max((e1, e2) -> e2.getValue() - e1.getValue()).get().getKey();
    }

    @Override
    public Map<String, String> getMostDesiredLocationForMissionsPerCompany() {
        return missions.stream().collect(Collectors.groupingBy(Mission::company)).entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> mostDesiredLocation(entry.getValue())
            ));
    }

    @Override
    public Map<String, String> getLocationWithMostSuccessfulMissionsPerCompany(LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("not implemented yet...");
    }

    @Override
    public Collection<Rocket> getAllRockets() {
        return List.copyOf(rockets);
    }

    @Override
    public List<Rocket> getTopNTallestRockets(int n) {
        return rockets.stream().sorted(new RocketBiggestHeightComparator()).limit(n).toList();
    }

    @Override
    public Map<String, Optional<String>> getWikiPageForRocket() {
        return rockets.stream().collect(Collectors.toMap(Rocket::name, Rocket::wiki));
    }

    @Override
    public List<String> getWikiPagesForRocketsUsedInMostExpensiveMissions(int n, MissionStatus missionStatus,
                                                                          RocketStatus rocketStatus) {
        if (n <= 0 || missionStatus == null || rocketStatus == null) {
            throw new IllegalArgumentException("Bad arguments given...");
        }

    }

    @Override
    public void saveMostReliableRocket(OutputStream outputStream, LocalDate from, LocalDate to) throws Exception {
        if (outputStream == null || from == null || to == null) {
            throw new IllegalAccessException("Null given as argument...");
        }
        rockets.stream().filter(rocket -> isBetween(from, to, rocket.))
    }
}
