package com.webcheckers.ui;

import com.webcheckers.Appl.PlayerLobby;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import spark.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class PostSignInRouteTest {

    private PostSignInRoute CuT;
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine engine;
    private PlayerLobby playerLobby;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        engine = mock(TemplateEngine.class);
        playerLobby = mock(PlayerLobby.class);
        CuT = new PostSignInRoute(playerLobby, engine);
    }


    @Test
    public void testInvalidSpaceName(){nameCheck("  ");}

    public void nameCheck(String nameTested){
        when(request.queryParams(ArgumentMatchers.eq("name"))).thenReturn(nameTested);
        TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(ArgumentMatchers.any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", "Sign in!");
        testHelper.assertViewModelAttribute("messageType", null);
        testHelper.assertViewModelAttribute("message",null);
        testHelper.assertViewName("signin.ftl");
    }

    @Test
    public void testInvalidPuncName(){nameCheck("..");}

    @Test
    public void testDuplicateName(){
        playerLobby.addPlayer("1a2b");
        when(request.queryParams("name")).thenReturn("1a2b");
        CuT.handle(request, response);
    }

    @Test
    void testValidName(){
        try {
            final TemplateEngineTester testHelper = new TemplateEngineTester();
            when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

            when(request.queryParams("signIn")).thenReturn("red");
            when(session.attribute(PostSignInRoute.SESSION_ATTR)).thenReturn("red");

            when(playerLobby.addPlayer("red")).thenReturn(true);
            CuT.handle(request, response);

        } catch (HaltException err) {
            // pass
        }
        verify(response).redirect(WebServer.HOME_URL);
    }

    @Test
    void testInValidName(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        when(request.queryParams("signIn")).thenReturn("red");
        when(session.attribute(PostSignInRoute.SESSION_ATTR)).thenReturn("red");

        when(playerLobby.addPlayer("red")).thenReturn(false);
        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("nameTaken", true);
        testHelper.assertViewModelAttribute("title","Sign in!");
        testHelper.assertViewName(GetSignInRoute.VIEW_NAME);
    }

}
