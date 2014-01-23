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

    private AsynchronousSocketChannel clientChannel;

    private ByteBuffer readBuffer;

    private ByteBuffer writeBuffer;

    private Serializer serializer;

    private boolean connectable;

    private ClientContext clientContext;

    public AioClient(AsynchronousSocketChannel clientChannel, Serializer serializer, ClientContext clientContext, boolean connectable) {
        this.clientChannel = clientChannel;
        this.readBuffer = ByteBufferPool.getBufferFromPool();
        this.writeBuffer = ByteBufferPool.getBufferFromPool();
        this.serializer = serializer;
        this.connectable = connectable;
        this.clientContext = clientContext;
    }

    public void connectSysCall(SocketAddress remoteServerAddress) {
        if (connectable) {
            clientChannel.connect(remoteServerAddress, this, ConnectCompletionHandler.getInstance());
        } else {
            throw new RuntimeException("Can not connect to remote server, this client is created by server socket accept.");
        }
    }

    /**
     * Just tell OS that I want to read
     */
    public void readSysCall() {
        clientChannel.read(readBuffer, this, ReadCompletionHandler.getInstance());
    }

    /**
     * Just tell OS that I want to write
     */
    public void writeSysCall() {
        clientChannel.write(writeBuffer, this, WriteCompletionHandler.getInstance());
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

    public void clearWriteBuffer() {
        if (writeBuffer != null) {
            writeBuffer.clear();
        }
    }

    public void clearReadBuffer() {
        if (readBuffer != null) {
            readBuffer.clear();
        }
    }

    private void returnByteBuffer(ByteBuffer bb) {
        if (bb != null) {
            ByteBufferPool.returnByteBuffer(bb);
        }
    }

    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    public ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }

    public ClientContext getClientContext() {
        return clientContext;
    }

    public Serializer getSerializer() {
        return serializer;
    }
}
