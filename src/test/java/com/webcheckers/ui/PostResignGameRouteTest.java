package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.Appl.GameList;
import com.webcheckers.Appl.SavedGameList;
import com.webcheckers.Model.BoardModel;
import com.webcheckers.Model.Message;
import com.webcheckers.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostResignGameRouteTest {

    private PostResignGameRoute CuT;
    private GameList gameList;
    private SavedGameList savedGameList;
    private Gson gson;
    private Response response;
    private Request request;
    private Session session;
    private String playerName = "playerName";
    private BoardModel model;
    private Player redPlayer;
    private Player whitePlayer;

    @BeforeEach
    void initializeTest() {
        gameList = mock(GameList.class);
        savedGameList = mock(SavedGameList.class);
        gson = new Gson();
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        when(session.attribute("id")).thenReturn(playerName);
        model = mock(BoardModel.class);
        redPlayer = new Player(playerName);
        whitePlayer = new Player("whitePlayer");
        when(model.getRedPlayer()).thenReturn(redPlayer);
        when(model.getWhitePlayer()).thenReturn(whitePlayer);
        when(gameList.getBoardModel(any())).thenReturn(model);
        CuT = new PostResignGameRoute(gameList, savedGameList, gson);
    }

    @Test
    void resignTest() {
        assertEquals(gson.toJson(new Message(Message.TYPE.info, "Resigned")),
                CuT.handle(request, response));
    }
}