package com.bnp.tictactoe.features.game.mapper;

import com.bnp.tictactoe.data.dto.GameStatusEnum;
import com.bnp.tictactoe.model.data.Game;
import org.springframework.stereotype.Service;
import java.util.UUID;

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

}