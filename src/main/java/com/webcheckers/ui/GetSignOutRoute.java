package com.webcheckers.ui;

import com.webcheckers.Appl.PlayerLobby;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static spark.Spark.halt;

public class GetSignOutRoute implements Route {


    //
    // Constants
    //

    static final String SESSION_ATTR = "id";
    //
    // Attributes
    //

    private final PlayerLobby playerLobby;
    private final TemplateEngine templateEngine;

    //
    // Constructor
    //

    /**
     * The constructor for the {@code POST /guess} route handler.
     *
     * @param templateEngine
     *    template engine to use for rendering HTML page
     *
     * @throws NullPointerException
     *    when the {@code playerLobby} or {@code templateEngine} parameter is null
     */
    GetSignOutRoute(PlayerLobby playerLobby, TemplateEngine templateEngine) {
        // validation
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
    }

    //
    // TemplateViewRoute method
    //

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public String handle(Request request, Response response) {
        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();
        // retrieve the HTTP session
        final Session httpSession = request.session();
        // remove player from lobby and free up player name
        playerLobby.removePlayer(httpSession.attribute(SESSION_ATTR));
        // Clear session attribute
        httpSession.attribute(SESSION_ATTR, null);
        response.redirect(WebServer.HOME_URL);
        halt();
        return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
    }

}
