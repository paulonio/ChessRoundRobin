package com.paulonio.chess.models;


import java.util.NoSuchElementException;

public enum ListOrderingType {
    RANDOM(0, "RND"),
    ALPHABETICAL(1, "ALP"),
    RATING(2, "RTG");

    private int orderInSpinner;
    private String dbName;

    ListOrderingType(int orderInSpinner, String dbName) {
        this.orderInSpinner = orderInSpinner;
        this.dbName = dbName;
    }

    public int getOrderInSpinner() {
        return orderInSpinner;
    }

    public String getDbName() {
        return dbName;
    }

    public static ListOrderingType getValueFromSpinnerIndex(int index) {
        for (ListOrderingType orderingType : ListOrderingType.values()) {
            if (orderingType.getOrderInSpinner() == index) {
                return orderingType;
            }
        }
        throw new NoSuchElementException("Element with given index do not exists in ListOrderingType enum");
    }

    public static ListOrderingType getValueFromDbName(String dbName) {
        for (ListOrderingType orderingType : ListOrderingType.values()) {
            if (orderingType.getDbName().equals(dbName)) {
                return orderingType;
            }
        }
        throw new NoSuchElementException("Element with given index do not exists in ListOrderingType enum");
    }
}
