package decode;
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

import com.lmax.disruptor.EventHandler;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class DecodeEventHandler implements EventHandler<DecodeEvent> {

    private static final DecodeEventHandler INSTANCE = new DecodeEventHandler();

    private DecodeEventHandler() {
    }

    public static DecodeEventHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEvent(DecodeEvent event, long sequence, boolean endOfBatch) throws Exception {
        String xxxx = byteBufferToString(event.getClient().getReadBuffer());
        System.out.println("decode xxxx " + xxxx);
        event.getClient().clearReadBuffer();
        event.getClient().writeSysCall();
    }


    private String byteBufferToString(ByteBuffer buffer) {
        CharBuffer charBuffer = null;
        try {
            buffer.flip();
            Charset charset = Charset.forName("UTF-8");
            CharsetDecoder decoder = charset.newDecoder();
            charBuffer = decoder.decode(buffer);
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
