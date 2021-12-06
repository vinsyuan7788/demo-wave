package com.demo.wave.fundamental.server.business.middleware.processor;

import com.demo.wave.fundamental.server.business.common.processor.BaseProcessor;
import com.demo.wave.fundamental.server.business.common.utility.ProcessorHelper;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.definition.ByteBufferType;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.definition.OperationType;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.packet.Packet;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.request.body.RequestBody;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.request.body.SnowflakeIdRequestBody;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.request.body.TransmitDataRequestBody;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.request.header.RequestHeader;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.response.body.ResponseBody;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.response.body.SnowflakeIdResponseBody;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.response.body.TransmitDataResponseBody;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.response.header.ResponseHeader;
import com.demo.wave.fundamental.server.business.middleware.utility.snowflake.SnowFlakeIdWorker;
import com.demo.wave.fundamental.server.business.middleware.utility.snowflake.factory.SnowFlakeIdWorkerFactory;
import com.demo.wave.fundamental.server.design.reactor.poller.utility.NeedProcessEvent;
import com.demo.wave.fundamental.server.design.reactor.selector.utility.NioSelectionKeyInfo;
import com.demo.wave.fundamental.server.design.reactor.socket.BaseSocketWrapper;
import com.demo.wave.fundamental.utility.exception.DemoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author Vince Yuan
 * @date 2021/12/1
 */
public class MiddlewareBaseProcessor extends ProcessorHelper implements BaseProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(MiddlewareBaseProcessor.class);

    private BaseSocketWrapper socketWrapper;

    public MiddlewareBaseProcessor(BaseSocketWrapper socketWrapper) {
        try {
            this.socketWrapper = socketWrapper;
        } catch (Throwable t) {
            handleInitializingThrowable(t);
        }
    }

    @Override
    public void process() {
        try {
            processReadEvent();
        } catch (Throwable t) {
            handleProcessingThrowable(t);
        }
    }

    @Override
    public void handleProcessingThrowable(Throwable t) {
        LOG.error("Perform processing error", t);
    }

    @Override
    public void processReadEvent() throws Exception {
        // Get content from socket wrapper
        NioSelectionKeyInfo selectedKeyInfo = socketWrapper.getSelectionKeyInfo();
        SocketChannel acceptedSocketChannel = (SocketChannel) selectedKeyInfo.getSelectableChannel();
        Selector selector = selectedKeyInfo.getSelector();
        SelectionKey selectedKey = selectedKeyInfo.getSelectionKey();
        NeedProcessEvent needProcessEvent = socketWrapper.getNeedProcessEvent();

        // Get the byte buffer that carries the read data
        ByteBuffer byteBuffer = needProcessEvent.getByteBufferWithDataRead();
        int numberOfBytesRead = needProcessEvent.getNumberOfBytesRead();
        LOG.info("[Data] | Server reads bytes from client {} | bytes: {}", acceptedSocketChannel.getRemoteAddress(), numberOfBytesRead);

        // Read the data from the byte buffers
        Packet packet = Packet.readOnServer(byteBuffer);
        LOG.info("[Data] | Server reads packet from client {} | packet: {}", acceptedSocketChannel.getRemoteAddress(), packet);

        // Process received packet and return a new one
        Packet packetToSend = processReceivedPacket(packet);

        // Get the byte buffers from packet
        ByteBuffer[] byteBuffers = packetToSend.createByteBuffersOnServer(ByteBufferType.DIRECT);

        // Gather-write the byte buffers to client
        long numberOfBytesWritten = acceptedSocketChannel.write(byteBuffers);
        if (numberOfBytesWritten <= 0) { return; }
        LOG.info("[Data] | Server writes bytes to client {} | bytes: {}", acceptedSocketChannel.getRemoteAddress(), numberOfBytesWritten);
        LOG.info("[Data] | Server writes packet to client {} | packet: {}", acceptedSocketChannel.getRemoteAddress(), packetToSend);

        // Register the accepted socket channel to selector
        SelectionKey registeredKey = acceptedSocketChannel.register(selector, SelectionKey.OP_READ);
        int registeredOpCode = registeredKey.interestOps();
        logSelectionKeyInfo(selectedKey, selectedKeyInfo.getInterestOps(), registeredKey, registeredOpCode);
    }

    @Override
    public synchronized Packet processReceivedPacket(Packet receivedPacket) {
        // Get request header
        RequestHeader requestHeader = receivedPacket.getRequestHeader();
        if (requestHeader == null) {
            throw new DemoException("Request header is null");
        }
        // Get the operation type from request header
        int operationType = requestHeader.getOperationType();
        // Perform business processing according to the operation type
        RequestBody requestBody = receivedPacket.getRequestBody();
        ResponseBody responseBody;
        switch (operationType) {
            case OperationType.TRANSMIT_DATA:
                // Get request body
                TransmitDataRequestBody transmitDataRequestBody = (TransmitDataRequestBody) requestBody;
                // Perform business processing here
                LOG.info("[Business] | Server completes processing data | data type: {} | data: {}", transmitDataRequestBody.getDataType(), transmitDataRequestBody.getData());
                // Create a corresponding response body
                responseBody = TransmitDataResponseBody.create(true);
                break;
            case OperationType.SNOWFLAKE_ID:
                // Get request body
                SnowflakeIdRequestBody snowflakeIdRequestBody = (SnowflakeIdRequestBody) requestBody;
                // Perform business processing here
                Long snowflakeId = null;
                SnowFlakeIdWorker snowFlakeIdWorker = SnowFlakeIdWorkerFactory.getWorker(snowflakeIdRequestBody.getAppCode());
                if (snowFlakeIdWorker == null) {
                    LOG.error("Snowflake ID worker is null");
                } else {
                    snowflakeId = snowFlakeIdWorker.getNextId();
                }
                LOG.info("[Business] | Server completes processing data | app code: {}", snowflakeIdRequestBody.getAppCode());
                // Create a corresponding response body
                responseBody = SnowflakeIdResponseBody.create(snowflakeId);
                break;
            default:
                throw new DemoException("Operation type is unknown");
        }
        // Get the connection ID from request header
        String connectionId = requestHeader.getConnectionId();
        // Create a response header for the packet to return
        ResponseHeader responseHeader = ResponseHeader.create(connectionId, operationType);
        // Return a new packet to send to client
        Packet packet = Packet.create(responseHeader, responseBody);
        LOG.info("[Process] | Server generates a new packet to send | packet: {}", packet);
        return packet;
    }

    @Override
    public void handleInitializingThrowable(Throwable t) {
        LOG.error("Create processor error", t);
    }
}
