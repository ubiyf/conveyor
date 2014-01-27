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

import aio.AcceptableClientFactory;
import aio.AioClient;
import aio.context.AcceptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AcceptContext> {

    private static final Logger logger = LoggerFactory.getLogger(AcceptCompletionHandler.class);

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
        logger.debug(exc.getMessage());
    }

    /**
     * Server is always passive, when accepts a new client
     * it will initiate a read system call and waiting for client action
     *
     * @param clientChannel the socket channel connected to the remote client
     * @param context       the accept action context
     */
    private void onNewClient(AsynchronousSocketChannel clientChannel, AcceptContext context) {
        try {
            clientChannel.setOption(StandardSocketOptions.TCP_NODELAY, true);
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
        AcceptableClientFactory factory = context.getServer().getAcceptableClientFactory();
        AioClient c = factory.newAcceptableClient(clientChannel);
        logger.debug(clientChannel.toString());
        c.readSysCall();
    }
}
