package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.Appl.GameList;
import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Model.BoardModel;
import com.webcheckers.Model.Message;
import com.webcheckers.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostCheckTurnRouteTest {

    private String playerName = "whiteName";

    private PostCheckTurnRoute CuT;
    private GameList gameList;
    private PlayerLobby playerLobby;
    private Gson gson;
    private Request request;
    private Session session;
    private Response response;
    private Player player;
    private Player opponentPlayer;
    private BoardModel model;

    @BeforeEach
    void initializeTest() {
        gameList = mock(GameList.class);
        playerLobby = mock(PlayerLobby.class);
        gson = new Gson();
        CuT = new PostCheckTurnRoute(gameList, playerLobby, gson);

        model = mock(BoardModel.class);
        player = mock(Player.class);
        opponentPlayer = mock(Player.class);
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        when(session.attribute("id")).thenReturn(playerName);
        when(playerLobby.getPlayer(playerName)).thenReturn(player);

        when(gameList.getBoardModel(player)).thenReturn(model);
        when(gameList.getBoardModel(opponentPlayer)).thenReturn(model);
        when(model.getWhitePlayer()).thenReturn(player);
        when(model.getRedPlayer()).thenReturn(opponentPlayer);



    }

    @Test
    void boardModelNullTest() {
        model = null;
        when(gameList.getBoardModel(player)).thenReturn(null);
        when(gameList.getBoardModel(opponentPlayer)).thenReturn(null);
        assertEquals(gson.toJson(new Message(Message.TYPE.error, "Opponent resigned - You won")),
                CuT.handle(request, response));
    }

    @Test
    void opponentResignedTest() {
        when(model.getActivePlayer()).thenReturn(opponentPlayer);
        when(gameList.getBoardModel(opponentPlayer)).thenReturn(null);
        assertEquals(gson.toJson(new Message(Message.TYPE.error, "Opponent resigned - You won")),
                CuT.handle(request, response));
    }

    @Test
    void turnChangedTest() {
        BoardView boardView = mock(BoardView.class);

        when(gameList.getBoardView(player)).thenReturn(boardView);
        when(boardView.getActivePlayer()).thenReturn(player);
        assertEquals(gson.toJson(new Message(Message.TYPE.info, "true")),
                CuT.handle(request, response));
    }

    @Test
    void stillWaitingTest() {
        BoardView boardView = mock(BoardView.class);

        when(gameList.getBoardView(player)).thenReturn(boardView);
        when(boardView.getActivePlayer()).thenReturn(opponentPlayer);
        assertEquals(gson.toJson(new Message(Message.TYPE.info, "Waiting for opponent to play")),
                CuT.handle(request, response));
    }
}