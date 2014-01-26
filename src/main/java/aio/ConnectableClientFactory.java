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
import serialization.KryoSerializer;
import serialization.Serializer;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectableClientFactory {

    private final AtomicInteger sequencedIdGenerator = new AtomicInteger();

    private final ClientContext connectableClientContext;

    private final AsynchronousChannelGroup connectableClientGroup;

    private final Serializer serializer;

    private final ConnectCompletionHandler connectCompletionHandler;

    private final ReadCompletionHandler readCompletionHandler;

    private final WriteCompletionHandler writeCompletionHandler;

    public ConnectableClientFactory(ClientContext connectableClientContext, Serializer serializer, ConnectCompletionHandler connectCompletionHandler, ReadCompletionHandler readCompletionHandler, WriteCompletionHandler writeCompletionHandler) throws IOException {
        this.connectableClientContext = connectableClientContext;
        this.serializer = serializer;
        this.connectCompletionHandler = connectCompletionHandler;
        this.readCompletionHandler = readCompletionHandler;
        this.writeCompletionHandler = writeCompletionHandler;
        this.connectableClientGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(), Executors.defaultThreadFactory());
    }

    public AioClient newConnectableClient() throws IOException {
        AsynchronousSocketChannel clientChannel = AsynchronousSocketChannel.open(connectableClientGroup);
        return new AioClient(sequencedIdGenerator.incrementAndGet(), clientChannel, serializer, KryoSerializer.getInstance().copy(connectableClientContext), true, connectCompletionHandler, readCompletionHandler, writeCompletionHandler);
    }
}
