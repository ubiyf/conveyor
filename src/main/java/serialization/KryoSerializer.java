package serialization;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.ByteBufferInput;
import com.esotericsoftware.kryo.io.ByteBufferOutput;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.nio.ByteBuffer;

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
public class KryoSerializer implements Serializer {

    private Kryo k = new Kryo();

    private ByteBufferOutput out = new ByteBufferOutput();

    private ByteBufferInput in = new ByteBufferInput();

    public byte[] ser(Object obj) {
        byte [] result = null;
        if (obj != null) {
            k.writeObject(out, obj);
        }
        return result;
    }

    @Override
    public Object deser(ByteBuffer inBuffer) {
        in.setBuffer(inBuffer, 0, inBuffer.position());
        return k.readClassAndObject(in);
    }

    @Override
    public void ser(ByteBuffer outBuffer, Object obj) {
        out.setBuffer(outBuffer, outBuffer.limit());
        k.writeClassAndObject(out, obj);
    }
}
