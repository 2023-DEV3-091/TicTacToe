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
import com.bnp.tictactoe.model.context.GameContextHolder;
import com.bnp.tictactoe.model.data.Game;
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

    @BeforeEach
    public void initMocks() throws Exception {
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
    void testPerformTurnSuccess() throws Exception {
        mockMvc.perform(patch("/api/v1/turn/5e8bac93-fbfd-46de-85be-58b7ddb30efa")
                        .content(getTurnRequest())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    void testPerformTurnOnSameBlock() throws Exception {
        mockMvc.perform(patch("/api/v1/turn/5e8bac93-fbfd-46de-85be-58b7ddb30efa")
                        .content(getTurnRequest())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().is4xxClientError());
    }


    //todo: below can be moved to DataProvider
    private String getTurnRequest() {
        return "{\n"
                + "  \"turnPositionInfo\": {\n"
                + "    \"rowIndex\": 0,\n"
                + "    \"columnIndex\": 0,\n"
                + "    \"playerId\": \"X\"\n" +
                "    }\n"
                + "}";
    }
}
