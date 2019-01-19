package com.gitub.lulewiczg.jetty;

import java.util.Scanner;

import com.gitub.lulewiczg.jetty.resource.DefaultMessageHandler;
import com.gitub.lulewiczg.jetty.server.JettyService;

/**
 * Simple UI implementation.
 *
 * @author Grzegurz
 */
public class Main {

    public static void main(String[] args) throws Exception {
        DefaultMessageHandler msgHandler = new DefaultMessageHandler();
        JettyService jettyService = new JettyService(msgHandler, "test");
        Scanner sc = new Scanner(System.in);
        jettyService.start();
        int read = -1;
        while (read != 3) {
            System.out.println("1- Start server\n2- Stop server\n3- Exit");
            read = sc.nextInt();
            if (read == 1) {
                jettyService.start();
            } else if (read == 2) {
                jettyService.stop();
            }
        }
        sc.close();
    }
}
