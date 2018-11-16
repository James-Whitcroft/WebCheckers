package com.webcheckers.ui;

import com.webcheckers.Appl.GameList;
import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Model.BoardModel;
import com.webcheckers.Model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import static spark.Spark.halt;

public class GetGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    static final String VIEW_NAME = "game.ftl";

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;
    private final GameList gameList;

    /**
     * Create the Spark Route (UI controller) for the
     * {@code GET /} HTTP request.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetGameRoute(GameList gameList, PlayerLobby playerLobby, final TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.gameList = gameList;
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
        //
        LOG.config("GetGameRoute is initialized.");
    }

    /**
     * @// TODO: 10/12/2018 error message
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetGameRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Game Time!");
        // retrieve the HTTP session
        final Session httpSession = request.session();
        String playerName = httpSession.attribute(PostSignInRoute.SESSION_ATTR);
        Player white = playerLobby.getPlayer(request.queryParams("white"));
        Player red = playerLobby.getPlayer(playerName);


        if(red == null || white == null || white.isInGame() || red.isInGame()) {
            response.redirect(WebServer.MESSAGE_URL);
            halt();
            return null;
        }

        BoardModel newGame = new BoardModel(white, red);
        gameList.addGame(newGame);
        white.setInGame(true);
        red.setInGame(true);

        response.redirect(WebServer.BOARD_URL);
        halt();
        return null;
    }

}
