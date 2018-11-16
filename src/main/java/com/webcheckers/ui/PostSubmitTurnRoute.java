package com.webcheckers.ui;
import com.google.gson.Gson;
import com.webcheckers.Appl.GameList;
import com.webcheckers.Model.*;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;


public class PostSubmitTurnRoute implements Route {
    //region Attributes

    private GameList gameList;
    private Gson gson;

    //endregion

    //region Constructor

    public PostSubmitTurnRoute( GameList gameList, Gson gson) {
        this.gameList = gameList;
        this.gson = gson;
    }

    //endregion

    //region Public Methods

    @Override
    public Object handle(Request request, Response response) {
        // retrieve the HTTP session
        final Session httpSession = request.session();
        String playerName = httpSession.attribute(PostSignInRoute.SESSION_ATTR);
        Player test = new Player(playerName);
        BoardModel model = gameList.getBoardModel(test);

        Position lastPos = model.getPendingMoves().getLast().getEnd();
        if (model.isJumping() && MoveValidator.pieceHasJump(model, lastPos)) {
            return gson.toJson(new Message(Message.TYPE.error, "Finish your jump!"));
        }

        model.makeMoves();
        model.changeTurns();
        if (!MoveValidator.teamHasMove(model, model.getRedPlayer().equals(test) ? Piece.color.WHITE : Piece.color.RED) ||
                model.getWinner() != null) {

            Player red = gameList.getBoardModel(test).getRedPlayer();
            Player white = gameList.getBoardModel(test).getWhitePlayer();

            if (test.equals(red)) {
                red.setInGame(false);
                red.winGame();
            } else {
                white.setInGame(false);
                white.winGame();
            }

            String winner = model.getWinner() == null ? "You won. " : model.getWinner().getName() + " has won. ";
            return gson.toJson(new Message(Message.TYPE.error, winner + "Game over."));
        }
        return gson.toJson(new Message(Message.TYPE.info, "Waiting"));
    }

}
