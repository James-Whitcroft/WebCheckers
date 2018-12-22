package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.Appl.BuildReplayBoard;
import com.webcheckers.Appl.MoveList;
import com.webcheckers.Appl.SavedGameList;
import com.webcheckers.Model.Player;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetReplayGameRoute implements Route {

    private static final String CURRENT_PLAYER = "currentPlayer";
    private static final String VIEW_MODE = "viewMode";
    private static final String BOARD_VIEW = "board";
    private static final String MESSAGE = "message";
    private static final String RED_PLAYER = "redPlayer";
    private static final String WHITE_PLAYER = "whitePlayer";
    private static final String ACTIVE_COLOR = "activeColor";

    private final TemplateEngine templateEngine;
    private final SavedGameList savedGameList;
    private final Gson gson;

    private enum Modes { PLAY, SPECTATOR, REPLAY }

    public GetReplayGameRoute (SavedGameList savedGameList, Gson gson, final TemplateEngine templateEngine){

        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        this.templateEngine = templateEngine;
        this.savedGameList = savedGameList;
        this.gson = gson;
    }

    @Override
    public Object handle(Request request, Response response) {
        Map<String, Object> vm = new HashMap<>();


        int gameId = Integer.parseInt(request.queryParams("gameId"));
        MoveList moveList = savedGameList.getSavedGameList(gameId);

        final Session httpSession = request.session();
        httpSession.attribute("gameId", moveList.getGameID());
        BoardView board = BuildReplayBoard.buildBoard(moveList);

        Map<String, Object> modeOptions = new HashMap<>();

        modeOptions.put("hasNext", moveList.hasNext());
        modeOptions.put("hasPrevious", moveList.hasPrevious());


        vm.put("title", "Welcome!");
        vm.put(MESSAGE, null);
        vm.put(CURRENT_PLAYER, new Player(moveList.getRedPlayerName()));
        vm.put(RED_PLAYER, new Player(moveList.getRedPlayerName()));
        vm.put(WHITE_PLAYER, new Player(moveList.getWhitePlayerName()));
        vm.put(VIEW_MODE, GetReplayGameRoute.Modes.REPLAY);
        vm.put(ACTIVE_COLOR, board.getActiveColor());
        vm.put(BOARD_VIEW, board);
        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));

        return templateEngine.render(new ModelAndView(vm , GetGameRoute.VIEW_NAME));
    }
}
