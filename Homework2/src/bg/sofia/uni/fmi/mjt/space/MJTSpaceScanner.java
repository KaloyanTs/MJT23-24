package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.algorithm.Rijndael;
import bg.sofia.uni.fmi.mjt.space.algorithm.SymmetricBlockCipher;
import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;
import javax.crypto.SecretKey;

public class MJTSpaceScanner implements SpaceScannerAPI {

    private final List<Mission> missions;
    private final List<Rocket> rockets;
    SymmetricBlockCipher cipher;

    private static boolean isBetween(LocalDate from, LocalDate to, LocalDate date) {
        return date.isEqual(from) || date.isEqual(to) || (date.isAfter(from) && date.isBefore(to));
    }

    public MJTSpaceScanner(Reader missionsReader, Reader rocketsReader, SecretKey secretKey) {
        missions = new ArrayList<>();
        rockets = new ArrayList<>();
        cipher = new Rijndael(secretKey);
        String line;
        String[] parts;
        try (BufferedReader bufferedMissionsReader = new BufferedReader(missionsReader);
             BufferedReader bufferedRocketsReader = new BufferedReader(rocketsReader)) {
            bufferedMissionsReader.readLine();
            while ((line = bufferedMissionsReader.readLine()) != null) {
                parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                missions.add(Mission.of(parts));
            }
            bufferedRocketsReader.readLine();
            while ((line = bufferedRocketsReader.readLine()) != null) {
                parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                rockets.add(Rocket.of(parts));
            }
        } catch (IOException e) {
            throw new RuntimeException("Unexpected problem while reading data...", e);
        }
    }

    public Collection<Mission> getAllMissions() {
        return List.copyOf(missions);
    }

    @Override
    public Collection<Mission> getAllMissions(MissionStatus missionStatus) {
        if (missionStatus == null) {
            throw new IllegalArgumentException("Null given as argument...");
        }
        return missions.stream().filter((mission -> mission.missionStatus() == missionStatus)).toList();
    }

    @Override
    public String getCompanyWithMostSuccessfulMissions(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Null given as argument...");
        }
        if (from.isAfter(to)) {
            throw new TimeFrameMismatchException("Start date is after еnd date...");
        }
        Map<String, List<Mission>> grouped =
            missions.stream()
                .filter(
                    mission -> isBetween(from, to, mission.date())
                        && mission.missionStatus() == MissionStatus.SUCCESS)
                .collect(Collectors.groupingBy(Mission::company));

        if (grouped.isEmpty()) {
            return "";
        }

        return grouped.entrySet().stream()
            .max(Comparator.comparingInt(e -> e.getValue().size()))
            .get().getKey();
    }

    @Override
    public Map<String, Collection<Mission>> getMissionsPerCountry() {
        return missions.stream().collect(Collectors.groupingBy(Mission::getCountry)).entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            ));
    }

    @Override
    public List<Mission> getTopNLeastExpensiveMissions(int n, MissionStatus missionStatus, RocketStatus rocketStatus) {
        if (n <= 0 || missionStatus == null || rocketStatus == null) {
            throw new IllegalArgumentException("Bad arguments given...");
        }
        return missions.stream()
            .filter(m -> m.missionStatus() == missionStatus
                && m.rocketStatus() == rocketStatus
                && m.cost().isPresent())
            .sorted(Comparator.comparingDouble(m -> m.cost().get())).limit(n).toList();
    }

    private String mostDesiredLocation(List<Mission> l) {
        return l.stream()
            .collect(Collectors.groupingBy(Mission::location))
            .entrySet().stream()
            .max(Comparator.comparingInt(e -> e.getValue().size()))
            .get().getKey();
    }

    @Override
    public Map<String, String> getMostDesiredLocationForMissionsPerCompany() {
        return missions.stream().collect(Collectors.groupingBy(Mission::company))
            .entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> mostDesiredLocation(entry.getValue())
            ));
    }

    private String mostSuccessfulMissionsLocation(List<Mission> l) {
        Optional<Map.Entry<String, List<Mission>>> res = l.stream()
            .filter(mission -> mission.missionStatus() == MissionStatus.SUCCESS)
            .collect(Collectors.groupingBy(Mission::location))
            .entrySet().stream()
            .max(Comparator.comparingInt(e -> e.getValue().size()));
        return res.get().getKey();
    }

    @Override
    public Map<String, String> getLocationWithMostSuccessfulMissionsPerCompany(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Dates cannot be null...");
        }
        if (from.isAfter(to)) {
            throw new TimeFrameMismatchException("Start date is after end date...");
        }
        return missions.stream().collect(Collectors.groupingBy(Mission::company)).entrySet().stream()
            .filter(
                stringListEntry -> !stringListEntry
                    .getValue().stream()
                    .filter(mission -> mission.missionStatus() == MissionStatus.SUCCESS)
                    .toList().isEmpty()
            )
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> mostSuccessfulMissionsLocation(entry.getValue())
            ));
    }

    @Override
    public Collection<Rocket> getAllRockets() {
        return List.copyOf(rockets);
    }

    @Override
    public List<Rocket> getTopNTallestRockets(int n) {
        return rockets.stream()
            .filter(rocket -> rocket.height().isPresent())
            .sorted((r1, r2) -> {
                double cmp = r2.height().get() - r1.height().get();
                return (cmp < 0 ? -1 : (cmp > 0 ? 1 : 0));
            })
            .limit(n)
            .toList();
    }

    @Override
    public Map<String, Optional<String>> getWikiPageForRocket() {
        return rockets.stream()
            .collect(Collectors.toMap(Rocket::name, Rocket::wiki));
    }

    private Optional<String> getPage(String rocketName) {
        for (Rocket rocket : rockets) {
            if (rocket.name().equals(rocketName)) {
                return rocket.wiki();
            }
        }
        return Optional.empty();
    }

    @Override
    public List<String> getWikiPagesForRocketsUsedInMostExpensiveMissions(int n, MissionStatus missionStatus,
                                                                          RocketStatus rocketStatus) {
        if (n <= 0 || missionStatus == null || rocketStatus == null) {
            throw new IllegalArgumentException("Bad arguments given...");
        }
        return missions.stream()
            .filter(mission -> mission.cost().isPresent()
                && mission.missionStatus() == missionStatus
                && mission.rocketStatus() == rocketStatus)
            .sorted((m1, m2) -> {
                double cmp = m2.cost().get() - m1.cost().get();
                return (cmp < 0 ? -1 : (cmp > 0 ? 1 : 0));
            })
            .limit(n)
            .map(mission -> getPage(mission.detail().rocketName()))
            .filter(Optional::isPresent).map(Optional::get)
            .distinct().toList();
    }

    private double getReliability(Rocket rocket, LocalDate from, LocalDate to) {
        long allWithRocket =
            missions.stream().filter(
                    mission -> isBetween(from, to, mission.date()) &&
                        mission.detail().rocketName().equals(rocket.name()))
                .count();
        //todo
        if (allWithRocket == 0)
            return 0;

        long unsuccessful = missions.stream()
            .filter(mission -> isBetween(from, to, mission.date()) &&
                mission.detail().rocketName().equals(rocket.name()) &&
                mission.missionStatus() != MissionStatus.SUCCESS)
            .count();

        return 1 - (double) unsuccessful / 2 / allWithRocket;
    }

    private Optional<Rocket> mostReliable(LocalDate from, LocalDate to) {
        return rockets.stream().max((r1, r2) -> {
            double dif = getReliability(r2, from, to) - getReliability(r1, from, to);
            return dif > 0 ? 1 : (dif < 0 ? -1 : 0);
        });
    }

    @Override
    public void saveMostReliableRocket(OutputStream outputStream, LocalDate from, LocalDate to) throws CipherException {
        if (outputStream == null || from == null || to == null) {
            throw new IllegalArgumentException("Null given as argument...");
        }
        if (from.isAfter(to)) {
            throw new TimeFrameMismatchException("From date is after To date...");
        }
        Optional<Rocket> bestOptional = mostReliable(from, to);

        String bestRocketName = (bestOptional.isPresent() ? bestOptional.get().name() : "");
        cipher.encrypt(new ByteArrayInputStream(bestRocketName.getBytes()), outputStream);
    }
}
