package com.webcheckers.ui;

import com.webcheckers.Appl.SavedGameList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.*;

import static com.webcheckers.ui.GetSignOutRoute.SESSION_ATTR;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetReplayStopRouteTest {

    private SavedGameList savedGameList;
    private TemplateEngine templateEngine;
    private Request request;
    private Response response;
    private GetReplayStopRoute CuT;
    private Session session;

    @BeforeEach
    public void setUp(){
        request = mock(Request.class);
        response = mock(Response.class);
        session = mock(Session.class);
        savedGameList = mock(SavedGameList.class);
        templateEngine = mock(TemplateEngine.class);
        CuT = new GetReplayStopRoute(savedGameList, templateEngine);
        when(request.session()).thenReturn(session);
        when(session.attribute("gameId")).thenReturn("test");
    }

    @Test
    public void testHandle(){
        try {
            final TemplateEngineTester testHelper = new TemplateEngineTester();
            when(templateEngine.render((ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
            when(session.attribute("gameId")).thenReturn(0);

            CuT.handle(request, response);

            testHelper.assertViewModelExists();
            testHelper.assertViewModelIsaMap();
            testHelper.assertViewModelAttribute(SESSION_ATTR, false);
            testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
        } catch (HaltException err) {
            // pass
        }
    }

}
