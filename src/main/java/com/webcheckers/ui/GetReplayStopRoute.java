package com.webcheckers.ui;

import com.webcheckers.Appl.SavedGameList;
import spark.*;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.halt;

public class GetReplayStopRoute implements Route {

    private SavedGameList savedGameList;
    private TemplateEngine templateEngine;

    public GetReplayStopRoute(SavedGameList savedGameList, TemplateEngine templateEngine){
        this.templateEngine = templateEngine;
        this.savedGameList = savedGameList;
    }

    @Override
    public Object handle(Request request, Response response) {
        final Map<String, Object> vm = new HashMap<>();

        final Session httpSession = request.session();
        int gameId = httpSession.attribute("gameId");

        savedGameList.resetReplay(gameId);

        /*Send player back to Home screen*/
        response.redirect(WebServer.HOME_URL);
        halt();
        return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
    }
}
