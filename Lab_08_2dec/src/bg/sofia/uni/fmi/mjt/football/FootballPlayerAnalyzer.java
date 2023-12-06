package bg.sofia.uni.fmi.mjt.football;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class FootballPlayerAnalyzer {

    private static final int BUFFER_SIZE = 4096;
    private static final int MAX_POTENTIAL_DIFFERENCE_FOR_SIMILARITY = 3;

    List<Player> players;

    /**
     * Loads the dataset from the given {@code reader}. The reader argument will not be null and a correct dataset of
     * the specified type can be read from it.
     *
     * @param reader Reader from which the dataset can be read.
     */
    public FootballPlayerAnalyzer(Reader reader) {
        StringBuilder str = new StringBuilder();
        char[] readBuf = new char[BUFFER_SIZE];
        int read;
        try {
            read = reader.read(readBuf);
            while (read > 0) {
                readBuf[read] = '\0';
                str.append(String.valueOf(readBuf).strip());
                read = reader.read(readBuf);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Problems on reading...", e);
        }
        players = Arrays.stream(str.toString().split("\n")).skip(1).map(Player::of).toList();
        //players.forEach(player -> System.out.println(player));
    }

    /**
     * Returns all players from the dataset in undefined order as an unmodifiable List. If the dataset is empty, returns
     * an empty List.
     *
     * @return the list of all players.
     */
    public List<Player> getAllPlayers() {
        return List.copyOf(players);
    }

    /**
     * Returns an unmodifiable set of all nationalities in the dataset. If the dataset is empty, returns an empty Set.
     *
     * @return the set of all nationalities
     */
    public Set<String> getAllNationalities() {
        return Set.copyOf(players.stream().map((Player::nationality)).collect(Collectors.toSet()));
    }

    /**
     * Returns the highest paid player from the provided nationality. If there are two or more players with equal
     * maximum wage, returns any of them.
     *
     * @param nationality the nationality of the player to return
     * @return the highest paid player
     * @throws IllegalArgumentException in case the provided nationality is null
     * @throws NoSuchElementException   in case there is no player with the provided nationality
     */
    public Player getHighestPaidPlayerByNationality(String nationality) {
        if (nationality == null) {
            throw new IllegalArgumentException("Nationality is null...");
        }
        Optional<Player> bestPlayer =
            players.stream().filter(player -> player.nationality().equals(nationality)).max((p1, p2) -> Math.toIntExact(
                p1.wageEuro() - p2.wageEuro()));
        if (bestPlayer.isEmpty()) {
            throw new NoSuchElementException("No players from given nationality...");
        }
        return bestPlayer.get();
    }

    /**
     * Returns a breakdown of players by position. Note that some players can play in more than one position so they
     * should be present in more than one value Set. If no player plays in a given Position then that position should
     * not be present as a key in the map.
     *
     * @return a Map with key: a Position and value: the set of players in the dataset that can play in that Position,
     * in undefined order.
     */
    public Map<Position, Set<Player>> groupByPosition() {
        Map<Position, Set<Player>> res = new EnumMap<>(Position.class);
        for (Position position : Position.values()) {
            if (players.stream().anyMatch(player -> player.positions().contains(position))) {
                res.put(position,
                    players.stream().filter(player -> player.positions().contains(position))
                        .collect(Collectors.toSet()));
            }
        }
        return res;
    }

    /**
     * Returns an Optional containing the top prospect player in the dataset that can play in the provided position and
     * that can be bought with the provided budget considering the player's value_euro. If no player can be bought with
     * the provided budget then return an empty Optional.
     * <p>
     * The player's prospect is calculated by the following formula: Prospect = (r + p) ÷ a where r is the player's
     * overall rating, p is the player's potential and a is the player's age
     *
     * @param position the position in which the player should be able to play
     * @param budget   the available budget for buying a player
     * @return an Optional containing the top prospect player
     * @throws IllegalArgumentException in case the provided position is null or the provided budget is negative
     */
    public Optional<Player> getTopProspectPlayerForPositionInBudget(Position position, long budget) {
        if (position == null || budget < 0) {
            throw new IllegalArgumentException("Bad arguments given...");
        }
        return players.stream().filter(player -> player.positions().contains(position) && player.valueEuro() <= budget)
            .max((p1, p2) -> -p2.potential() + p1.potential());
    }

    /**
     * Returns an unmodifiable set of players that are similar to the provided player. Two players are considered
     * similar if: 1. there is at least one position in which both of them can play 2. both players prefer the same foot
     * 3. their overall_rating measures differ by at most 3 (inclusive)
     * If the dataset contains the provided player, the player will be present in the returned result.
     *
     * @param player the player for whom similar players are retrieved. It may or may not be part of the dataset.
     * @return an unmodifiable set of similar players
     * @throws IllegalArgumentException if the provided player is null
     */
    public Set<Player> getSimilarPlayers(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player was null...");
        }
        return players.stream().filter(p -> p.preferredFoot() == player.preferredFoot() &&
                !Collections.disjoint(player.positions(), p.positions()) &&
                Math.abs(p.overallRating() - player.overallRating()) <= MAX_POTENTIAL_DIFFERENCE_FOR_SIMILARITY)
            .collect(Collectors.toUnmodifiableSet());
    }

    /**
     * Returns an unmodifiable set of players whose full name contains the provided keyword (case-sensitive search)
     *
     * @param keyword the keyword that should be contained in player's full name
     * @return an unmodifiable set of players
     * @throws IllegalArgumentException if the provided keyword is null
     */
    public Set<Player> getPlayersByFullNameKeyword(String keyword) {
        if (keyword == null) {
            throw new IllegalArgumentException("Keyword is null...");
        }
        return players.stream().filter(player -> player.fullName().contains(keyword))
            .collect(Collectors.toUnmodifiableSet());
    }

}