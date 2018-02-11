package com.paulonio.chess.models;


import java.util.NoSuchElementException;

public enum Score {
    WHITE_WIN(0, 1.0),
    BLACK_WIN(1, 0.0),
    DRAW(2, 0.5);

    private int orderInSpinner;
    private double scorePoints;

    Score(int orderInSpinner, double scorePoints) {
        this.orderInSpinner = orderInSpinner;
        this.scorePoints = scorePoints;
    }

    public double getScorePoints(boolean ifForBlack) {
        return ifForBlack ? Math.abs(scorePoints - 1.0) : scorePoints;
    }

    public int getOrderInSpinner() {
        return orderInSpinner;
    }

    public static Score getValueFromSpinnerIndex(int index) {
        for (Score score : Score.values()) {
            if (score.getOrderInSpinner() == index) {
                return score;
            }
        }
        throw new NoSuchElementException("Element with given index do not exists in ListOrderingType enum");
    }
}
