package ru.clevertec.ecl.exception;

import java.util.List;
import lombok.Getter;

/**
 * Thrown when one or more servers are down that required to handle request
 */
@Getter
public class ServerDownException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Servers %s are down, but required to handle request.";

    private final List<String> serverAddresses;

    public ServerDownException(List<String> serverAddresses) {
        super(String.format(MESSAGE_TEMPLATE,
                            String.join(", ", serverAddresses)));
        this.serverAddresses = serverAddresses;
    }
}
