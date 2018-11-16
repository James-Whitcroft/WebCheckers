package com.webcheckers.ui;

import com.webcheckers.Appl.GameList;
import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
class GetGameRouteTest {

    private GetGameRoute gameRouteTest;

    private TemplateEngine template;
    private PlayerLobby lobby;
    private GameList list;

    private Request request;
    private Response response;
    private Session session;

    @BeforeEach
    void testSetup() {

        template = mock(TemplateEngine.class);
        lobby = mock(PlayerLobby.class);
        list = mock(GameList.class);

        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);

        when(request.session()).thenReturn(session);
        gameRouteTest = new GetGameRoute(list, lobby, template);
    }

    @Test
    void boardTest() {

        try {
            final TemplateEngineTester testHelper = new TemplateEngineTester();
            when(template.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

            when(request.queryParams("white")).thenReturn("white");
            when(session.attribute(PostSignInRoute.SESSION_ATTR)).thenReturn("red");

            final Player red = new Player("red");
            final Player white = new Player("white");
            when(lobby.getPlayer("red")).thenReturn(red);
            when(lobby.getPlayer("white")).thenReturn(white);

            assertNotNull(red);
            assertNotNull(white);
            red.setInGame(false);
            white.setInGame(false);
            assertFalse(white.isInGame());
            assertFalse(red.isInGame());

            gameRouteTest.handle(request, response);

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            testHelper.assertViewModelAttribute("title", "Game Time!");

        } catch (HaltException err) {
            // pass
        }

        verify(response).redirect(WebServer.BOARD_URL);
    }

    @Test
    void messageTest() {

        try {
            final TemplateEngineTester testHelper = new TemplateEngineTester();
            when(template.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

            when(request.queryParams("white")).thenReturn("white");
            when(session.attribute(PostSignInRoute.SESSION_ATTR)).thenReturn("red");

            final Player red = new Player("red");
            final Player white = new Player("white");
            when(lobby.getPlayer("red")).thenReturn(red);
            when(lobby.getPlayer("white")).thenReturn(white);

            assertNotNull(red);
            assertNotNull(white);
            red.setInGame(true);
            white.setInGame(false);

            gameRouteTest.handle(request, response);

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            testHelper.assertViewModelAttribute("title", "Game Time!");

        } catch (HaltException err) {
            // pass
        }

        verify(response).redirect(WebServer.MESSAGE_URL);
    }

    @Test
    void nullPlayerTest() {

        try {
            final TemplateEngineTester testHelper = new TemplateEngineTester();
            when(template.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

            when(request.queryParams("white")).thenReturn("white");
            when(session.attribute(PostSignInRoute.SESSION_ATTR)).thenReturn("red");

            final Player red = new Player("red");
            final Player white = new Player("white");
            when(lobby.getPlayer("red")).thenReturn(null);
            when(lobby.getPlayer("white")).thenReturn(white);

            red.setInGame(true);
            white.setInGame(true);

            gameRouteTest.handle(request, response);

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            testHelper.assertViewModelAttribute("title", "Game Time!");

        } catch (HaltException err) {
            // pass
        }

        verify(response).redirect(WebServer.MESSAGE_URL);
    }
    @Test
    void nullPlayerTwoTest() {

        try {
            final TemplateEngineTester testHelper = new TemplateEngineTester();
            when(template.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

            when(request.queryParams("white")).thenReturn("white");
            when(session.attribute(PostSignInRoute.SESSION_ATTR)).thenReturn("red");

            final Player red = new Player("red");
            final Player white = new Player("white");
            when(lobby.getPlayer("red")).thenReturn(red);
            when(lobby.getPlayer("white")).thenReturn(null);

            red.setInGame(true);
            white.setInGame(true);

            gameRouteTest.handle(request, response);

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            testHelper.assertViewModelAttribute("title", "Game Time!");

        } catch (HaltException err) {
            // pass
        }

        verify(response).redirect(WebServer.MESSAGE_URL);
    }
    @Test
    void playerInGameTwoTest() {

        try {
            final TemplateEngineTester testHelper = new TemplateEngineTester();
            when(template.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

            when(request.queryParams("white")).thenReturn("white");
            when(session.attribute(PostSignInRoute.SESSION_ATTR)).thenReturn("red");

            final Player red = new Player("red");
            final Player white = new Player("white");
            when(lobby.getPlayer("red")).thenReturn(red);
            when(lobby.getPlayer("white")).thenReturn(null);

            red.setInGame(false);
            white.setInGame(true);

            gameRouteTest.handle(request, response);

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            testHelper.assertViewModelAttribute("title", "Game Time!");

        } catch (HaltException err) {
            // pass
        }

        verify(response).redirect(WebServer.MESSAGE_URL);
    }

    @Test
    void allTest() {

        try {
            final TemplateEngineTester testHelper = new TemplateEngineTester();
            when(template.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

            when(request.queryParams("white")).thenReturn("white");
            when(session.attribute(PostSignInRoute.SESSION_ATTR)).thenReturn("red");

            final Player red = new Player("red");
            final Player white = new Player("white");
            when(lobby.getPlayer("red")).thenReturn(null);
            when(lobby.getPlayer("white")).thenReturn(null);

            red.setInGame(true);
            white.setInGame(true);

            gameRouteTest.handle(request, response);

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            testHelper.assertViewModelAttribute("title", "Game Time!");

        } catch (HaltException err) {
            // pass
        }

        verify(response).redirect(WebServer.MESSAGE_URL);
    }
}