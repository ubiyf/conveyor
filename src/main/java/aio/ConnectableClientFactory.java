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
import serialization.KryoSerializer;
import serialization.Serializer;

import java.nio.channels.AsynchronousSocketChannel;

public class ConnectableClientFactory {

    private ClientContext connectableClientContext;

    public ConnectableClientFactory(ClientContext connectableClientContext) {
        this.connectableClientContext = connectableClientContext;
    }

    public AioClient newConnectableClient(AsynchronousSocketChannel clientChannel, Serializer serializer) {
        return new AioClient(clientChannel, serializer, KryoSerializer.getInstance().copy(connectableClientContext), true);
    }
}
