package com.demo.wave.fundamental.server.business.middleware.utility.data.network.io.serdes;

import com.demo.wave.fundamental.server.business.middleware.utility.data.network.io.wrapper.InputWrapper;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.io.wrapper.OutputWrapper;

/**
 * @author Vince Yuan
 * @date 2021/11/22
 */
public interface Record {

    /**
     * This method is used to serialize the value of each field to the output wrapper
     *
     * @param outputWrapper
     */
    void serializeTo(OutputWrapper outputWrapper);

    /**
     * This method is used to deserialize the value of input wrapper to each field
     *
     * @param inputWrapper
     */
    void deserializeFrom(InputWrapper inputWrapper);
}
