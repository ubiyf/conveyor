package serialization;
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
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class StringSerializer implements Serializer {

    private static final StringSerializer INSTANCE = new StringSerializer();

    public static StringSerializer getInstance() {
        return INSTANCE;
    }

    private StringSerializer() {
    }

    @Override
    public Object toObject(ByteBuffer inBuffer) {
        CharBuffer charBuffer;
        try {
            inBuffer.flip();
            CharsetDecoder decoder = StringThreadLocal.getLocalDecoder();
            charBuffer = decoder.decode(inBuffer);
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void toByte(ByteBuffer outBuffer, Object obj) {
        CharBuffer charBuffer = CharBuffer.wrap(obj.toString());
        try {
            CharsetEncoder encoder = StringThreadLocal.getLocalEncoder();
            encoder.encode(charBuffer, outBuffer, true);
            outBuffer.flip();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
