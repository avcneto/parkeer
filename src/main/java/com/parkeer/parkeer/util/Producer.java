package com.parkeer.parkeer.util;

import lombok.extern.slf4j.Slf4j;

import static java.lang.String.format;

@Slf4j
public class Producer {

    public static <T> void producerMessageConsole(T objectMessage, String topName) {
        log.info(format(topName, objectMessage));
    }
}
