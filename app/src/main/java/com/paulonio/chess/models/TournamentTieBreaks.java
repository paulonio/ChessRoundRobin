package com.paulonio.chess.models;

import java.util.NoSuchElementException;

public enum TournamentTieBreaks {
    BERGER(0, "BR"),
    DIRECT_GAME(1, "DG");

    private int orderInSpinner;
    private String dbName;

    TournamentTieBreaks(int orderInSpinner, String dbName) {
        this.orderInSpinner = orderInSpinner;
        this.dbName = dbName;
    }

    public int getOrderInSpinner() {
        return orderInSpinner;
    }

    public String getDbName() {
        return dbName;
    }


    public static TournamentTieBreaks getValueFromSpinnerIndex(int index) {
        for (TournamentTieBreaks tieBreak : TournamentTieBreaks.values()) {
            if (tieBreak.getOrderInSpinner() == index) {
                return tieBreak;
            }
        }
        throw new NoSuchElementException("Element with given index do not exists in ListOrderingType enum");
    }

    public static TournamentTieBreaks getValueFromDbName(String dbName) {
        for (TournamentTieBreaks orderingType : TournamentTieBreaks.values()) {
            if (orderingType.getDbName().equals(dbName)) {
                return orderingType;
            }
        }
        throw new NoSuchElementException("Element with given index do not exists in ListOrderingType enum");
    }
}
