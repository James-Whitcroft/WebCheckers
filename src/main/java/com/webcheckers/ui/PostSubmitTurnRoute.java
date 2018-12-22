package com.webcheckers.ui;
import com.google.gson.Gson;
import com.webcheckers.Appl.GameList;
import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Model.*;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;


public class PostSubmitTurnRoute implements Route {
    //region Attributes

    private GameList gameList;
    private PlayerLobby playerLobby;
    private Gson gson;

    //endregion

    //region Constructor

    public PostSubmitTurnRoute(GameList gameList, PlayerLobby playerLobby, Gson gson) {
        this.gameList = gameList;
        this.playerLobby = playerLobby;
        this.gson = gson;
    }

    //endregion

    //region Public Methods

    @Override
    public Object handle(Request request, Response response) {
        // retrieve the HTTP session
        final Session httpSession = request.session();
        String playerName = httpSession.attribute(PostSignInRoute.SESSION_ATTR);
        Player currentPlayer = playerLobby.getPlayer(playerName);
        BoardModel model = gameList.getBoardModel(currentPlayer);

        // Checks if the jump is finished
        Position lastPos = model.getPendingMoves().getLast().getEnd();
        if (model.isJumping() && MoveValidator.pieceHasJump(model, lastPos)) {
            return gson.toJson(new Message(Message.TYPE.error, "Finish your jump!"));
        }

        // Finalizes moves and changes turn
        model.makeMoves();
        model.changeTurns();

        // Determine if the game is over
        if (!MoveValidator.teamHasMove(model, model.getRedPlayer().equals(currentPlayer) ? Piece.color.WHITE : Piece.color.RED) ||
                model.getWinner() != null) {

            Player red = gameList.getBoardModel(currentPlayer).getRedPlayer();
            Player white = gameList.getBoardModel(currentPlayer).getWhitePlayer();

            if (currentPlayer.equals(red)) {
                red.setInGame(false);
                red.winGame();
            } else {
                white.setInGame(false);
                white.winGame();
            }
            String winner = model.getWinner() == null ? "You won. " : model.getWinner().getName() + " has won. ";
            return gson.toJson(new Message(Message.TYPE.error, winner + "Game over."));
        }

        // Makes AI move if the opponent is an AI
        Player white = gameList.getBoardModel(currentPlayer).getWhitePlayer();
        if (white instanceof AIPlayer) {
            ((AIPlayer) white).makeMove(model);
        }

        return gson.toJson(new Message(Message.TYPE.info, "Waiting"));
    }

}
