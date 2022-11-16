package ru.clevertec.ecl.cluster.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class RequestPathParser {

    private static final Pattern ID_PARSE_PATTERN = Pattern.compile("^/v\\d+/\\S*?/(\\d+)");
    private static final Pattern REQUEST_TO_ALL_CHECK_PATTERN = Pattern.compile("^/v\\d+/[^/]+/?(\\?.*)?$");
    private static final Pattern REQUEST_TO_ORDERS_BY_USER_ID_CHECK_PATTERN =
        Pattern.compile("^/v\\d+/orders/user/\\d+");
    private final static Pattern SERVER_ADDRESS_PARSE_PATTERN = Pattern.compile("^(https?://\\S*?:\\d+)");
    private final static Pattern ENTITY_NAME_PARSE_PATTERN = Pattern.compile("^/v\\d+/([^/]+)");
    private final static Pattern URI_TO_ENTITIES_PARSE_PATTERN = Pattern.compile("(/api/v\\d+/[^/]+)");
    private final static int FIRST_GROUP_INDEX = 1;

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
            return Integer.parseInt(matcher.group(FIRST_GROUP_INDEX));
        } else {
            log.error("Could not extract ID from {}", servletPath);
            throw new IllegalArgumentException("Incorrect path" + servletPath);
        }
    }

    public String getRawEntityNameFromServletPath(String servletPath) {
        Matcher addressMatcher = ENTITY_NAME_PARSE_PATTERN.matcher(servletPath);
        if (addressMatcher.find()) {
            return addressMatcher.group(FIRST_GROUP_INDEX);
        } else {
            log.error("Could not extract entity name from {}", servletPath);
            throw new IllegalArgumentException("Incorrect path " + servletPath);
        }
    }

    public String getServerAddressFromUrl(String url) {
        Matcher addressMatcher = SERVER_ADDRESS_PARSE_PATTERN.matcher(url);
        if (addressMatcher.find()) {
            return addressMatcher.group(FIRST_GROUP_INDEX);
        } else {
            log.error("Could not extract server address from {}", url);
            throw new IllegalArgumentException("Incorrect url " + url);
        }
    }

    public String getUriToEntitiesFromUrl(String url) {
        Matcher addressMatcher = URI_TO_ENTITIES_PARSE_PATTERN.matcher(url);
        if (addressMatcher.find()) {
            return addressMatcher.group(FIRST_GROUP_INDEX);
        } else {
            log.error("Could not extract entity u from {}", url);
            throw new IllegalArgumentException("Incorrect url " + url);
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

    public boolean isFindAllOrdersByUserId(String servletPath) {
        Matcher matcher = REQUEST_TO_ORDERS_BY_USER_ID_CHECK_PATTERN.matcher(servletPath);
        return matcher.find();
    }
}
