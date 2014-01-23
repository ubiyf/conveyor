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

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

class StringThreadLocal {

    private static final String CHARSET_NAME = "UTF-8";

    private static final ThreadLocal<Charset> CHARSET = new ThreadLocal<>();

    private static final ThreadLocal<CharsetDecoder> DECODER = new ThreadLocal<>();

    private static final ThreadLocal<CharsetEncoder> ENCODER = new ThreadLocal<>();

    public static Charset getLocalCharset() {
        Charset c = CHARSET.get();
        if (c == null) {
            c = Charset.forName(CHARSET_NAME);
            CHARSET.set(c);
        }
        return c;
    }

    public static CharsetDecoder getLocalDecoder() {
        CharsetDecoder d = DECODER.get();
        if (d == null) {
            Charset c = getLocalCharset();
            d = c.newDecoder();
            DECODER.set(d);
        }
        return d;
    }

    public static CharsetEncoder getLocalEncoder() {
        CharsetEncoder e = ENCODER.get();
        if (e == null) {
            Charset c = getLocalCharset();
            e = c.newEncoder();
            ENCODER.set(e);
        }
        return e;
    }
}
