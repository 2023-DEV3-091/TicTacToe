package com.bnp.tictactoe.model.data;

import com.bnp.tictactoe.data.dto.GameStateResponse;
import com.bnp.tictactoe.data.dto.GameStatusEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game {

    private UUID gameId;
    private int[][] board = new int[3][3];
    private GameStatusEnum status;
    private GameStateResponse.WinnerEnum winner;
    private List<String> moves = new ArrayList<>();

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public List<String> getMoves() {
        return moves;
    }

    public GameStatusEnum getStatus() {
        return status;
    }

    public void setStatus(GameStatusEnum status) {
        this.status = status;
    }

    public GameStateResponse.WinnerEnum getWinner() {
        return winner;
    }

    public void setWinner(GameStateResponse.WinnerEnum winner) {
        this.winner = winner;
    }
}
