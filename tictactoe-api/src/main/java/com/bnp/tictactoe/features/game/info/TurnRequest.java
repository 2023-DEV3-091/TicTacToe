package com.bnp.tictactoe.features.game.info;

import com.bnp.tictactoe.data.dto.Position;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
public class TurnRequest {
    private Position.PlayerIdEnum playerId;
    @Min(value = 1, message = "rowIndex must be greater than or equal to 1")
    @Max(value = 3, message = "rowIndex must be less than or equal to 3")
    private int rowIndex;
    @Min(value = 1, message = "columnIndex must be greater than or equal to 1")
    @Max(value = 3, message = "columnIndex must be less than or equal to 3")
    private int columnIndex;
}
