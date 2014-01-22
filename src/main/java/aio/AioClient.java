package aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

public class AioClient {

    private AsynchronousSocketChannel clientChannel;

    private ByteBuffer readBuffer;

    private ByteBuffer writeBuffer;

    public AioClient(AsynchronousSocketChannel clientChannel) {
        this.clientChannel = clientChannel;
        this.readBuffer = ByteBufferPool.getBufferFromPool();
        this.writeBuffer = ByteBufferPool.getBufferFromPool();
    }

    public void wantRead() {
        clientChannel.read(readBuffer, this, ReadCompletionHandler.getInstance());
    }

    public void wantWrite() {
        clientChannel.write(writeBuffer, this, WriteCompletionHandler.getInstance());
    }

    public void shutdown() {
        try {
            clientChannel.shutdownInput();
            clientChannel.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void destroy() {
        clearReadBuffer();
        clearWriteBuffer();
        returnByteBuffer(readBuffer);
        returnByteBuffer(writeBuffer);
    }

    public void clearWriteBuffer() {
        if (writeBuffer != null) {
            writeBuffer.clear();
        }
    }

    public void clearReadBuffer() {
        if (readBuffer != null) {
            readBuffer.clear();
        }
    }

    private void returnByteBuffer(ByteBuffer bb) {
        if (bb != null) {
            ByteBufferPool.returnByteBuffer(bb);
        }
    }

    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    public ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }
}
