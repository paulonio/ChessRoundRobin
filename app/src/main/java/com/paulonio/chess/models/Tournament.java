package com.paulonio.chess.models;

import com.paulonio.chess.utils.RoundRobinUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class Tournament implements Serializable {

    @Setter
    private long dataBaseId;
    @NonNull
    private String name;
    @NonNull
    private Date creationDate;
    @NonNull
    private ListOrderingType orderingType;
    @NonNull
    private TournamentTieBreaks tieBreak;
    @NonNull
    private TournamentType tournamentType;
    @NonNull
    private int numberOfRoundRobins;

    private List<Player> players = new ArrayList<>();
    private List<Player> startList = new ArrayList<>();
    private List<Player> results;
    private List<List<Game>> pairings = new ArrayList<>();
    @Setter
    private int numberOfRoundsPlayed;

    public final static Player FAKE_BYE_PLAYER = Player.of("BYE", "BYE", 0);

    public void addPlayer(Player player) {
        players.add(player);
    }

    public int getNumberOfPlayers() {
        return players.size();
    }

    public void startTournament() {
        prepareStartList();
        preparePairings();
    }

    public List<Game> getPairingsForRound(int roundNumber) {
        return pairings.get(roundNumber - 1);
    }

    public void updateResults(int roundNumber, boolean startNextRound) {
        results = new ArrayList<>();
        if (startNextRound) {
            ++numberOfRoundsPlayed;

            for (Game game : pairings.get(roundNumber - 1)) {
                if (!game.getWhitePlayer().equals(FAKE_BYE_PLAYER) && !game.getBlackPlayer().equals(FAKE_BYE_PLAYER)) {
                    game.getBlackPlayer().addPlayedGame(game);
                    game.getWhitePlayer().addPlayedGame(game);
                }
            }
        }
        for (Player player : startList) {
            player.updateBerger();
            results.add(player);
        }

        if (tieBreak == TournamentTieBreaks.BERGER) {
            Collections.sort(results, new Player.ResultsBergerComparator());
        }
        if (tieBreak == TournamentTieBreaks.DIRECT_GAME) {
            Collections.sort(results, new Player.ResultDirectScoreComparator());
        }

    }

    private void prepareStartList() {
        Comparator<Player> playerComparator = null;
        startList = new ArrayList<>(players);
        switch (orderingType) {
            case RATING:
                playerComparator = new Player.RatingComparator();
                break;
            case ALPHABETICAL:
                playerComparator = new Player.AlphabeticalComparator();
                break;
            case RANDOM:
            default:
                Collections.shuffle(startList);
        }
        if (playerComparator != null) {
            Collections.sort(startList, playerComparator);
        }
        actuatePlayersStartListNumbers();
    }

    private void actuatePlayersStartListNumbers() {
        for (int i = 0; i < startList.size(); ++i) {
            startList.get(i).setStartListNumber(i + 1);
        }
    }

    private void preparePairings() {

        startList.add(FAKE_BYE_PLAYER);
        pairings = new ArrayList<>();
        int[][][] tmpPairings = RoundRobinUtils.getRoundRobinPairings(getNumberOfPlayers());
        for (int i = 0; i < numberOfRoundRobins; ++i) {
            for (int[][] round : tmpPairings) {
                List<Game> roundPairings = new ArrayList<>();
                for (int[] table : round) {
                    Game game = Game.of(startList.get(table[i % 2]), startList.get(table[Math.abs((i % 2) - 1)]));
                    roundPairings.add(game);
                }
                pairings.add(roundPairings);
            }
        }

        startList.remove(startList.size() - 1);
    }

}
