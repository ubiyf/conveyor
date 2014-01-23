package aio.context;

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

import aio.AioServer;

import java.nio.channels.AsynchronousServerSocketChannel;

public final class AcceptContext {

    private AioServer server;

    private AsynchronousServerSocketChannel listeningChannel;

    public AcceptContext(AioServer server, AsynchronousServerSocketChannel listeningChannel) {
        this.server = server;
        this.listeningChannel = listeningChannel;
    }

    public AioServer getServer() {
        return server;
    }

    public AsynchronousServerSocketChannel getListeningChannel() {
        return listeningChannel;
    }

}
