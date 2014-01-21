package aio;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedDeque;

public final class ByteBufferPool {

    private static final int INIT_BYTE_BUFFER_NUMBER = 1024;

    private static final int DEFAULT_BYTE_BUFFER_SIZE = 4096;

    private static final ConcurrentLinkedDeque<ByteBuffer> BYTE_BUFFER_POOL = new ConcurrentLinkedDeque();

    private static int bufferCounter;

    static {
        for (int i = 0; i < INIT_BYTE_BUFFER_NUMBER; i++) {
            ByteBuffer bb = allocateDirectBuffer();
            BYTE_BUFFER_POOL.add(bb);
        }
    }

    public static ByteBuffer getBufferFromPool() {
        ByteBuffer bb = BYTE_BUFFER_POOL.poll();
        if (bb != null) {
            return  bb;
        } else {
            return allocateDirectBuffer();
        }
    }

    public static void returnByteBuffer(ByteBuffer bb) {
        BYTE_BUFFER_POOL.add(bb);
    }

    private static ByteBuffer allocateDirectBuffer() {
        bufferCounter ++;
        return ByteBuffer.allocateDirect(DEFAULT_BYTE_BUFFER_SIZE);
    }

    public static int getBufferCounter() {
        return bufferCounter;
    }

    public static int getDirectMemoryLoad() {
        return 100;
    }
}
