package aio;

import org.omg.PortableInterceptor.INACTIVE;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public class ReadCompletionHandler implements CompletionHandler<Integer, AioClient> {

    private static final ReadCompletionHandler INSTANCE = new ReadCompletionHandler();

    private ReadCompletionHandler (){}

    public static ReadCompletionHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void completed(Integer result, AioClient client) {
        checkConnection(result, client);
        ByteBuffer bb = client.getReadBuffer();
        System.out.println(bb.get(0));
        client.clearReadBuffer();
        client.wantWrite();
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