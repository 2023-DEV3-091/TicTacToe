package com.bnp.tictactoe;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
                .andExpect(jsonPath("$.board.positionInfoList[0].rowIndex").value(0))
                .andExpect(jsonPath("$.board.positionInfoList[0].columnIndex").value(0))
                .andExpect(jsonPath("$.board.positionInfoList[0].playerId").value("X"))

                .andExpect(jsonPath("$.board.positionInfoList[1].rowIndex").value(0))
                .andExpect(jsonPath("$.board.positionInfoList[1].columnIndex").value(1))
                .andExpect(jsonPath("$.board.positionInfoList[1].playerId").value("X"))

                .andExpect(jsonPath("$.board.positionInfoList[2].rowIndex").value(0))
                .andExpect(jsonPath("$.board.positionInfoList[2].columnIndex").value(2))
                .andExpect(jsonPath("$.board.positionInfoList[2].playerId").value("0"))

                .andExpect(jsonPath("$.board.positionInfoList[3].rowIndex").value(1))
                .andExpect(jsonPath("$.board.positionInfoList[3].columnIndex").value(0))
                .andExpect(jsonPath("$.board.positionInfoList[3].playerId").value("X"))

                .andExpect(jsonPath("$.board.positionInfoList[4].rowIndex").value(1))
                .andExpect(jsonPath("$.board.positionInfoList[4].columnIndex").value(1))
                .andExpect(jsonPath("$.board.positionInfoList[4].playerId").value("-"))

                .andExpect(jsonPath("$.board.positionInfoList[5].rowIndex").value(1))
                .andExpect(jsonPath("$.board.positionInfoList[5].columnIndex").value(2))
                .andExpect(jsonPath("$.board.positionInfoList[5].playerId").value("-"))

                .andExpect(jsonPath("$.board.positionInfoList[6].rowIndex").value(2))
                .andExpect(jsonPath("$.board.positionInfoList[6].columnIndex").value(0))
                .andExpect(jsonPath("$.board.positionInfoList[6].playerId").value("-"))

                .andExpect(jsonPath("$.board.positionInfoList[7].rowIndex").value(2))
                .andExpect(jsonPath("$.board.positionInfoList[7].columnIndex").value(1))
                .andExpect(jsonPath("$.board.positionInfoList[7].playerId").value("0"))

                .andExpect(jsonPath("$.board.positionInfoList[8].rowIndex").value(2))
                .andExpect(jsonPath("$.board.positionInfoList[8].columnIndex").value(2))
                .andExpect(jsonPath("$.board.positionInfoList[8].playerId").value("O"))

        ;
    }

    @Test
    void testGetGameStateNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/state/5e8bac93-fbfd-46de-85be-58b7ddb30404"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
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
