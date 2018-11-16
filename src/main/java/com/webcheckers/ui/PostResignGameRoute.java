package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.Appl.GameList;
import com.webcheckers.Model.Message;
import com.webcheckers.Model.Player;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.Objects;

public class PostResignGameRoute implements Route {

    private final GameList gameList;
    private Gson gson;


    /**
     * Create the Spark Route (UI controller) for the
     * {@code GET /} HTTP request.
     *
     * @param gameList
     *  the site wide PlayerLobby
     */
    public PostResignGameRoute(GameList gameList, Gson gson) {
        // validation
        Objects.requireNonNull(gameList, "gameList must not be null");
        //
        this.gameList = gameList;
        this.gson = gson;
        //
    }

    /**
     * Render the WebCheckers Home page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Home page
     */
    @Override
    public Object handle(Request request, Response response) {
        // retrieve the HTTP session
        final Session httpSession = request.session();
        String playerName = httpSession.attribute(PostSignInRoute.SESSION_ATTR);
        Player test = new Player(playerName);

        Player red = gameList.getBoardModel(test).getRedPlayer();
        Player white = gameList.getBoardModel(test).getWhitePlayer();

        if (test.equals(red)) {
            red.setInGame(false);
        } else {
            white.setInGame(false);
        }

        gameList.removeBoard(red.equals(test) ? red : white);

        return gson.toJson(new Message(Message.TYPE.info, "Resigned"));
    }
}
