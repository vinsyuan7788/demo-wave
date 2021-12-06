package com.demo.wave.fundamental.server.business.middleware.utility.data.network.request.body;

import com.demo.wave.fundamental.server.business.middleware.utility.data.network.io.serdes.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * @author Vince Yuan
 * @date 2021/11/22
 */
public abstract class RequestBody implements Record {

    private static final Logger LOG = LoggerFactory.getLogger(RequestBody.class);

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Class<? extends RequestBody> clazz = getClass();
        sb.append(clazz.getSimpleName()).append("{");
        try {
            Field[] declaredFields = clazz.getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                Field declaredField = declaredFields[i];
                declaredField.setAccessible(true);
                sb.append(declaredField.getName()).append("=").append(declaredField.get(this));
                if (i < declaredFields.length - 1) {
                    sb.append(",");
                }
            }
        } catch (Throwable t) {
            LOG.error("to string error", t);
        }
        sb.append("}");
        return sb.toString();
    }
}
