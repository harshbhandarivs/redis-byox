package com.harsh.redis.app;

import com.harsh.redis.app.server.ServerThread;
import com.harsh.redis.app.store.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        int portNumber = Integer.parseInt(args[0]);
        DataStore dataStore = new DataStore();
        if (args.length != 1) {
            logger.error("Usage: java App <port number>");
            System.exit(1);
        }
        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            logger.info("REDIS TCP Server started on port {}", portNumber);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ServerThread(clientSocket, dataStore).start();
            }
        } catch (IOException e) {
            logger.error("Error in running program", e);
        }
    }
}
