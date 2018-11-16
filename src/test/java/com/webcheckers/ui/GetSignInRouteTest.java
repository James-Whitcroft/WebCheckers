package com.webcheckers.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-tier")
public class GetSignInRouteTest {

    private GetSignInRoute CuT;

    //mock objects
    private TemplateEngine templateEngine;
    private Session session;
    private Request request;
    private Response response;
    private Logger LOG;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        templateEngine = mock(TemplateEngine.class);
        LOG = Logger.getLogger(GetHomeRoute.class.getName());

        //create new GetSignInRoute for each test.
        CuT = new GetSignInRoute(templateEngine);
    }

    @Test
    void testHandle() {
        LOG.config("GetSignInRoute is initialized.");

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("title", "Sign In!");
        testHelper.assertViewName(GetSignInRoute.VIEW_NAME);
    }
}