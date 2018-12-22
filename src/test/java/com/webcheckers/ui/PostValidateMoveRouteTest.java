package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.Appl.GameList;
import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostValidateMoveRouteTest {

    private PostValidateMoveRoute CuT;
    private PlayerLobby playerLobby;
    private GameList gameList;
    private Gson gson;
    private Request request;
    private Response response;
    private Session session;
    private String playerName = "playerName";
    private Player player;
    private BoardModel model;

    @BeforeEach
    void initializeTest() {
        playerLobby = mock(PlayerLobby.class);
        gameList = mock(GameList.class);
        gson = new Gson();
        CuT = new PostValidateMoveRoute(playerLobby, gameList, gson);
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);
        player = mock(Player.class);
        model = new BoardModel(new Player("whitePlayer"), player, null);
        when(request.session()).thenReturn(session);
        when(session.attribute("id")).thenReturn(playerName);
        when(playerLobby.getPlayer(playerName)).thenReturn(player);
        when(gameList.getBoardModel(player)).thenReturn(model);
    }

    @Test
    void validMove() {
        Move move = new Move(new Position(2, 3), new Position(3, 4));
        when(request.body()).thenReturn(gson.toJson(move));

        assertEquals(gson.toJson(new Message(Message.TYPE.info, "Valid move")),
                CuT.handle(request, response));
    }

    @Test
    void invalidMove() {
        Move move = new Move(new Position(2, 3), new Position(3, 6));
        when(request.body()).thenReturn(gson.toJson(move));

        assertEquals(gson.toJson(new Message(Message.TYPE.error, "Invalid move buckaroo")),
                CuT.handle(request, response));
    }
}