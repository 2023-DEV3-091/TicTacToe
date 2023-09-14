package com.bnp.tictactoe.features.game.controller;

import com.bnp.tictactoe.data.dto.GameStateResponse;
import com.bnp.tictactoe.features.game.service.GameService;
import com.bnp.tictactoe.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Validated
@RestController
public class GameController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/api/v1/start-game")
    public ResponseEntity<Void> initializeGameState() {

        log.debug("Request: POST /api/v1/start-game");

        String gameId = gameService.initializeGameState();

        String gameLocation = "/api/v1/start-game" + gameId;

        log.debug("Location: {}", gameLocation);

        return ResponseEntity.status(HttpStatus.CREATED).header("Location", gameLocation).body(null);
    }

    @GetMapping("/api/v1/state/{game-id}")
    public ResponseEntity<GameStateResponse> getGameState(@PathVariable(value = "game-id") UUID gameId) {

        log.debug("Request: GET /api/v1/state/{}", gameId);

        GameStateResponse response = gameService.getGameState(gameId);

        log.debug("Response Body: {}", JsonUtils.formatJson(response));

        return ResponseEntity.ok(response);

    }
}
