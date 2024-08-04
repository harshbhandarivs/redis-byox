package com.harsh.redis.app.resp_data_type;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class BulkStringRespToken implements RESPToken {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String getToken() {
        return token;
    }

    private final String token;

    public BulkStringRespToken(Scanner scanner) {
        scanner.next();
        token = scanner.next();
        logger.debug("Received bulk string {}", this);
    }

    public BulkStringRespToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return StringEscapeUtils.escapeJava(respEncode());
    }

    @Override
    public String respEncode() {
        return "$" + token.length() + "\r\n" + token + "\r\n";
    }
}
