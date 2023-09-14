package com.bnp.tictactoe.core;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public enum ApplicationError {

    FORBIDDEN(HttpStatus.FORBIDDEN, "TTT-00", "Action Foridden", Level.ERROR),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "TTT-01", "Bad Request", Level.ERROR),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "TTT-02", "Server Error", Level.ERROR),
    MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "TTT-03", "Missing request parameter", Level.ERROR),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "TTT-04", "Invalid value for one or more parameters in request",
            Level.WARN),
    HTTP_METHOD_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "TTT-05", "HTTP Method not supported", Level.ERROR),
    MISSING_REQUEST_BODY(HttpStatus.BAD_REQUEST, "TTT-06", "Required request body is missing", Level.ERROR),
    GAME_NOT_FOUND(HttpStatus.NOT_FOUND, "TTT-07", "Game not found", Level.ERROR),
    X_TURN_FIRST(HttpStatus.BAD_REQUEST, "TTT-08", "X turn should always goes first.", Level.WARN),
    PLAYED_POSITION(HttpStatus.BAD_REQUEST, "TTT-09", "Players cannot play on a played position.", Level.WARN),
    ALTERNATE_TURN(HttpStatus.BAD_REQUEST, "TTT-10", "Players must take alternate turn.", Level.WARN),
    GAME_ALREADY_FINISHED(HttpStatus.FORBIDDEN, "TTT-11", "Game already finished", Level.WARN);


    private HttpStatus httpStatus;
    private String code;
    private String message;
    private Level logLevel;

    ApplicationError(HttpStatus httpStatus, String code, String message, Level logLevel) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Level getLogLevel() {
        return logLevel;
    }
}
