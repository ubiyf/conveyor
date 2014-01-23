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

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedDeque;

public final class ByteBufferPool {

    private static final int INIT_BYTE_BUFFER_QUANTITY = 1024;

    // TODO 4K align helper
    private static final int DEFAULT_BYTE_BUFFER_SIZE = 4096;

    private static final ConcurrentLinkedDeque<ByteBuffer> BYTE_BUFFER_POOL = new ConcurrentLinkedDeque();

    private static int bufferCounter;

    static {
        for (int i = 0; i < INIT_BYTE_BUFFER_QUANTITY; i++) {
            ByteBuffer bb = allocateDirectBuffer();
            BYTE_BUFFER_POOL.add(bb);
        }
    }

    public static ByteBuffer getBufferFromPool() {
        ByteBuffer bb = BYTE_BUFFER_POOL.poll();
        if (bb != null) {
            return bb;
        } else {
            return allocateDirectBuffer();
        }
    }

    public static void returnByteBuffer(ByteBuffer bb) {
        BYTE_BUFFER_POOL.add(bb);
    }

    private static ByteBuffer allocateDirectBuffer() {
        bufferCounter++;
        return ByteBuffer.allocateDirect(DEFAULT_BYTE_BUFFER_SIZE);
    }

    public static int getBufferCounter() {
        return bufferCounter;
    }

    // TODO dummy implementation
    public static int getDirectMemoryLoad() {
        return 100;
    }
}
