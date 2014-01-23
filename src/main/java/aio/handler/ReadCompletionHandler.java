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
import aio.AioServer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import decode.DecodeEvent;
import decode.DecodeEventFactory;
import decode.DecodeEventHandler;
import decode.DecodeEventTranslator;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.Executors;

public class ReadCompletionHandler implements CompletionHandler<Integer, AioClient> {

    private static final ReadCompletionHandler INSTANCE = new ReadCompletionHandler();

    private final Disruptor<DecodeEvent> decodeDisruptor;

    private ReadCompletionHandler() {
        decodeDisruptor = new Disruptor<>(
                DecodeEventFactory.getInstance(),
                AioServer.MAX_ACCPETED_CLIENT,
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()),
                ProducerType.MULTI,
                new BlockingWaitStrategy());
        decodeDisruptor.handleEventsWith(DecodeEventHandler.getInstance());
        decodeDisruptor.start();
    }

    public static ReadCompletionHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void completed(Integer result, AioClient client) {
        checkConnection(result, client);
        ByteBuffer bb = client.getReadBuffer();
        client.getClientContext();
        decodeDisruptor.publishEvent(DecodeEventTranslator.getInstance(), client);
    }

    @Override
    public void failed(Throwable exc, AioClient client) {
        client.destroy();
    }

    private void checkConnection(Integer result, AioClient client) {
        if (result == null || result.intValue() < 0) {
            client.destroy();
        }
    }
}