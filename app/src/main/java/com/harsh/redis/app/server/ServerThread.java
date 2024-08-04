package com.harsh.redis.app.server;

import com.harsh.redis.app.resp_data_type.BulkStringRespToken;
import com.harsh.redis.app.resp_data_type.CommandArrayRESPToken;
import com.harsh.redis.app.store.DataStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ServerThread extends Thread {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Socket socket;
    private final DataStore dataStore;

    public ServerThread(Socket socket, DataStore dataStore) {
        this.socket = socket;
        this.dataStore = dataStore;
    }

    public void run() {
        logger.debug("Socket connection established with {}", socket.getRemoteSocketAddress());
        handle();
    }

    private void handle() {
        try (
                InputStream inputStream = socket.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                OutputStream outputStream = socket.getOutputStream()
        ) {
            scanner.useDelimiter("\r\n");
            while (true) {
                CommandArrayRESPToken commandArrayRESPToken = new CommandArrayRESPToken(scanner);
                String command = commandArrayRESPToken.getCommand();
                if (command.equalsIgnoreCase("PING")) {
                    outputStream.write("+PONG\r\n".getBytes());
                } else if (command.equalsIgnoreCase("SET")) {
                    String key = commandArrayRESPToken.getKey();
                    String value = commandArrayRESPToken.getValue();
                    dataStore.put(key, value);
                    outputStream.write("$2\r\nOK\r\n".getBytes());
                } else if (command.equalsIgnoreCase("GET")) {
                    String key = commandArrayRESPToken.getKey();
                    String value = dataStore.get(key);
                    logger.debug("Got value {}", value);
                    if (value == null) {
                        outputStream.write("_\r\n".getBytes());
                    } else {
                        BulkStringRespToken bulkStringRespToken = new BulkStringRespToken(value);
                        outputStream.write(bulkStringRespToken.respEncode().getBytes());
                    }
                } else {
                    outputStream.write("+PONG\r\n".getBytes());
                }
            }
        } catch (IOException e) {
            logger.error("Error in running AppThread", e);
        } catch (NoSuchElementException e) {
            logger.info("Client {} closed connection", socket.getRemoteSocketAddress());
        } finally {
            try {
                socket.close();
                logger.info("Socket connection closed with {}", socket.getRemoteSocketAddress());
            } catch (IOException e) {
                logger.debug("Error while closing connection", e);
            }
        }
    }
}
