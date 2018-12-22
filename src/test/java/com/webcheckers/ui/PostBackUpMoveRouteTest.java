package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.Appl.GameList;
import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Appl.SavedGameList;
import com.webcheckers.Model.BoardModel;
import com.webcheckers.Model.Message;
import com.webcheckers.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PostBackUpMoveRouteTest {

    private final String playerName = "red";
    private GameList gameList;
    private PlayerLobby playerLobby;
    private Gson gson;
    private PostBackUpMoveRoute CuT;
    private Session session;
    private Request request;
    private Response response;
    private Player player;
    private BoardModel model;

    @BeforeEach
    public void setUp(){
        gameList = mock(GameList.class);
        Player white = mock(Player.class);
        player = mock(Player.class);
        SavedGameList savedGameList = mock(SavedGameList.class);
        model = mock(BoardModel.class);//new BoardModel(white,player,savedGameList);
        //gameList.addGame(model);
        playerLobby = mock(PlayerLobby.class);
        gson = new Gson();
        request = mock(Request.class);
        response = mock(Response.class);
        session  = mock(Session.class);
        CuT = new PostBackUpMoveRoute(gameList,playerLobby,gson);
        when(request.session()).thenReturn(session);
        when(session.attribute("id")).thenReturn("player");
        when(playerLobby.getPlayer("player")).thenReturn(player);


    }

    @Test
    public void testBackUpTrue(){
        Session httpSession = request.session();
        String playerName = httpSession.attribute("id");
        Player currentPlayer = playerLobby.getPlayer(playerName);
        doReturn(model).when(gameList).getBoardModel(currentPlayer);
        when(model.backUpMove()).thenReturn(true);
        assertEquals(gson.toJson(new Message(Message.TYPE.info, "Back dat piece up, yo.")),
                CuT.handle(request, response));
    }

    @Test
    public void testBackUpFalse(){
        Session httpSession = request.session();
        String playerName = httpSession.attribute("id");
        Player currentPlayer = playerLobby.getPlayer(playerName);
        doReturn(model).when(gameList).getBoardModel(currentPlayer);
        when(model.backUpMove()).thenReturn(false);
        assertEquals(gson.toJson(new Message(Message.TYPE.error, "No move to back up.")),
                CuT.handle(request, response));
    }
}
