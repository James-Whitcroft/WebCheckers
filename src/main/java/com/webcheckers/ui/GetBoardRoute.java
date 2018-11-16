package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.Appl.GameList;
import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Model.*;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;


public class GetBoardRoute implements Route {

    private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

    private static final String CURRENT_PLAYER = "currentPlayer";
    private static final String VIEW_MODE = "viewMode";
    private static final String RED_PLAYER = "redPlayer";
    private static final String WHITE_PLAYER = "whitePlayer";
    private static final String ACTIVE_COLOR = "activeColor";
    private static final String BOARD_VIEW = "board";
    private static final String MESSAGE = "message";

    private enum Modes { PLAY, SPECTATOR, REPLAY }



    private final TemplateEngine templateEngine;
    private final GameList gameList;
    private final PlayerLobby playerLobby;
    private final Gson gson;

    /**
     * Create the Spark Route (UI controller) for the
     * {@code GET /} HTTP request.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetBoardRoute(GameList gameList, PlayerLobby playerLobby, final TemplateEngine templateEngine, Gson gson) {
        // validation
        Objects.requireNonNull(gameList, "gameList must not be null");
        Objects.requireNonNull(playerLobby, "playerLobby must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        //
        this.gameList = gameList;
        this.playerLobby = playerLobby;
        this.templateEngine = templateEngine;
        this.gson = gson;
        //
        LOG.config("GetGameRoute is initialized.");
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetBoardRoute is invoked.");
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Game Time!");
        // retrieve the HTTP session
        final Session httpSession = request.session();
        String playerName = httpSession.attribute(PostSignInRoute.SESSION_ATTR);
        Player currentPlayer = playerLobby.getPlayer(playerName);


        if (!currentPlayer.isInGame()) {
            if (gameList.gameExists(playerName)) {
                Player red = gameList.getBoardModel(currentPlayer).getRedPlayer();
                Player white = gameList.getBoardModel(currentPlayer).getWhitePlayer();
                gameList.removeBoard(red);
                gameList.removeBoard(white);
            }
            response.redirect(WebServer.HOME_URL);
        }

        BoardModel model = gameList.getBoardModel(currentPlayer);
        if (currentPlayer == model.getActivePlayer()) {
            model.resetPendingMoves();
        }
        BoardView board = gameList.getBoardView(currentPlayer);

        vm.put(MESSAGE, null);


        //Checks if there are any valid moves left.
        if (!MoveValidator.teamHasMove(model, model.getRedPlayer().equals(currentPlayer) ? Piece.color.RED : Piece.color.WHITE)) {
            vm.put(MESSAGE, new Message(Message.TYPE.error, "No Valid Moves. You Lose."));
            currentPlayer.setInGame(false);
        }

        // Checks if there is a winner by capturing all pieces
        if (model.getWinner() != null) {
            vm.put(MESSAGE, new Message(Message.TYPE.error, "Game Over. " + model.getWinner().getName() + " has won."));
            currentPlayer.setInGame(false);
        }

        if (!currentPlayer.isInGame()) {
            Player red = gameList.getBoardModel(currentPlayer).getRedPlayer();
            Player white = gameList.getBoardModel(currentPlayer).getWhitePlayer();
            gameList.removeBoard(red);
            gameList.removeBoard(white);
        }

        vm.put(CURRENT_PLAYER, currentPlayer);
        vm.put(VIEW_MODE, Modes.PLAY);
        vm.put(RED_PLAYER, board.getRedPlayer());
        vm.put(WHITE_PLAYER, board.getWhitePlayer());
        vm.put(ACTIVE_COLOR, board.getActiveColor());
        vm.put(BOARD_VIEW, board);

        return templateEngine.render(new ModelAndView(vm , GetGameRoute.VIEW_NAME));
    }
}
