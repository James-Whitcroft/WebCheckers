package com.webcheckers.ui;


import com.webcheckers.Appl.PlayerLobby;
import com.webcheckers.Model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.logging.Logger;

import static com.webcheckers.ui.GetSignOutRoute.SESSION_ATTR;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class GetSignOutRouteTest {

    private GetSignOutRoute CuT;

    //mock objects
    private PlayerLobby playerLobby;
    private TemplateEngine templateEngine;
    private Session session;
    private Request request;
    private Response response;
    private Logger LOG;
    private Player player;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerLobby = mock(PlayerLobby.class);
        templateEngine = mock(TemplateEngine.class);
        LOG = Logger.getLogger(GetHomeRoute.class.getName());
        player = mock(Player.class);

        //create new GetSignInRoute for each test.
        when(session.attribute("id")).thenReturn("test");
        when(playerLobby.getPlayer(session.attribute("id"))).thenReturn(player);
        CuT = new GetSignOutRoute(playerLobby, templateEngine);
    }

    @Test
    public void testHandle(){
        try {
            final TemplateEngineTester testHelper = new TemplateEngineTester();
            when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
            when(session.attribute(SESSION_ATTR)).thenReturn("red");

            CuT.handle(request, response);

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();

            assertNull(playerLobby.getPlayer("test"));
            testHelper.assertViewModelAttribute(SESSION_ATTR, false);

        } catch (HaltException err) {
            // pass
        }

        verify(response).redirect(WebServer.HOME_URL);

    }

}
