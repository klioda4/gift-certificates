package ru.clevertec.ecl.cluster.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

@UtilityClass
public class RequestPathParser {

    private static final Pattern ID_PARSE_PATTERN = Pattern.compile("^/v\\d+/\\S*?/(\\d+)");
    private static final Pattern REQUEST_TO_ALL_CHECK_PATTERN = Pattern.compile("^/v\\d+/\\S*?/?(\\?.*)?$");
    private static final Pattern REQUEST_TO_ORDERS_BY_USER_ID_CHECK_PATTERN =
        Pattern.compile("^/v\\d+/orders/user/\\d+");

    /**
     * Returns id parsed from servletPath.
     *
     * @param servletPath servletPath of request
     * @return parsed id
     * @throws IllegalArgumentException if servletPath doesn't contain id
     */
    public long getIdFromServletPath(String servletPath) {
        Matcher matcher = ID_PARSE_PATTERN.matcher(servletPath);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            throw new IllegalArgumentException("Incorrect path");
        }
    }

    public boolean isRequestById(String servletPath) {
        Matcher matcher = ID_PARSE_PATTERN.matcher(servletPath);
        return matcher.find();
    }

    public boolean isRequestToAll(String servletPath) {
        Matcher matcher = REQUEST_TO_ALL_CHECK_PATTERN.matcher(servletPath);
        return matcher.matches();
    }

    public boolean isGetAllOrdersByUserId(String servletPath) {
        Matcher matcher = REQUEST_TO_ORDERS_BY_USER_ID_CHECK_PATTERN.matcher(servletPath);
        return matcher.find();
    }
}
