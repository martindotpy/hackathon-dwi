package xyz.cupscoffee.hackathondwi.shared.adapter.in.util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Messages shortcuts.
 */
@Slf4j
@Component
public final class MessagesShortcuts {
    private static MessageSource messageSource;

    public MessagesShortcuts(MessageSource messageSource) {
        MessagesShortcuts.messageSource = messageSource;
    }

    /***
     * Get a message from the message source.
     *
     * @param code           The message code.
     * @param defaultMessage The default message.
     * @return The message
     */
    public static String getMessage(String code, String defaultMessage) {
        return getMessage(code, defaultMessage, LocaleContextHolder.getLocale());
    }

    /**
     * Get a message from the message source.
     *
     * @param code           The message code.
     * @param defaultMessage The default message.
     * @param locale         The locale.
     * @return The message
     */
    public static String getMessage(String code, String defaultMessage, Locale locale) {
        String message = messageSource.getMessage(
                code,
                null,
                null,
                locale);

        if (message == null) {
            log.warn("The message code is empty, using the default message `{}`", defaultMessage);

            message = defaultMessage;
        }

        return message;
    }
}
