package ru.clevertec.ecl.cluster.nodeInfo;

import lombok.experimental.UtilityClass;
import ru.clevertec.ecl.model.CommitLog;

@UtilityClass
public class InterceptorConstants {

    public String SKIP_HANDLING_ATTRIBUTE = "Skip-Handling";
    public String IS_INTERCEPTOR_REQUEST_ATTRIBUTE = "Is-Interceptor-Request";
    public String COMMIT_LOG_ID_ATTRIBUTE = "Commit-Log-Id";

    public String COMMIT_LOG_ENTITY_NAME = CommitLog.class.getSimpleName();
    public String COMMIT_LOG_URI = "/api/commit-log";
    public String COMMIT_LOG_UPDATE_STATUS_URI_PATTERN = COMMIT_LOG_URI + "/%d/status";
    public String COMMIT_LOG_APPLY_URI_PATTERN = COMMIT_LOG_URI + "/%d/apply";
    public String COMMIT_LOG_DECLINE_URI_PATTERN = COMMIT_LOG_URI + "/%d/decline";
    public String CURRENT_SEQUENCE_URI_PATTERN = COMMIT_LOG_URI + "/sequence/%s/current";
    public String NEXT_SEQUENCE_URI_PATTERN_ = COMMIT_LOG_URI + "/sequence/%s/next";
}
