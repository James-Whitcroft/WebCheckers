package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.Appl.MoveList;
import com.webcheckers.Appl.SavedGameList;
import com.webcheckers.Model.Move;
import com.webcheckers.Model.Piece;
import com.webcheckers.Model.Player;
import com.webcheckers.Model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetReplayGameRouteTest {

    private static final String CURRENT_PLAYER = "currentPlayer";
    private static final String MESSAGE = "message";
    private static final String RED_PLAYER = "redPlayer";
    private static final String WHITE_PLAYER = "whitePlayer";
    private static final String ACTIVE_COLOR = "activeColor";

    private TemplateEngine templateEngine;
    private SavedGameList savedGameList;
    private Gson gson;
    private TemplateEngineTester testHelper;
    private MoveList moveList;
    private String redName = "red";
    private String whiteName = "white";

    private GetReplayGameRoute CuT;

    @BeforeEach
    void initializeTests() {
        templateEngine = mock(TemplateEngine.class);
        savedGameList = mock(SavedGameList.class);
        gson = new Gson();
        testHelper = new TemplateEngineTester();
        moveList = new MoveList(redName, whiteName);
        CuT = new GetReplayGameRoute(savedGameList, gson, templateEngine);
    }

    @Test
    void handleTest() {
        moveList.addMove(new Move(new Position(2, 3), new Position(3, 4)));
        Request request = mock(Request.class);
        Response response = mock(Response.class);
        Session session = mock(Session.class);
        when(request.queryParams("gameId")).thenReturn("0");
        when(savedGameList.getSavedGameList(0)).thenReturn(moveList);
        when(request.session()).thenReturn(session);
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", "Welcome!");
        testHelper.assertViewModelAttribute(MESSAGE, null);
        testHelper.assertViewModelAttribute(CURRENT_PLAYER, new Player(moveList.getRedPlayerName()));
        testHelper.assertViewModelAttribute(RED_PLAYER, new Player(moveList.getRedPlayerName()));
        testHelper.assertViewModelAttribute(WHITE_PLAYER, new Player(moveList.getWhitePlayerName()));
        testHelper.assertViewModelAttribute(ACTIVE_COLOR, Piece.color.RED);
    }
}