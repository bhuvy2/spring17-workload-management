package org.apache.airavata.sga.messaging.service.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ajinkya on 2/2/17.
 */
public class Constants {

    public static final String AMQP_URI = "amqp://airavata:airavata@gw56.iu.xsede.org:5672/messaging";
    public static final boolean IS_DURABLE_QUEUE = true;
    public static final int PREFETCH_COUT = 20;
    public static final int QUEUE_MAX_PRIORITY = 10;
    public static final Map<String, Object> QUEUE_ARGS = new HashMap<String, Object>()
    {{put("x-max-priority", Constants.QUEUE_MAX_PRIORITY);}};

}
