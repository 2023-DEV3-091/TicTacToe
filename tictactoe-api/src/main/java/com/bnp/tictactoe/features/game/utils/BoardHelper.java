package com.bnp.tictactoe.features.game.utils;

import org.springframework.stereotype.Component;

@Component
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

    private final int[][] GAME_FINISHED_COMBINATION_POSITIONS = {
            //horizontal
            {0,1,2}, {3,4,5}, {6,7,8},
            //vertical
            {0,3,6}, {1,4,7},{2,5,8},
            //diagonal
            {0,4,8}, {2,4,6}
    };

    public boolean playerHasThreeInARow(int[][] board, int playerId) {

        int[] gameBoardInArray = getBoardInLinearList(board);

        for (int[] horizontalCombinationPosition : GAME_FINISHED_COMBINATION_POSITIONS) {
            int counter = 0;
            for (int i : horizontalCombinationPosition) {
                if (gameBoardInArray[i] == playerId) {
                    counter++;
                    if (counter == 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isAllSquaresFilled(int[][] boards) {
        for (int[] board : boards) {
            for (int i : board) {
                if (i == 0) {
                    return false;
                }
            }
        }
        return true;
    }

}
