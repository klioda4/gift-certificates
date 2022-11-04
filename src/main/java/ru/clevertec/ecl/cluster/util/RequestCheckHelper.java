package ru.clevertec.ecl.cluster.util;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import ru.clevertec.ecl.cluster.nodeInfo.InterceptorConstants;

@UtilityClass
public class RequestCheckHelper {

    public boolean isForwarded(HttpServletRequest request) {
        String isForwardedHeader = request.getHeader(InterceptorConstants.FORWARDED_ATTRIBUTE);
        return Optional.ofNullable(isForwardedHeader)
                   .map(Boolean::parseBoolean)
                   .orElse(false);
    }

    public boolean doesNeedToDistribute(HttpServletRequest request) {
        Boolean needToDistributeAttribute = (Boolean) request.getAttribute(
            InterceptorConstants.NEED_TO_DISTRIBUTE_ATTRIBUTE);
        return Optional.ofNullable(needToDistributeAttribute)
                   .orElse(false);
    }
}
