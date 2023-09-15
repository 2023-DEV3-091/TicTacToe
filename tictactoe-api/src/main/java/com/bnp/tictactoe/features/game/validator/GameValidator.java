package com.bnp.tictactoe.features.game.validator;

import com.bnp.tictactoe.core.ApplicationError;
import com.bnp.tictactoe.core.ApplicationException;
import com.bnp.tictactoe.data.dto.GameStatusEnum;
import com.bnp.tictactoe.data.dto.Position;
import com.bnp.tictactoe.features.game.info.TurnRequest;
import com.bnp.tictactoe.model.data.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Service
public class GameValidator {

    private final Logger log = LoggerFactory.getLogger(getClass());


    public void validateTurnRequest(UUID gameId, TurnRequest turnRequest, Game game)  {

        validateGameId(gameId, game);

        validateGameStatus(gameId, game);

        validateTurnsAndPositions(turnRequest, game);
    }
    
    public void validateGameId(UUID gameId, Game game) {
        if(ObjectUtils.isEmpty(game)) {
            log.error("Game with ID {} does not exist", gameId);
            throw new ApplicationException(ApplicationError.GAME_NOT_FOUND);
        }
    }

    private void validateGameStatus(UUID gameId,Game game){
        if(GameStatusEnum.FINISHED.equals(game.getStatus())) {
            log.error("Game with ID {} is already finished", gameId);
            throw new ApplicationException(ApplicationError.GAME_ALREADY_FINISHED);
        }
    }

    private void validateTurnsAndPositions(TurnRequest turnRequest, Game existingGameState) {

        validateFirstTurn(turnRequest, existingGameState);

        //todo: need to verify this check
        if(!isFirstMove(existingGameState)) {
            validateAlternateTurn(turnRequest, existingGameState);
        }

        validatePosition(turnRequest, existingGameState);
    }

    private void validateFirstTurn(TurnRequest turnRequest, Game existingGameState) {
        if(isFirstMove(existingGameState)
                && currentMoveIsNotX(turnRequest.getPlayerId())) {
            log.warn("X turn should always goes first.");
            throw new ApplicationException(ApplicationError.X_TURN_FIRST);
        }
    }

    private boolean isFirstMove(Game game) {
        return game.getMoves().isEmpty();
    }

    private void validatePosition(TurnRequest turnRequest, Game existingGameState) {
        if(isPositionAlreadyPlayed(
                turnRequest.getRowIndex(),
                turnRequest.getColumnIndex(),
                existingGameState.getBoard())) {

            log.warn("Players cannot play on a played position.");
            throw new ApplicationException(ApplicationError.PLAYED_POSITION);
        }
    }

    private boolean isPositionAlreadyPlayed(int row, int col, int[][] board) {
        return board[row - 1][col - 1] != 0;
    }

    private void validateAlternateTurn(TurnRequest turnRequest, Game game) {
        String lastTurn = game.getMoves().get(game.getMoves().size() - 1);
        Position.PlayerIdEnum currentTurnPlayerId = turnRequest.getPlayerId();

        if (lastTurn.equalsIgnoreCase(currentTurnPlayerId.getValue())) {

            log.warn("Players must take alternate turn.");
            throw new ApplicationException(ApplicationError.ALTERNATE_TURN);
        }
    }

    private boolean currentMoveIsNotX(Position.PlayerIdEnum playerIdEnum) {
        return !Position.PlayerIdEnum.X.equals(playerIdEnum);
    }
}
