# TicTacToe

Tic-Tac-Toe Kata Game


## Actions

- Start Game     : To initialize game state to new         ( POST /api/v1/start-game )
- GET Game State : Get current state of game               ( GET /api/v1/state/{game-id} )
- Perform Turn   : Update game board to make a move / turn ( PATCH   /api/v1/turn/{game-id} )

## For more detail about request /  response detail:

- Have a look at API Specification swagger file tictactoe-api/src/main/resources/swagger/TicTacToe.yaml
