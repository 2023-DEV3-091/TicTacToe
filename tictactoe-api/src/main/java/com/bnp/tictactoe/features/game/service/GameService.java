package com.bnp.tictactoe.features.game.service;

import com.bnp.tictactoe.core.ApplicationError;
import com.bnp.tictactoe.core.ApplicationException;
import com.bnp.tictactoe.data.dto.GameStateResponse;
import com.bnp.tictactoe.features.game.mapper.GameMapper;
import com.bnp.tictactoe.model.context.GameContextHolder;
import com.bnp.tictactoe.model.data.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.UUID;

@Service
public class GameService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static GameContextHolder GAME_CONTEXT = GameContextHolder.getInstance();

    private final GameMapper gameMapper;

    public GameService(GameMapper gameMapper) {
        this.gameMapper = gameMapper;
    }

    public String initializeGameState() {

        Game game = gameMapper.toInitializeGame();

        GAME_CONTEXT.setGame(game);

        log.debug("Game initialized successfully");

        return game.getGameId().toString();
    }

    public GameStateResponse getGameState(UUID gameId) {

        Game game = GAME_CONTEXT.getGames().get(gameId);

        if(ObjectUtils.isEmpty(game)) {
            log.error("Game with ID {} does not exist", gameId);
            throw new ApplicationException(ApplicationError.GAME_NOT_FOUND);
        }
        return gameMapper.toGameStateResponse(game);
    }
}
