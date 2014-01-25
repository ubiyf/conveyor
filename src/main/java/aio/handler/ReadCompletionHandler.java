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
import compute.ComputeEvent;
import compute.ComputeEventFactory;
import compute.ComputeEventHandler;
import compute.ComputeEventTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ReadCompletionHandler implements CompletionHandler<Integer, AioClient> {

    private static final Logger logger = LoggerFactory.getLogger(ReadCompletionHandler.class);

    private final List<Disruptor<ComputeEvent>> computeDisruptors = new ArrayList<>(Runtime.getRuntime().availableProcessors());

    public ReadCompletionHandler() {
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            Disruptor<ComputeEvent> computeDisruptor = new Disruptor<>(
                    ComputeEventFactory.getInstance(),
                    AioServer.MAX_ACCPETED_CLIENT,
                    Executors.newSingleThreadExecutor(),
                    ProducerType.MULTI,
                    new BlockingWaitStrategy());
            computeDisruptor.handleEventsWith(ComputeEventHandler.getInstance());
            computeDisruptor.start();
            computeDisruptors.add(computeDisruptor);
        }
    }

    @Override
    public void completed(Integer result, AioClient client) {
        if (result == null || result < 0) {
            client.destroy();
            return;
        }
        client.getClientContext();
        int hashCode = client.hashCode();
        int index = hashCode % Runtime.getRuntime().availableProcessors();
        logger.debug(hashCode + " " + index);
        Disruptor<ComputeEvent> computeDisruptor = computeDisruptors.get(index);
        computeDisruptor.publishEvent(ComputeEventTranslator.getInstance(), client);
    }

    @Override
    public void failed(Throwable exc, AioClient client) {
        client.destroy();
    }

}