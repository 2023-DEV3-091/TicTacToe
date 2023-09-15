package com.bnp.tictactoe;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bnp.tictactoe.data.dto.GameStateResponse;
import com.bnp.tictactoe.data.dto.GameStatusEnum;
import com.bnp.tictactoe.data.dto.Position;
import com.bnp.tictactoe.features.game.info.TurnRequest;
import com.bnp.tictactoe.model.context.GameContextHolder;
import com.bnp.tictactoe.model.data.Game;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        GameContextHolder GAME_CONTEXT = GameContextHolder.getInstance();
        Game existingGame = new Game();
        existingGame.setGameId(UUID.fromString("5e8bac93-fbfd-46de-85be-58b7ddb30efa"));
        existingGame.setStatus(GameStatusEnum.FINISHED);
        existingGame.setWinner(GameStateResponse.WinnerEnum.X);
        existingGame.setBoard(new int[][]{{1,1,1},
                                          {1,0,2},
                                          {0,2,2}
                                         });
        GAME_CONTEXT.setGame(existingGame);
    }

    @Test
    void testInitializeGameState() throws Exception {
        mockMvc.perform(post("/api/v1/start-game"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void testGetGameStateSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/state/5e8bac93-fbfd-46de-85be-58b7ddb30efa"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value("5e8bac93-fbfd-46de-85be-58b7ddb30efa"))
                .andExpect(jsonPath("$.status").value("Finished"))
                .andExpect(jsonPath("$.winner").value("X"))
                .andExpect(jsonPath("$.board.positionInfoList[0].playerId").value("X"))
                .andExpect(jsonPath("$.board.positionInfoList[1].playerId").value("X"))
                .andExpect(jsonPath("$.board.positionInfoList[2].playerId").value("X"))


                .andExpect(jsonPath("$.board.positionInfoList[3].playerId").value("X"))
                .andExpect(jsonPath("$.board.positionInfoList[4].playerId").value("-"))
                .andExpect(jsonPath("$.board.positionInfoList[5].playerId").value("O"))

                .andExpect(jsonPath("$.board.positionInfoList[6].playerId").value("-"))
                .andExpect(jsonPath("$.board.positionInfoList[7].playerId").value("O"))
                .andExpect(jsonPath("$.board.positionInfoList[8].playerId").value("O"));
    }

    @Test
    void testGetGameStateNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/state/5e8bac93-fbfd-46de-85be-58b7ddb30404"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("TTT-07"))
                .andExpect(jsonPath("$.message").value("Game not found"));
    }

    @Test
    void testPerformTurnDraw() throws Exception {
        createNewGameState("0ce3758e-5341-11ee-8c99-0242ac120002");
        testPerformFirstTurnXShouldAlwaysGoFirst();
        testPerformTurnSuccess(1,1,"X","0ce3758e-5341-11ee-8c99-0242ac120002");
        testPerformTurnCannotPlayOnPlayedPosition();
        testPerformTurnAlternateTurn();
        testPerformTurnSuccess(2,2,"O", "0ce3758e-5341-11ee-8c99-0242ac120002");
        testPerformTurnSuccess(2,1,"X","0ce3758e-5341-11ee-8c99-0242ac120002");
        testPerformTurnSuccess(3,1,"O", "0ce3758e-5341-11ee-8c99-0242ac120002");
        testPerformTurnSuccess(1,3,"X", "0ce3758e-5341-11ee-8c99-0242ac120002");
        testPerformTurnSuccess(1,2,"O", "0ce3758e-5341-11ee-8c99-0242ac120002");
        testPerformTurnSuccess(3,2,"X","0ce3758e-5341-11ee-8c99-0242ac120002");
        testPerformTurnSuccess(2,3,"O","0ce3758e-5341-11ee-8c99-0242ac120002");
        testPerformTurnSuccess(3,3,"X","0ce3758e-5341-11ee-8c99-0242ac120002");
        testGetGameStateInAGameDraw();
    }

    @Test
    void testPerformTurnWinnerX() throws Exception {
        createNewGameState("0ce3758e-5341-11ee-8c99-0242ac120003");
        testPerformTurnSuccess(1,1,"X", "0ce3758e-5341-11ee-8c99-0242ac120003");
        testPerformTurnSuccess(2,2,"O", "0ce3758e-5341-11ee-8c99-0242ac120003");
        testPerformTurnSuccess(2,1,"X", "0ce3758e-5341-11ee-8c99-0242ac120003");
        testPerformTurnSuccess(3,2,"O", "0ce3758e-5341-11ee-8c99-0242ac120003");
        testPerformTurnSuccess(3,1,"X", "0ce3758e-5341-11ee-8c99-0242ac120003");
        testGetGameStateInAWinnerX();
    }

    public void createNewGameState(String gameId) {
        GameContextHolder GAME_CONTEXT = GameContextHolder.getInstance();
        Game existingGame = new Game();
        existingGame.setGameId(UUID.fromString(gameId));
        existingGame.setStatus(GameStatusEnum.STARTED);
        existingGame.setWinner(null);
        existingGame.setBoard(new int[][]{{0,0,0},
                                          {0,0,0},
                                          {0,0,0}
                                         });
        GAME_CONTEXT.setGame(existingGame);
    }

    public void testPerformFirstTurnXShouldAlwaysGoFirst() throws Exception {
        mockMvc.perform(patch("/api/v1/turn/0ce3758e-5341-11ee-8c99-0242ac120002")
                        .content(objectMapper.writeValueAsString(getTurnRequest(1,1, "O")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("TTT-08"))
                .andExpect(jsonPath("$.message")
                        .value("X turn should always goes first."));
    }

    void testPerformTurnSuccess(int rowIndex, int colIndex, String playerId, String gameId) throws Exception {
        mockMvc.perform(patch("/api/v1/turn/" + gameId)
                        .content(objectMapper.writeValueAsString(getTurnRequest(rowIndex,colIndex, playerId)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNoContent());
    }

    public void testPerformTurnCannotPlayOnPlayedPosition() throws Exception {
        mockMvc.perform(patch("/api/v1/turn/0ce3758e-5341-11ee-8c99-0242ac120002")
                        .content(objectMapper.writeValueAsString(getTurnRequest(1,1, "O")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("TTT-09"))
                .andExpect(jsonPath("$.message")
                        .value("Players cannot play on a played position."));
    }

    public void testPerformTurnAlternateTurn() throws Exception {
        mockMvc.perform(patch("/api/v1/turn/0ce3758e-5341-11ee-8c99-0242ac120002")
                        .content(objectMapper.writeValueAsString(getTurnRequest(1,2, "X")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("TTT-10"))
                .andExpect(jsonPath("$.message")
                        .value("Players must take alternate turn."));
    }

    public void testGetGameStateInAGameDraw() throws Exception {
        mockMvc.perform(get("/api/v1/state/0ce3758e-5341-11ee-8c99-0242ac120002"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value("0ce3758e-5341-11ee-8c99-0242ac120002"))
                .andExpect(jsonPath("$.status").value("Finished"))
                .andExpect(jsonPath("$.winner").value("DRAW"))
                .andExpect(jsonPath("$.board.positionInfoList[0].playerId").value("X"))
                .andExpect(jsonPath("$.board.positionInfoList[1].playerId").value("O"))
                .andExpect(jsonPath("$.board.positionInfoList[2].playerId").value("X"))


                .andExpect(jsonPath("$.board.positionInfoList[3].playerId").value("X"))
                .andExpect(jsonPath("$.board.positionInfoList[4].playerId").value("O"))
                .andExpect(jsonPath("$.board.positionInfoList[5].playerId").value("O"))

                .andExpect(jsonPath("$.board.positionInfoList[6].playerId").value("O"))
                .andExpect(jsonPath("$.board.positionInfoList[7].playerId").value("X"))
                .andExpect(jsonPath("$.board.positionInfoList[8].playerId").value("X"));
    }

    public void testGetGameStateInAWinnerX() throws Exception {
        mockMvc.perform(get("/api/v1/state/0ce3758e-5341-11ee-8c99-0242ac120003"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameId").value("0ce3758e-5341-11ee-8c99-0242ac120003"))
                .andExpect(jsonPath("$.status").value("Finished"))
                .andExpect(jsonPath("$.winner").value("X"))
                .andExpect(jsonPath("$.board.positionInfoList[0].playerId").value("X"))
                .andExpect(jsonPath("$.board.positionInfoList[1].playerId").value("-"))
                .andExpect(jsonPath("$.board.positionInfoList[2].playerId").value("-"))


                .andExpect(jsonPath("$.board.positionInfoList[3].playerId").value("X"))
                .andExpect(jsonPath("$.board.positionInfoList[4].playerId").value("O"))
                .andExpect(jsonPath("$.board.positionInfoList[5].playerId").value("-"))

                .andExpect(jsonPath("$.board.positionInfoList[6].playerId").value("X"))
                .andExpect(jsonPath("$.board.positionInfoList[7].playerId").value("O"))
                .andExpect(jsonPath("$.board.positionInfoList[8].playerId").value("-"));
    }


    //todo: below can be moved to DataProvider
    private TurnRequest getTurnRequest(Integer rowIndex, Integer colIndex, String playerId) {
        TurnRequest turnRequest = new TurnRequest();
        turnRequest.setPlayerId(Position.PlayerIdEnum.fromValue(playerId));
        turnRequest.setRowIndex(rowIndex);
        turnRequest.setColumnIndex(colIndex);
        return turnRequest;
    }
}
