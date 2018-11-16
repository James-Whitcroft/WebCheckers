package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.Appl.GameList;
import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Model.*;
import spark.*;
import java.util.Objects;
import java.util.logging.Logger;


public class PostCheckTurnRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());
    private final GameList gameList;
    private final PlayerLobby playerLobby;
    private Gson gson;


    /**
     * Create the Spark Route (UI controller) for the
     * {@code GET /} HTTP request.
     *
     * @param gameList
     *  the site wide PlayerLobby
     */
    public PostCheckTurnRoute(GameList gameList, PlayerLobby playerLobby, Gson gson) {
        // validation
        Objects.requireNonNull(gameList, "gameList must not be null");
        //
        this.gameList = gameList;
        this.playerLobby = playerLobby;
        this.gson = gson;
        //
        LOG.config("GetHomeRoute is initialized.");
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
        LOG.finer("PostCheckRoute is invoked.");
        // retrieve the HTTP session
        final Session httpSession = request.session();
        String playerName = httpSession.attribute(PostSignInRoute.SESSION_ATTR);

        Player activePlayer = playerLobby.getPlayer(playerName);

        BoardModel model = gameList.getBoardModel(activePlayer);

        if (model == null) {
            return gson.toJson(new Message(Message.TYPE.error, "Opponent resigned - You won"));
        }

        Player red = model.getRedPlayer();
        Player white = model.getWhitePlayer();

        BoardModel redModel = gameList.getBoardModel(red);
        BoardModel whiteModel = gameList.getBoardModel(white);

        if ((redModel == null && activePlayer.equals(white)) || (whiteModel == null && activePlayer.equals(red))) {
            activePlayer.setInGame(false);
            activePlayer.winGame();
            gameList.removeBoard(activePlayer);
            return gson.toJson(new Message(Message.TYPE.error, "Opponent resigned - You won"));
        }

        if (gameList.getBoardView(activePlayer).getActivePlayer().equals(activePlayer)) {
            return gson.toJson(new Message(Message.TYPE.info, "true"));
        }
        return gson.toJson(new Message(Message.TYPE.info, "Waiting for opponent to play"));
    }

}
