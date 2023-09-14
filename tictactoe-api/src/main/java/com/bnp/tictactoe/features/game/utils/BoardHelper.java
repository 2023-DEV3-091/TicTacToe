package com.bnp.tictactoe.features.game.utils;


public class BoardHelper {

    public static int[] getBoardInLinearList(int[][] boards) {

        int[] boardInLinearList = new int[9];
        int linearPosition = 0;

        for (int[] board : boards) {
            for (int positionIn2d : board) {
                boardInLinearList[linearPosition] = positionIn2d;
                linearPosition++;
            }
        }
        return boardInLinearList;
    }
}
