package com.demo.wave.fundamental.client.nonblocking;

import com.demo.wave.common.utility.LogUtils;
import com.demo.wave.fundamental.client.nonblocking.socket.DemoClientSocket;
import com.demo.wave.fundamental.client.nonblocking.socket.NioDemoClientSocket;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.definition.OperationType;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.packet.Packet;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.request.body.RequestBody;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.request.body.SnowflakeIdRequestBody;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.request.body.TransmitDataRequestBody;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.request.header.RequestHeader;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.response.body.ResponseBody;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.response.body.SnowflakeIdResponseBody;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.response.body.TransmitDataResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * This client is used to serve as client that can send data to server. <br/>
 * It implements a duplex communication with server with one thread and one client socket
 * channel each thread. This idea is widely adopted in the industry and implemented by some
 * open-source frameworks (e.g., zoo-keeper, etc.). Of course this demo can be further
 * extended in terms of design (e.g., introducing or integrating heart-beat, session management,
 * data processing (including structure, transaction, sharding, etc.), exception processing,
 * configuration, statistics, monitoring, etc.)
 *
 * @author Vince Yuan
 * @date 2021/11/20
 */
public class NioDemoClient extends DemoClient {

    private static final Logger LOG = LoggerFactory.getLogger(NioDemoClient.class);

    public DemoClientSocket clientSocket;

    public NioDemoClient(SocketAddress serverAddressToConnect) {
        clientSocket = new NioDemoClientSocket(this, serverAddressToConnect);
    }

    @Override
    public void startClient() {
        clientSocket.startSocket();
        LOG.info(LogUtils.getMessage("Client is started"));
    }

    @Override
    public void closeClient() {
        clientSocket.closeSocket();
        LOG.info(LogUtils.getMessage("Client is closed"));
    }

    @Override
    public <T> Boolean write(T data) {
        if (data == null) {
            return null;
        }
        long startTimeMillis = System.currentTimeMillis();
        RequestHeader requestHeader = RequestHeader.create(OperationType.TRANSMIT_DATA);
        RequestBody requestBody = TransmitDataRequestBody.create(data);
        Packet packet = Packet.create(requestHeader, requestBody);
        System.out.println("Time elapsed for creating a packet: " + (System.currentTimeMillis() - startTimeMillis) + "ms");
        ResponseBody responseBody = clientSocket.submit(packet);
        if (responseBody == null) {
            return null;
        } else {
            return ((TransmitDataResponseBody) responseBody).isSuccess();
        }
    }

    @Override
    public Long getSnowflakeId(int appCode) {
        long startTimeMillis = System.currentTimeMillis();
        RequestHeader requestHeader = RequestHeader.create(OperationType.SNOWFLAKE_ID);
        RequestBody requestBody = SnowflakeIdRequestBody.create(appCode);
        Packet packet = Packet.create(requestHeader, requestBody);
        System.out.println("Time elapsed for creating a packet: " + (System.currentTimeMillis() - startTimeMillis) + "ms");
        ResponseBody responseBody = clientSocket.submit(packet);
        if (responseBody == null) {
            return null;
        } else {
            return ((SnowflakeIdResponseBody) responseBody).getSnowflakeId();
        }
    }
}
