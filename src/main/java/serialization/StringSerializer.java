package serialization;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

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
/**
 * Not thread safe
 */
public class StringSerializer implements Serializer {

    private static final String CHARSET_NAME = "UTF-8";

    private final Charset charset;

    private final CharsetDecoder decoder;

    private final CharsetEncoder encoder;

    public StringSerializer() {
        charset = Charset.forName(CHARSET_NAME);
        decoder = charset.newDecoder();
        encoder = charset.newEncoder();
    }

    @Override
    public Object deser(ByteBuffer inBuffer) {
        CharBuffer charBuffer = null;
        try {
            inBuffer.flip();

            charBuffer = decoder.decode(inBuffer);
            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public void ser(ByteBuffer outBuffer, Object obj) {
        CharBuffer charBuffer = CharBuffer.wrap(obj.toString());
        try {
            encoder.encode(charBuffer, outBuffer, true);
            outBuffer.flip();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
