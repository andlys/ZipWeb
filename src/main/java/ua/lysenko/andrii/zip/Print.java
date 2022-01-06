package ua.lysenko.andrii.zip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Print {

    static Logger log = LoggerFactory.getLogger(Print.class);

    public static void print(String s) {
        log.info(s);
    }
}
