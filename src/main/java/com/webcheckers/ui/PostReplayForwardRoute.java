package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.Appl.MoveList;
import com.webcheckers.Appl.SavedGameList;
import com.webcheckers.Model.Message;
import com.webcheckers.Model.Move;
import com.webcheckers.Model.MoveValidator;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.sql.Savepoint;
import java.util.ArrayList;

public class PostReplayForwardRoute implements Route {

    private final Gson gson;
    private final SavedGameList savedGameList;

    public PostReplayForwardRoute(SavedGameList savedGameList, Gson gson){
        this.savedGameList = savedGameList;
        this.gson = gson;
    }

    @Override
    public Object handle(Request request, Response response) {

        final Session httpSession = request.session();
        Integer id = Integer.parseInt(httpSession.attribute("gameId").toString());
        MoveList moveList = savedGameList.getSavedGameList(id);

        if (moveList.forward()) {
            return (gson.toJson(new Message(Message.TYPE.info, "true")));
        }
        return (gson.toJson(new Message(Message.TYPE.error, "You can't go forward, turn back or leave")));
    }
}
