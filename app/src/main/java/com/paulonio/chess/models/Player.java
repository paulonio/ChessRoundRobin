package com.paulonio.chess.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode(exclude = {"playedGames", "score", "bergerScore", "startListNumber"})
@Getter
@RequiredArgsConstructor(staticName = "of")
public class Player implements Serializable {

    @Setter
    private long dataBaseId;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private int rating;
    @Setter
    private double score = 0;
    @Setter
    private double bergerScore;
    private List<Game> playedGames = new ArrayList<>();
    @Setter
    private int startListNumber;

    public void addPlayedGame(Game game) {
        playedGames.add(game);
        score += game.getGameScore().getScorePoints(game.getBlackPlayer() == this);
    }

    public void updateBerger() {
        bergerScore = 0;
        for (Game game : playedGames) {
            boolean ifIsWhitePlayer = game.getWhitePlayer() == this;
            if (ifIsWhitePlayer) {
                bergerScore += game.getGameScore().getScorePoints(false) * game.getBlackPlayer().getScore();
            } else {
                bergerScore += game.getGameScore().getScorePoints(true) * game.getWhitePlayer().getScore();
            }
        }
    }

    public int compareDirectScore(Player p2) {
        double sumOfP1ScoresWithP2 = 0.0;
        double sumOfP2ScoresWithP1 = 0.0;
        for (Game game : playedGames) {
            if (game.getWhitePlayer().equals(p2)) {
                sumOfP1ScoresWithP2 += game.getGameScore().getScorePoints(true);
                sumOfP2ScoresWithP1 += game.getGameScore().getScorePoints(false);
            }
            if (game.getBlackPlayer().equals(p2)) {
                sumOfP1ScoresWithP2 += game.getGameScore().getScorePoints(false);
                sumOfP2ScoresWithP1 += game.getGameScore().getScorePoints(true);
            }
        }
        return Double.compare(sumOfP1ScoresWithP2, sumOfP2ScoresWithP1);
    }

    public static class AlphabeticalComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            int surnameComparisionResult = p1.getSurname().compareToIgnoreCase(p2.getSurname());
            return (surnameComparisionResult != 0) ? surnameComparisionResult : p1.getName().compareToIgnoreCase(p2.getName());
        }
    }

    public static class RatingComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            return p1.getRating() > p2.getRating() ? -1 : 1;
        }
    }

    public static class ResultsBergerComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            int scoreComparision = Double.compare(p2.getScore(), p1.getScore());
            int bergerComparision = scoreComparision == 0 ? Double.compare(p2.getBergerScore(), p1.getBergerScore()) : scoreComparision;
            return bergerComparision == 0 ? p1.compareDirectScore(p2) : bergerComparision;
        }
    }

    public static class ResultDirectScoreComparator implements Comparator<Player> {
        @Override
        public int compare(Player p1, Player p2) {
            int scoreComparision = Double.compare(p2.getScore(), p1.getScore());
            int directScoreComparision = scoreComparision == 0 ? p1.compareDirectScore(p2) : scoreComparision;
            return directScoreComparision == 0 ? Double.compare(p2.getBergerScore(), p1.getBergerScore()) : directScoreComparision;
        }
    }

}
