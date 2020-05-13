package com.solution.eventsmanager.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParserImplTest {

    ParserImpl parser = new ParserImpl();

    @Test
    void parse3() {
        String template = "VISA8872 11.05.20 Зачисление {attribute} руб. от отправителя {attribute} Сообщение:";
        String message = "VISA8872 12.05.20 Зачисление 300 руб. от отправителя ДМИТРИЙ Ш.";

        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("300");
        expectedResult.add("ДМИТРИЙ Ш.");
        List<String> actualResult = parser.parse(message);

        assertEquals(expectedResult, actualResult);
    }
}