package aio;

import java.nio.channels.CompletionHandler;

public class WriteCompletionHandler implements CompletionHandler<Integer, AioClient> {

    private static final WriteCompletionHandler INSTANCE = new WriteCompletionHandler();

    private WriteCompletionHandler (){}

    public static WriteCompletionHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void completed(Integer result, AioClient client) {
        checkConnection(result, client);
        client.clearWriteBuffer();
        client.wantRead();
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
