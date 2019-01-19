package com.gitub.lulewiczg.jetty;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;

import com.gitub.lulewiczg.jetty.exceptions.JettyException;
import com.gitub.lulewiczg.jetty.resource.DefaultMessageHandler;
import com.gitub.lulewiczg.jetty.resource.Message;

/**
 * Jetty container service.
 *
 * @author Grzegurz
 */
public class JettyService {

    private volatile Server server;
    private DefaultMessageHandler msgHandler;
    private Object lock = new Object();

    public JettyService(DefaultMessageHandler msgHandler) throws Exception {
        this.msgHandler = msgHandler;
        server = new Server(8080);

        Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(server);
        classlist.addAfter(FragmentConfiguration.class.getName(), EnvConfiguration.class.getName(),
                PlusConfiguration.class.getName());
        classlist.addBefore(JettyWebXmlConfiguration.class.getName(), AnnotationConfiguration.class.getName());
        // Test WebApp
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/ContentServer");
        webapp.setWar("ContentServer.war");

        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[] { new HomeHandler(), webapp });
        server.setHandler(handlers);
    }

    /**
     * Starts server.
     *
     * @throws Exception
     *             the Exception
     */
    public void start() throws Exception {
        synchronized (lock) {
            if (server.isRunning()) {
                printMsg(Message.ALREADY_RUNNING);
                return;
            }
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        printMsg(Message.SERVER_STARTING);
                        server.start();
                        server.join();
                        printMsg(Message.SERVER_START);
                    } catch (Exception e) {
                        throw new JettyException(e);
                    }
                }
            };
            t.start();
        }
    }

    /**
     * Stops server.
     *
     * @throws Exception
     *             the Exception
     */
    public void stop() throws Exception {
        synchronized (lock) {
            if (!server.isRunning()) {
                printMsg(Message.ALREADY_STOPPED);
                return;
            }
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        printMsg(Message.SERVER_STOPPING);
                        server.stop();
                        server.join();
                        printMsg(Message.SERVER_STOP);
                    } catch (Exception e) {
                        throw new JettyException(e);
                    }
                }
            };
            t.start();
        }
    }

    /**
     * Prints message
     *
     * @param m
     *            message
     */
    protected void printMsg(Message m) {
        System.out.println(msgHandler.getMsg(m));
    }
}
