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

import java.nio.channels.CompletionHandler;

public class WriteCompletionHandler implements CompletionHandler<Integer, AioClient> {

    private static final WriteCompletionHandler INSTANCE = new WriteCompletionHandler();

    private WriteCompletionHandler() {
    }

    public static WriteCompletionHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void completed(Integer result, AioClient client) {
        checkConnection(result, client);
        client.clearWriteBuffer();
        client.readSysCall();
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