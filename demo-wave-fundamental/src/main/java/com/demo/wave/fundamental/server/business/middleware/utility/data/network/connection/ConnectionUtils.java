package com.demo.wave.fundamental.server.business.middleware.utility.data.network.connection;

import java.util.UUID;

/**
 * @author Vince Yuan
 * @date 2021/11/23
 */
public class ConnectionUtils {

    private ConnectionUtils() {}

    public static String getUuidString() {
        return UUID.randomUUID().toString();
    }
}
