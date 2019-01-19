package com.gitub.lulewiczg.jetty.server.context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Searches webapps and deploys them.
 *
 * @author Grzegurz
 */
public class WebAppDeployer {
    private static final String WAR = ".war";
    private Server server;
    private String webappPath;
    private List<Handler> toRedeploy = new ArrayList<>();

    public WebAppDeployer(Server server, String webappPath) {
        this.server = server;
        this.webappPath = webappPath;
    }

    /**
     * Deploys all contexts.
     */
    public void deploy() {
        File path = getPath();
        File[] contexts = path.listFiles();
        List<Handler> handlers = new ArrayList<>();
        handlers.add(new HomeHandler());
        for (File f : contexts) {
            String name = f.getName();
            if (f.isDirectory()) {
                WebAppContext webapp = createDirHandler(path, name);
                handlers.add(webapp);
            } else if (name.toLowerCase().endsWith(WAR)) {
                WebAppContext webapp = createWarHandler(path, name);
                handlers.add(webapp);
                toRedeploy.add(webapp);
            }
        }
        HandlerCollection handlerCollection = new HandlerCollection();
        handlerCollection.setHandlers(handlers.toArray(new Handler[] {}));
        server.setHandler(handlerCollection);
    }

    /**
     * Creates handler for WAR.
     *
     * @param path
     *            path
     * @param name
     *            app name
     * @return handler
     */
    private WebAppContext createWarHandler(File path, String name) {
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/" + name.replace(WAR, ""));
        webapp.setWar(path.getName() + File.separator + name);
        return webapp;
    }

    /**
     * Creates handler for directory.
     *
     * @param path
     *            path
     * @param name
     *            app name
     * @return handler
     */
    private WebAppContext createDirHandler(File path, String name) {
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/" + name);
        webapp.setResourceBase(path.getName() + File.separator + name);
        return webapp;
    }

    /**
     * Gets path.
     *
     * @return path
     */
    private File getPath() {
        File path = new File(webappPath);
        if (!path.exists()) {
            path.mkdirs();
        }
        return path;
    }
}
