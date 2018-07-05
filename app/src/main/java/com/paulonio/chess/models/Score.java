package com.paulonio.chess.models;


import java.util.NoSuchElementException;

public enum Score {
    WHITE_WIN(0, "W", 1.0),
    BLACK_WIN(1, "B", 0.0),
    DRAW(2, "D", 0.5);

    private int orderInSpinner;
    private double scorePoints;
    private String dbName;

    Score(int orderInSpinner, String dbName, double scorePoints) {
        this.orderInSpinner = orderInSpinner;
        this.dbName = dbName;
        this.scorePoints = scorePoints;
    }

    public double getScorePoints(boolean ifForBlack) {
        return ifForBlack ? Math.abs(scorePoints - 1.0) : scorePoints;
    }

    public String getDbName() {
        return dbName;
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

    public static Score getValueFromDbName(String dbName) {
        for (Score orderingType : Score.values()) {
            if (orderingType.getDbName().equals(dbName)) {
                return orderingType;
            }
        }
        return null;
    }
}
