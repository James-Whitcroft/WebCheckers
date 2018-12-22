package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.Appl.MoveList;
import com.webcheckers.Appl.SavedGameList;
import com.webcheckers.Model.Move;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostReplayBackwardRouteTest {

    private PostReplayBackwardRoute RuT;
    private Request request;
    private Session session;
    private Response response;
    private SavedGameList savedGameList;
    private Gson gson;
    private MoveList moveList;
    private Move move;

    @BeforeEach
    void initializeTest() {
        session = mock(Session.class);
        request = mock(Request.class);
        response = mock(Response.class);
        moveList = new MoveList("red", "white");
        move = mock(Move.class);
        moveList.addMove(move);
        savedGameList = mock(SavedGameList.class);
        gson = new Gson();
        RuT = new PostReplayBackwardRoute(savedGameList, gson);

        when(request.session()).thenReturn(session);
        when(session.attribute("gameId")).thenReturn(0);
        when(savedGameList.getSavedGameList(0)).thenReturn(moveList);
    }

    @Test
    void moveBackwardTest() {
        moveList.addMove(move);
        moveList.forward();
        moveList.forward();
        when(savedGameList.getSavedGameList(0)).thenReturn(moveList);
        RuT.handle(request, response);
        assertTrue(moveList.backward());
    }

    @Test
    void noMoveBackWardTest() {
        when(savedGameList.getSavedGameList(0)).thenReturn(moveList);
        RuT.handle(request, response);
        assertFalse(moveList.backward());
    }
}