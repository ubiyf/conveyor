package aio.handler;
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

import aio.AioClient;
import aio.context.AcceptContext;
import aio.context.ClientContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import serialization.KryoSerializer;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AcceptContext> {

    private static final Logger logger = LoggerFactory.getLogger(AcceptCompletionHandler.class);

    private static final AcceptCompletionHandler INSTANCE = new AcceptCompletionHandler();

    private AcceptCompletionHandler() {
    }

    public static AcceptCompletionHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void completed(AsynchronousSocketChannel clientChannel, AcceptContext context) {
        // accept next connect request
        AsynchronousServerSocketChannel listener = context.getListeningChannel();
        listener.accept(context, this);
        // create new accepted client
        onNewClient(clientChannel, context);
    }

    @Override
    public void failed(Throwable exc, AcceptContext context) {
    }

    private void onNewClient(AsynchronousSocketChannel clientChnl, AcceptContext context) {
        ClientContext sampleClientContext = context.getServer().getAccpetedClientContext();
        ClientContext clientContext = KryoSerializer.getInstance().copy(sampleClientContext);
        AioClient c = new AioClient(clientChnl, context.getServer().getSerializer(), clientContext, false);
        logger.debug(clientChnl.toString());
        c.readSysCall();
    }
}
