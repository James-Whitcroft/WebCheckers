package com.webcheckers.ui;

import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Appl.SavedGameList;
import com.webcheckers.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
class GetHomeRouteTest {

    private GetHomeRoute CuT;

    //mock objects
    private PlayerLobby playerLobby;
    private TemplateEngine templateEngine;
    private Player player;
    private Session session;
    private Request request;
    private Response response;
    private SavedGameList savedGameList;

    /**
     * Setup mock objects prior to each test
     */
    @BeforeEach
    public void setup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        templateEngine = mock(TemplateEngine.class);
        playerLobby = mock(PlayerLobby.class);
        player = mock(Player.class);
        savedGameList = mock(SavedGameList.class);


        when(session.attribute("id")).thenReturn("Bob");
        when(playerLobby.getPlayer(session.attribute("id"))).thenReturn(player);

        //create new GetHomeRoute for each test.
        CuT = new GetHomeRoute(playerLobby, templateEngine, savedGameList);
    }

    @Test
    void new_session() {
        //
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //invoke the test
        CuT.handle(request, response);

        //analysis of results
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        testHelper.assertViewModelAttribute(GetHomeRoute.ACTIVE_PLAYERS, playerLobby.getActivePlayers());
        testHelper.assertViewModelAttribute(GetHomeRoute.ACTIVE_PLAYER_COUNT, playerLobby.getActivePlayers().size());
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYER_NAME, session.attribute(PostSignInRoute.SESSION_ATTR));
        testHelper.assertViewModelAttribute(GetHomeRoute.SIGNED_IN, true);

        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
    }

}