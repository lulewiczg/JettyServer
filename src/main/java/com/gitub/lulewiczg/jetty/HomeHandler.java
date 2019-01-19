package com.gitub.lulewiczg.jetty;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerWrapper;

public class HomeHandler extends HandlerWrapper {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        if (response.isCommitted() || baseRequest.isHandled())
            return;

        baseRequest.setHandled(true);
        if (!request.getMethod().equals(HttpMethod.GET.asString()) || !request.getRequestURI().equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append("<html>\n<head>\n<title>Jetty");
        builder.append("</title>\n<body>\n<h2>Welcome to jetty</h2>\n");
        builder.append("<p>Jetty is running successfully</p>");

        Server server = getServer();
        Handler[] handlers = server == null ? null : server.getChildHandlersByClass(ContextHandler.class);

        if (handlers.length == 0) {
            builder.append("<p>No webapps deployed</p>");
        } else {
            builder.append("<p>Available webapps:</p>");
        }
        List<ContextHolder> holders = new ArrayList<>();
        for (Handler h : handlers) {
            ContextHandler context = (ContextHandler) h;
            ContextHolder contextHolder = new ContextHolder(context);
            holders.add(contextHolder);
        }
        builder.append(ContextHolder.toHMTL(holders));
        builder.append("\n</body>\n</html>\n");
        response.setStatus(HttpStatus.OK_200);
        response.setContentLength(builder.length());
        response.setContentType("text/html");

        try (OutputStream out = response.getOutputStream()) {
            out.write(builder.toString().getBytes());
        }
    }

}
