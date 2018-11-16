package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.Appl.GameList;
import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Model.*;
import spark.Request;
import spark.Response;
import spark.Route;


public class PostValidateMoveRoute implements Route {

    //region Constants

    private static final String SESSION_ATTR = "id";

    //endregion

    //region Attributes

    private PlayerLobby playerLobby;
    private GameList gameList;
    private Gson gson;

    //endregion

    //region Constructor

    PostValidateMoveRoute(PlayerLobby playerLobby, GameList gameList, Gson gson) {
        this.playerLobby = playerLobby;
        this.gameList = gameList;
        this.gson = gson;
    }

    //endregion

    //region Public Methods

    @Override
    public Object handle(Request request, Response response) {
        String moveAsJSONString = request.body();
        Move move = gson.fromJson(moveAsJSONString, Move.class);

        String userName = request.session().attribute(SESSION_ATTR);
        Player player = playerLobby.getPlayer(userName);
        BoardModel model = gameList.getBoardModel(player);

        if (MoveValidator.validateMove(model, move)) {
            model.addPendingMove(move);
            return (gson.toJson(new Message(Message.TYPE.info, "Valid move")));
        }
        return (gson.toJson(new Message(Message.TYPE.error, "Invalid move buckaroo")));
    }

    //endregion
}
