package com.transport.liby.service;


import org.springframework.context.MessageSource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Message {
    private static MessageSource messageSource;

    public Message(MessageSource messageSource) {
        Message.messageSource = messageSource;
    }

    public static String get( @NonNull String property){
        return get(property, new Locale("en"));
    }

    public static String get( @NonNull String property, @NonNull Locale locale){
        return Message.messageSource.getMessage(property, null, locale);
    }
}
