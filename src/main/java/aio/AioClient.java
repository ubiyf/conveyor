package aio;
/*
 * Copyright 2014 Yang Fan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import aio.context.ClientContext;
import aio.handler.ConnectCompletionHandler;
import aio.handler.ReadCompletionHandler;
import aio.handler.WriteCompletionHandler;
import serialization.Serializer;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class AioClient {

    private final AsynchronousSocketChannel clientChannel;

    private ByteBuffer readBuffer;

    private ByteBuffer writeBuffer;

    private final Serializer serializer;

    private final boolean connectable;

    private final ClientContext clientContext;

    private final ConnectCompletionHandler connectCompletionHandler;

    private final ReadCompletionHandler readCompletionHandler;

    private final WriteCompletionHandler writeCompletionHandler;

    private boolean read;

    private Object inputNetworkMessage;

    public AioClient(AsynchronousSocketChannel clientChannel, Serializer serializer, ClientContext clientContext, boolean connectable, ConnectCompletionHandler connectCompletionHandler, ReadCompletionHandler readCompletionHandler, WriteCompletionHandler writeCompletionHandler) {
        this.clientChannel = clientChannel;
        this.readBuffer = ByteBufferPool.getBufferFromPool();
        this.writeBuffer = ByteBufferPool.getBufferFromPool();
        this.serializer = serializer;
        this.connectable = connectable;
        this.clientContext = clientContext;
        this.connectCompletionHandler = connectCompletionHandler;
        this.readCompletionHandler = readCompletionHandler;
        this.writeCompletionHandler = writeCompletionHandler;
    }

    public void connectSysCall(SocketAddress remoteServerAddress) {
        if (connectable) {
            clientChannel.connect(remoteServerAddress, this, connectCompletionHandler);
        } else {
            throw new RuntimeException("Can not connect to remote server, this client is created by server socket accept.");
        }
    }

    /**
     * Just tell OS that I want to read
     */
    public void readSysCall() {
        clientChannel.read(readBuffer, this, readCompletionHandler);
    }

    /**
     * Just tell OS that I want to write
     */
    public void writeSysCall() {
        clientChannel.write(writeBuffer, this, writeCompletionHandler);
    }

    public void shutdown() {
        try {
            clientChannel.shutdownInput();
            clientChannel.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        clearReadBuffer();
        clearWriteBuffer();
        returnByteBuffer(readBuffer);
        returnByteBuffer(writeBuffer);
    }

    private void clearWriteBuffer() {
        if (writeBuffer != null) {
            writeBuffer.clear();
        }
    }

    private void clearReadBuffer() {
        if (readBuffer != null) {
            readBuffer.clear();
        }
    }

    private void returnByteBuffer(ByteBuffer bb) {
        if (bb != null) {
            ByteBufferPool.returnByteBuffer(bb);
        }
    }

    public ClientContext getClientContext() {
        return clientContext;
    }

    public Object readNetworkMessage() {
        if (!read) {
            inputNetworkMessage = serializer.toObject(readBuffer);
            clearReadBuffer();
        }
        return  inputNetworkMessage;
    }

    public void writeNetworkMessage(Object msg) {
        clearWriteBuffer();
        serializer.toByte(writeBuffer, msg);
    }
}
