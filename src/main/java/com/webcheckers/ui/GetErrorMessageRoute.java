package com.webcheckers.ui;

import com.webcheckers.Model.Message;
import spark.*;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.halt;

public class GetErrorMessageRoute implements Route {

    private final TemplateEngine templateEngine;

    static final String SESSION_ATTR = "id";

    /**
     * Create GET /errorMessage route
     * @param templateEngine current templateEngine
     */
    public GetErrorMessageRoute(final TemplateEngine templateEngine){
        this.templateEngine = templateEngine;
    }

    /**
     * Handle Error Message Occurance
     * @param request http request
     * @param response http response
     * @return render html for home page
     */
    @Override
    public Object handle(Request request, Response response) {
        // start building the View-Model
        final Map<String, Object> vm = new HashMap<>();

        Message message = new Message(Message.TYPE.error);
        vm.put("message", message.getText());
        response.redirect(WebServer.HOME_URL);
        halt();
        return templateEngine.render(new ModelAndView(vm, GetHomeRoute.VIEW_NAME));
    }
}
