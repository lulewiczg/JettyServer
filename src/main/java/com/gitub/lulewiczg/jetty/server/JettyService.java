package com.gitub.lulewiczg.jetty.server;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;

import com.gitub.lulewiczg.jetty.exceptions.JettyException;
import com.gitub.lulewiczg.jetty.resource.DefaultMessageHandler;
import com.gitub.lulewiczg.jetty.resource.Message;
import com.gitub.lulewiczg.jetty.server.context.WebAppDeployer;

/**
 * Jetty container service.
 *
 * @author Grzegurz
 */
public class JettyService {

    private volatile Server server;
    private ServerState state = ServerState.STOPPED;
    private DefaultMessageHandler msgHandler;
    private Object lock = new Object();
    private String webappPath;

    public JettyService(DefaultMessageHandler msgHandler, String webappPath) throws Exception {
        this.msgHandler = msgHandler;
        this.webappPath = webappPath;
        server = new Server(8080);

        Configuration.ClassList classlist = Configuration.ClassList.setServerDefault(server);
        classlist.addAfter(FragmentConfiguration.class.getName(), EnvConfiguration.class.getName(),
                PlusConfiguration.class.getName());
        classlist.addBefore(JettyWebXmlConfiguration.class.getName(), AnnotationConfiguration.class.getName());
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
                        deploy();
                        server.start();
                        printMsg(Message.SERVER_START);
                        server.join();
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
                        printMsg(Message.SERVER_STOP);
                        server.join();
                    } catch (Exception e) {
                        throw new JettyException(e);
                    }
                }
            };
            t.start();
        }
    }

    /**
     * Deploys webapps if not yet deployed.
     */
    private void deploy() {
        new WebAppDeployer(server, webappPath).deploy();
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

    public ServerState getState() {
        synchronized (lock) {
            return state;
        }
    }

}
