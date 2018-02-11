package com.paulonio.chess.models;

import java.util.NoSuchElementException;

public enum TournamentTieBreaks {
    BERGER(0),
    DIRECT_GAME(1);

    private int orderInSpinner;

    TournamentTieBreaks(int orderInSpinner) {
        this.orderInSpinner = orderInSpinner;
    }

    public int getOrderInSpinner() {
        return orderInSpinner;
    }

    public static TournamentTieBreaks getValueFromSpinnerIndex(int index) {
        for (TournamentTieBreaks tieBreak : TournamentTieBreaks.values()) {
            if (tieBreak.getOrderInSpinner() == index) {
                return tieBreak;
            }
        }
        throw new NoSuchElementException("Element with given index do not exists in ListOrderingType enum");
    }
}
