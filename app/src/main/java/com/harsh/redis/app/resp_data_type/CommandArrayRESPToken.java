package com.harsh.redis.app.resp_data_type;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CommandArrayRESPToken implements RESPToken {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BulkStringRespToken[] respTokens;

    public CommandArrayRESPToken(Scanner scanner) throws IOException {
        String start = scanner.next();
        String arrayLength = start.substring(1);
        int numberOfArguments = Integer.parseInt(arrayLength);
        respTokens = new BulkStringRespToken[numberOfArguments];
        for (int index = 0; index < numberOfArguments; index++) {
            respTokens[index] = new BulkStringRespToken(scanner);
        }
        logger.debug("Received array {}", this);
    }

    public String toString() {
        return StringEscapeUtils.escapeJava(respEncode());
    }

    public String getCommand() {
        return respTokens[0].getToken();
    }

    public String getKey() {
        return respTokens[1].getToken();
    }

    public String getValue() {
        return respTokens[2].getToken();
    }

    public String respEncode() {
        return "*"
                + respTokens.length
                + "\r\n"
                + Arrays.stream(respTokens)
                .map(RESPToken::respEncode)
                .collect(Collectors.joining(""));
    }
}
