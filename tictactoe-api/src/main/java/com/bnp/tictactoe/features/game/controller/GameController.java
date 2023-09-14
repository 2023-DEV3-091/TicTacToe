package com.bnp.tictactoe.features.game.controller;

import com.bnp.tictactoe.features.game.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
