package aio;

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

    private ReadCompletionHandler (){
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