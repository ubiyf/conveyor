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

import aio.context.*;
import aio.handler.AcceptCompletionHandler;
import serialization.Serializer;
import serialization.StringSerializer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AioServer {

    private static final String DEFAULT_NAME_PREFIX = "Aio-server-";

    public static final int MAX_ACCPETED_CLIENT = 1024;

    private static final AtomicInteger AIO_SERVER_COUNTER = new AtomicInteger();

    private final AsynchronousChannelGroup channelGroup;

    private String name;

    private int port;

    private List<AsynchronousServerSocketChannel> listeningChannels;

    private Serializer serializer;

    private ServerContext serverContext;

    private ClientContext accpetedClientContext;

    // TODO customize io monitor thread quantity
    // TODO customize io monitor thread factory
    // TODO customize compute thread quantity
    // TODO customize compute thread factory
    // TODO customize connectable client thread quantity
    // TODO customize connectable client thread factory
    // TODO customize client context factory
    public AioServer(String name, int port, Serializer serializer, ServerContext serverContext) throws IOException {
        channelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                Executors.defaultThreadFactory());
        if (null == name) {
            this.name = getDefaultName();
        } else {
            this.name = name;
        }
        this.port = port;
        this.serializer = serializer;
        this.serverContext = serverContext;
    }

    public void start() throws IOException {
        List<InetAddress> ipv4Address = IpUtils.getIpv4Address();
        listeningChannels = new ArrayList<>(ipv4Address.size());
        for (InetAddress ip : ipv4Address) {
            System.out.println(ip);
            InetSocketAddress socketAddress = new InetSocketAddress(ip, port);
            AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open().bind(socketAddress);
            // TODO set socket option
            listener.accept(new AcceptContext(this, listener), AcceptCompletionHandler.getInstance());
        }
    }

    public void shutdown() throws InterruptedException, IOException {
        channelGroup.shutdownNow();
        channelGroup.awaitTermination(1, TimeUnit.SECONDS);
    }

    private String getDefaultName() {
        return DEFAULT_NAME_PREFIX + AIO_SERVER_COUNTER.addAndGet(1);
    }

    public Serializer getSerializer() {
        return serializer;
    }

    public AsynchronousChannelGroup getChannelGroup() {
        return channelGroup;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public List<AsynchronousServerSocketChannel> getListeningChannels() {
        return listeningChannels;
    }

    public ServerContext getServerContext() {
        return serverContext;
    }

    public ClientContext getAccpetedClientContext() {
        return accpetedClientContext;
    }

    public void setAccpetedClientContext(ClientContext accpetedClientContext) {
        this.accpetedClientContext = accpetedClientContext;
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        CountDownLatch latch = new CountDownLatch(1);
        AioServer echoServer = new AioServer("test", 9999, StringSerializer.getInstance(), null);
        echoServer.start();
        latch.await();
    }

}
