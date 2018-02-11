package com.paulonio.chess.utils;

import java.util.Random;

/**
 * Implements round-robin algorithm based on pairing tables constructed by Richard Schurig
 */
public class RoundRobinUtils {

    public static int[][][] getRoundRobinPairings(int numberOfPlayers) {
        int n;
        n = (numberOfPlayers % 2 == 1) ? (numberOfPlayers + 1) : numberOfPlayers;
        int[][][] pairings = new int[n - 1][n / 2][2];
        int[][] table = getSchurigTable(n);
        int[][] oppositeTable = getOppositeSchurigTable(n);
        boolean colorOnFirstTable = new Random().nextBoolean();
        for (int i = 0; i < (n - 1); ++i) {
            for (int j = 0; j < (n / 2); ++j) {
                pairings[i][j][0] = table[i][j];
                pairings[i][j][1] = oppositeTable[i][j];
                if (j == 0) {
                    if (colorOnFirstTable) {
                        pairings[i][j][0] = n - 1;
                    } else {
                        pairings[i][j][1] = n - 1;
                    }
                    colorOnFirstTable = !colorOnFirstTable;
                }
            }
        }
        return pairings;
    }


    private static int[][] getSchurigTable(int n) {
        int[][] table = new int[n - 1][n / 2];
        for (int i = 0; i < (n - 1); ++i) {
            for (int j = 0; j < (n / 2); ++j) {
                table[i][j] = (i * (n / 2) + j) % (n - 1);
            }
        }
        return table;

    }

    private static int[][] getOppositeSchurigTable(int n) {
        int[][] table = new int[n - 1][n / 2];
        int actualNumber = 0;
        for (int i = 0; i < (n - 1); ++i) {
            for (int j = 0; j < (n / 2); ++j) {
                table[i][j] = actualNumber;
                --actualNumber;
                if (actualNumber < 0) {
                    actualNumber = n - 2;
                }
            }
            ++actualNumber;
        }
        return table;
    }
}
