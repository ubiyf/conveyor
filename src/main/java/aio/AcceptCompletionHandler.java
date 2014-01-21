package aio;

import com.sun.org.apache.xml.internal.security.transforms.params.InclusiveNamespaces;

import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

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
public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

    private static final AcceptCompletionHandler INSTANCE = new AcceptCompletionHandler();

    private AcceptCompletionHandler () {}

    public static AcceptCompletionHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void completed(AsynchronousSocketChannel clientChannel, AsynchronousServerSocketChannel listener) {
        // 接受下一个连接
        listener.accept(listener, this);
        // 处理当前连接
        onNewClient(clientChannel);
    }

    @Override
    public void failed(Throwable exc, AsynchronousServerSocketChannel listener) {
    }

    private void onNewClient(AsynchronousSocketChannel clientChnl) {
        AioClient c = new AioClient(clientChnl);
        System.out.println(clientChnl.toString());
        System.out.println(clientChnl.hashCode());
        c.wantRead();
    }
}
