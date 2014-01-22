package aio;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

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

/**
 * Use default channel group
 * The thread quantity of default channel group equals to the processor quantity
 */
public class AioServer {

    private static final String DEFAULT_NAME_PREFIX = "Aio-server-";

    public static final int MAX_ACCPETED_CLIENT = 1024;

    private static final AtomicInteger AIO_SERVER_COUNTER = new AtomicInteger();

    private final AsynchronousChannelGroup channelGroup;

    private String name;

    private int port;

    private List<AsynchronousServerSocketChannel> listeningChnls;

    public AioServer(String name, int port) throws IOException{
        channelGroup = AsynchronousChannelGroup.withFixedThreadPool(Runtime.getRuntime().availableProcessors(),
                Executors.defaultThreadFactory());
        if (null == name) {
            this.name = getDefaultName();
        } else {
            this.name = name;
        }
        this.port = port;
    }

    public void start() throws IOException {
        List<InetAddress> ipv4Address = IpUtils.getIpv4Address();
        listeningChnls = new ArrayList<>(ipv4Address.size());
        for (InetAddress ip : ipv4Address) {
            System.out.println(ip);
            InetSocketAddress socketAddress = new InetSocketAddress(ip, port);
            AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open().bind(socketAddress);
            listener.accept(listener, AcceptCompletionHandler.getInstance());
        }
    }

    public void shutdown() throws InterruptedException, IOException {
        channelGroup.shutdownNow();
        channelGroup.awaitTermination(1, TimeUnit.SECONDS);
    }

    private String getDefaultName() {
        return DEFAULT_NAME_PREFIX + AIO_SERVER_COUNTER.addAndGet(1);
    }

    public static void main(String [] args) throws InterruptedException, IOException {
        CountDownLatch latch = new CountDownLatch(1);
        AioServer server = new AioServer("test", 9999);
        server.start();
        latch.await();
    }

}
