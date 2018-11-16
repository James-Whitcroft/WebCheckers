package com.webcheckers.ui;
import com.google.gson.Gson;
import com.webcheckers.Appl.GameList;
import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Model.*;
import spark.*;

public class PostBackUpMoveRoute implements Route {

    private final GameList gameList;
    private final PlayerLobby playerLobby;
    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) for the
     * {@code GET /} HTTP request.
     *
     *   the HTML template rendering engine
     */
    public PostBackUpMoveRoute(GameList gameList, PlayerLobby playerLobby, Gson gson) {
        //
        this.gameList = gameList;
        this.playerLobby = playerLobby;
        this.gson = gson;
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object handle(Request request, Response response) {
        // retrieve the HTTP session
        final Session httpSession = request.session();
        String playerName = httpSession.attribute(PostSignInRoute.SESSION_ATTR);
        Player currentPlayer = playerLobby.getPlayer(playerName);

        if (gameList.getBoardModel(currentPlayer).backUpMove()) {

            return (gson.toJson(new Message(Message.TYPE.info, "Back dat piece up, yo.")));
        } else {
            return (gson.toJson(new Message(Message.TYPE.error, "No move to back up.")));
        }
    }
}
