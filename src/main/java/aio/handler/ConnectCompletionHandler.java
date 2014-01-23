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
import aio.context.ClientContext;

import java.nio.channels.CompletionHandler;

public class ConnectCompletionHandler implements CompletionHandler<Void, AioClient> {

    private static final ConnectCompletionHandler INSTANCE = new ConnectCompletionHandler();

    private ConnectCompletionHandler() {
    }

    public static ConnectCompletionHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void completed(Void result, AioClient attachment) {

    }

    @Override
    public void failed(Throwable exc, AioClient attachment) {

    }
}
