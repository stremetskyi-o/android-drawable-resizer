package com.github.forsety;

/**
 * Created by Forsety on 28.08.2014.
 */
public class InvalidConfigurationException extends RuntimeException {

    public InvalidConfigurationException() {
    }

    public InvalidConfigurationException(String s) {
        super(addTagToMessage(s));
    }

    public InvalidConfigurationException(String s, Throwable throwable) {
        super(addTagToMessage(s), throwable);
    }

    public InvalidConfigurationException(Throwable throwable) {
        super(throwable);
    }

    public InvalidConfigurationException(String s, Throwable throwable, boolean b, boolean b2) {
        super(addTagToMessage(s), throwable, b, b2);
    }

    private static String addTagToMessage(String input) {
        return "[ADR] " + input;
    }

}
