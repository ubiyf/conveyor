package decode;

import aio.AioClient;

import java.nio.ByteBuffer;

public class DecodeEvent {

    private AioClient client;

    public AioClient getClient() {
        return client;
    }

    public void setClient(AioClient client) {
        this.client = client;
    }
}
