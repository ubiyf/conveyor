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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * thread safe for multi-threads, adopt thread local variable.
 * NOTICE:
 * before invoke toObject and toByte method
 * please set the Kryo message package name at first
 * otherwise it will throw a runtime exception
 */
public class KryoSerializer implements Serializer {

    private static final KryoSerializer INSTANCE = new KryoSerializer();

    public static KryoSerializer getInstance() {
        return INSTANCE;
    }

    private KryoSerializer() {
    }

    public static void registerKryoClasses(String kryoMessagePackageName) throws IOException, ClassNotFoundException {
        KryoThreadLocal.initRegistration(kryoMessagePackageName);
    }

    @Override
    public Object toObject(ByteBuffer inBuffer) {
        Kryo k = KryoThreadLocal.getLocalRegisteredKryo();
        ByteBufferInput in = KryoThreadLocal.getLocalInput();
        in.setBuffer(inBuffer, 0, inBuffer.position());
        return k.readClassAndObject(in);
    }

    @Override
    public void toByte(ByteBuffer outBuffer, Object obj) {
        Kryo k = KryoThreadLocal.getLocalRegisteredKryo();
        ByteBufferOutput out = KryoThreadLocal.getLocalOutput();
        out.setBuffer(outBuffer, outBuffer.limit());
        k.writeClassAndObject(out, obj);
    }

    public <T> T copy(T obj) {
        Kryo k = KryoThreadLocal.getLocalDefaultKryo();
        return k.copy(obj);
    }

}
