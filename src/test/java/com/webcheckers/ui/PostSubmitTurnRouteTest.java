package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.Appl.GameList;
import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Appl.SavedGameList;
import com.webcheckers.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostSubmitTurnRouteTest {

    private PostSubmitTurnRoute CuT;
    private GameList gameList;
    private PlayerLobby playerLobby;
    private Gson gson;

    private Request mockRequest;
    private Response mockResponse;
    private Session mockSession;
    private String playerName;
    private Player testPlayer;
    private SavedGameList savedGameList;
    private BoardModel model;

    @BeforeEach
    void initializeTest() {
        mockSession = mock(Session.class);
        mockRequest = mock(Request.class);
        mockResponse = mock(Response.class);
        playerName = "testPlayer";
        gameList = mock(GameList.class);
        playerLobby = mock(PlayerLobby.class);
        gson = new Gson();
        CuT = new PostSubmitTurnRoute(gameList, playerLobby, gson);
        testPlayer = new Player(playerName);

        savedGameList = new SavedGameList();
        model = new BoardModel(testPlayer, new Player("a"), null);


        when(playerLobby.getPlayer(playerName)).thenReturn(testPlayer);
        when(mockRequest.session()).thenReturn(mockSession);
        when(mockSession.attribute("id")).thenReturn(playerName);
        when(gameList.getBoardModel(testPlayer)).thenReturn(model);

    }

    @Test
    void successfulSubmitMove() {
        model.addPendingMove(new Move(new Position(2, 3), new Position(3, 4)));
        CuT.handle(mockRequest, mockResponse);
        assertEquals(new Space(4, false, new Piece(Piece.color.RED)),
                model.getSpace(new Position(3, 4)));
    }

    @Test
    void submitAgainstAITest() {
        savedGameList = new SavedGameList();
        model = new BoardModel(new AIPlayer("otherPlayer", -1), testPlayer, savedGameList);

        when(gameList.getBoardModel(testPlayer)).thenReturn(model);

        model.addPendingMove(new Move(new Position(2, 3), new Position(3, 4)));
        CuT.handle(mockRequest, mockResponse);
        assertEquals(new Space(4, false, new Piece(Piece.color.RED)),
                model.getSpace(new Position(3, 4)));
    }

    @Test
    void submitFinishGame() {
        for (int r = 5; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                model.getBoard()[r][c].removePiece();
            }
        }

        model.addPendingMove(new Move(new Position(2, 3), new Position(3, 4)));
        CuT.handle(mockRequest, mockResponse);
        assertEquals(new Space(4, false, new Piece(Piece.color.RED)),
                model.getSpace(new Position(3, 4)));

    }




}