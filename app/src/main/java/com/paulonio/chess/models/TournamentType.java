package com.paulonio.chess.models;


import java.util.NoSuchElementException;

public enum TournamentType {
    CLASSIC(0, "C"),
    RAPID(1, "R"),
    BLITZ(2, "B");

    private int orderInSpinner;
    private String dbName;

    TournamentType(int orderInSpinner, String dbName) {
        this.orderInSpinner = orderInSpinner;
        this.dbName = dbName;
    }

    public int getOrderInSpinner() {
        return orderInSpinner;
    }

    public String getDbName() {
        return dbName;
    }

    public static TournamentType getValueFromSpinnerIndex(int index) {
        for (TournamentType tournamentType : TournamentType.values()) {
            if (tournamentType.getOrderInSpinner() == index) {
                return tournamentType;
            }
        }
        throw new NoSuchElementException("Element with given index do not exists in ListOrderingType enum");
    }

    public static TournamentType getValueFromDbName(String dbName) {
        for (TournamentType orderingType : TournamentType.values()) {
            if (orderingType.getDbName().equals(dbName)) {
                return orderingType;
            }
        }
        throw new NoSuchElementException("Element with given index do not exists in ListOrderingType enum");
    }
}
