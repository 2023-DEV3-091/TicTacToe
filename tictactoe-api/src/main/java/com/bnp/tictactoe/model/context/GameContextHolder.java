package com.bnp.tictactoe.model.context;

import com.bnp.tictactoe.model.data.Game;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameContextHolder {

    private static Map<UUID, Game> games;

    private static GameContextHolder gameContextHolderInstance;

    private GameContextHolder(){
        games = new HashMap<>();
    }

    public static synchronized GameContextHolder getInstance(){
        if(gameContextHolderInstance == null) {
            gameContextHolderInstance = new GameContextHolder();
        }
        return gameContextHolderInstance;
    }

    public Map<UUID, Game> getGames(){
        return games;
    }

    public void setGame(Game game) {
        games.put(game.getGameId(), game);
    }
}
