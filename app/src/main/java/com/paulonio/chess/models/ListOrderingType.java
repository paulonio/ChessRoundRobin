package com.paulonio.chess.models;


import java.util.NoSuchElementException;

public enum ListOrderingType {
    RANDOM(0),
    ALPHABETICAL(1),
    RATING(2);

    private int orderInSpinner;

    ListOrderingType(int orderInSpinner) {
        this.orderInSpinner = orderInSpinner;
    }

    public int getOrderInSpinner() {
        return orderInSpinner;
    }

    public static ListOrderingType getValueFromSpinnerIndex(int index) {
        for (ListOrderingType orderingType : ListOrderingType.values()) {
            if (orderingType.getOrderInSpinner() == index) {
                return orderingType;
            }
        }
        throw new NoSuchElementException("Element with given index do not exists in ListOrderingType enum");
    }
}
