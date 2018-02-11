package com.paulonio.chess.models;


import java.util.NoSuchElementException;

public enum TournamentType {
    CLASSIC(0),
    RAPID(1),
    BLITZ(2);

    private int orderInSpinner;

    TournamentType(int orderInSpinner) {
        this.orderInSpinner = orderInSpinner;
    }

    public int getOrderInSpinner() {
        return orderInSpinner;
    }

    public static TournamentType getValueFromSpinnerIndex(int index) {
        for (TournamentType tournamentType : TournamentType.values()) {
            if (tournamentType.getOrderInSpinner() == index) {
                return tournamentType;
            }
        }
        throw new NoSuchElementException("Element with given index do not exists in ListOrderingType enum");
    }
}
