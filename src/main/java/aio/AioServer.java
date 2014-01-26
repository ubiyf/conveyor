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

import aio.context.AcceptContext;
import aio.context.ClientContext;
import aio.context.ServerContext;
import aio.handler.AcceptCompletionHandler;
import aio.handler.ReadCompletionHandler;
import aio.handler.WriteCompletionHandler;
import compute.ComputeEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import protocol.NetworkMessageHandler;
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

    private static final Logger logger = LoggerFactory.getLogger(AioServer.class);

    private static final String DEFAULT_NAME_PREFIX = "Aio-server-";

    public static final int MAX_ACCPETED_CLIENT = 1024;

    private static final AtomicInteger AIO_SERVER_COUNTER = new AtomicInteger();

    private final AsynchronousChannelGroup channelGroup;

    private String name;

    private int port;

    private List<AsynchronousServerSocketChannel> listeningChannels;

    private ServerContext serverContext;

    private final AcceptableClientFactory acceptableClientFactory;

    // TODO customize io monitor thread quantity
    // TODO customize io monitor thread factory
    // TODO customize compute thread quantity
    // TODO customize compute thread factory
    public AioServer(String name, int port, Serializer serializer, ServerContext serverContext, ClientContext accpetedClientContext) throws IOException {
        channelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                Executors.defaultThreadFactory());
        if (null == name) {
            this.name = getDefaultName();
        } else {
            this.name = name;
        }
        this.port = port;
        this.serverContext = serverContext;
        this.acceptableClientFactory = new AcceptableClientFactory(accpetedClientContext, serializer, new ReadCompletionHandler(), new WriteCompletionHandler());
    }

    public AioServer(String name, int port, Serializer serializer, ServerContext serverContext) throws IOException {
        this(name, port, serializer, serverContext, null);
    }

    public AioServer(String name, int port, Serializer serializer) throws IOException {
        this(name, port, serializer, null);
    }

    public AioServer(String name, int port) throws IOException {
        this(name, port, StringSerializer.getInstance());
    }

    public void start() throws IOException {
        List<InetAddress> ipv4Address = IpUtils.getIpv4Address();
        listeningChannels = new ArrayList<>(ipv4Address.size());
        AcceptCompletionHandler acceptCompletionHandler = new AcceptCompletionHandler();
        for (InetAddress ip : ipv4Address) {
            logger.debug(ip.toString());
            InetSocketAddress socketAddress = new InetSocketAddress(ip, port);
            AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open(channelGroup).bind(socketAddress);
            // TODO set socket option
            listener.accept(new AcceptContext(this, listener), acceptCompletionHandler);
        }
    }

    public void shutdown() throws InterruptedException, IOException {
        channelGroup.shutdownNow();
        channelGroup.awaitTermination(1, TimeUnit.SECONDS);
    }

    private String getDefaultName() {
        return DEFAULT_NAME_PREFIX + AIO_SERVER_COUNTER.addAndGet(1);
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

    public AcceptableClientFactory getAcceptableClientFactory() {
        return acceptableClientFactory;
    }

    public void registerNetworkMessageHandler(Class clazz, NetworkMessageHandler handler) {
        ComputeEventHandler.getInstance().registerNetworkMessageHandler(clazz, handler);
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        CountDownLatch latch = new CountDownLatch(1);
        AioServer echoServer = new AioServer("test", 9999);
        echoServer.registerNetworkMessageHandler(String.class, new StringMessageEchoHandler());
        echoServer.start();
        latch.await();
    }

    private static class StringMessageEchoHandler implements NetworkMessageHandler<String> {

        private final Logger logger = LoggerFactory.getLogger(StringMessageEchoHandler.class);

        @Override
        public void handle(String msg, AioClient client) {
            logger.debug(msg);
            client.writeNetworkMessage(msg);
            client.writeSysCall();
        }
    }
}
