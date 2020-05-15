package com.solution.eventsmanager.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserImpl {
    Logger logger = LoggerFactory.getLogger(ParserImpl.class);

    public List<String> parse(String message) {
        logger.info("parse(): message = " + message);
        message = message.replaceAll("\"", "");
        logger.info("parse(): message = " + message);

        int idx1, idx2;

        idx1 = message.indexOf(" ", message.indexOf("Зачисление"));
        idx2 = message.indexOf(" ", idx1+1);
        logger.info("parse(): idx1 = " + idx1);
        logger.info("parse(): idx2 = " + idx2);

        List<String> attributeValues = new ArrayList<>();
        attributeValues.add(message.substring(idx1+1, idx2));
        logger.info("parse(): attributeValues = " + attributeValues);

        idx1 = message.indexOf(" ", message.indexOf("отправителя"));
        attributeValues.add(message.substring(idx1+1));
        logger.info("parse(): idx1 = " + idx1);
        logger.info("parse(): attributeValues = " + attributeValues);

        return attributeValues;
    }
}
