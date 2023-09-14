package com.bnp.tictactoe.features.game.mapper;

import com.bnp.tictactoe.data.dto.BoardInfo;
import com.bnp.tictactoe.data.dto.GameStateResponse;
import com.bnp.tictactoe.data.dto.GameStatusEnum;
import com.bnp.tictactoe.data.dto.Position;
import com.bnp.tictactoe.features.game.utils.BoardHelper;
import com.bnp.tictactoe.model.data.Game;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class GameMapper {

    private GameMapper() {
    }

    public Game toInitializeGame() {
        Game game = new Game();
        game.setGameId(UUID.randomUUID());
        game.setStatus(GameStatusEnum.STARTED);

        return game;
    }

    public GameStateResponse toGameStateResponse(Game game) {

        GameStateResponse gameStateResponse = new GameStateResponse();

        gameStateResponse.setGameId(game.getGameId());
        gameStateResponse.setStatus(game.getStatus());
        gameStateResponse.setWinner(game.getWinner());
        gameStateResponse.setBoard(toBoard(game.getBoard()));

        return gameStateResponse;
    }

    private BoardInfo toBoard(int[][] board) {
        BoardInfo boardInfo = new BoardInfo();
        List<Position> positionInfoList = new ArrayList<>();
        int[] boardInLinearList = BoardHelper.getBoardInLinearList(board);
        Arrays.stream(boardInLinearList).sequential()
            .mapToObj(boardState -> {
                Position position = new Position();
                position.setPlayerId(Position.PlayerIdEnum.fromValue(mapPlayerId(boardState)));
                return position;
            })
            .forEach(positionInfoList::add);

        boardInfo.setPositionInfoList(positionInfoList);
        return boardInfo;
    }

    private String mapPlayerId(int boardStatePos) {
        if (boardStatePos == 1) {
            return "X";
        }
        else if (boardStatePos == 2) {
            return "O";
        }
        else {
            return "-";
        }
    }

    public int mapPlayerIdFromResponse(String playerId) {
        if (playerId.equals("X")) {
            return 1;
        }
        else if (playerId.equals("O")) {
            return 2;
        }
        else {
            return 0;
        }
    }
}