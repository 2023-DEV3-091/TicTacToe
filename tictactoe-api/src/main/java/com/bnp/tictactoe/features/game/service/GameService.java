package com.bnp.tictactoe.features.game.service;

import com.bnp.tictactoe.data.dto.GameStateResponse;
import com.bnp.tictactoe.data.dto.GameStatusEnum;
import com.bnp.tictactoe.features.game.info.TurnRequest;
import com.bnp.tictactoe.features.game.mapper.GameMapper;
import com.bnp.tictactoe.features.game.utils.BoardHelper;
import com.bnp.tictactoe.features.game.validator.GameValidator;
import com.bnp.tictactoe.model.context.GameContextHolder;
import com.bnp.tictactoe.model.data.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class GameService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static GameContextHolder GAME_CONTEXT = GameContextHolder.getInstance();

    private final GameMapper gameMapper;
    private final GameValidator gameValidator;
    private final BoardHelper boardHelper;

    public GameService(GameMapper gameMapper, GameValidator gameValidator, BoardHelper boardHelper) {
        this.gameMapper = gameMapper;
        this.gameValidator = gameValidator;
        this.boardHelper = boardHelper;
    }

    public String initializeGameState() {

        Game game = gameMapper.toInitializeGame();

        GAME_CONTEXT.setGame(game);

        log.debug("Game initialized successfully");

        return game.getGameId().toString();
    }

    public GameStateResponse getGameState(UUID gameId) {

        Game game = GAME_CONTEXT.getGames().get(gameId);

        gameValidator.validateGameId(gameId, game);

        return gameMapper.toGameStateResponse(game);
    }

    public void performTurn(UUID gameId, TurnRequest turnRequest) {

        Game existingGameState = GAME_CONTEXT.getGames().get(gameId);

        gameValidator.validateTurnRequest(gameId, turnRequest, existingGameState);

        performTurnAndSaveState(turnRequest, existingGameState);

        if(hasPlayerWin(turnRequest, existingGameState)) {
            existingGameState.setStatus(GameStatusEnum.FINISHED);
            existingGameState.setWinner(GameStateResponse.WinnerEnum.valueOf(
                    turnRequest.getPlayerId().getValue()));
            log.info("Game finished with winner {}", turnRequest.getPlayerId().getValue());

        } else if (boardHelper.isAllSquaresFilled(existingGameState.getBoard())) {
            existingGameState.setStatus(GameStatusEnum.FINISHED);
            existingGameState.setWinner(GameStateResponse.WinnerEnum.DRAW);
            log.info("Game finished in draw");
        }
    }

    private void performTurnAndSaveState(TurnRequest turnRequest, Game existingGameState) {
        existingGameState.getMoves().add(turnRequest.getPlayerId().getValue());
        existingGameState
                .getBoard()[turnRequest.getRowIndex() - 1]
                           [turnRequest.getColumnIndex() - 1]
                = gameMapper.mapPlayerIdFromResponse(turnRequest.getPlayerId().getValue());
    }

    private boolean hasPlayerWin(TurnRequest turnRequest, Game existingGameState) {
        return boardHelper.playerHasThreeInARow(existingGameState.getBoard(),
                gameMapper.mapPlayerIdFromResponse(turnRequest.getPlayerId().getValue()));
    }
}
