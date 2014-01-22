package serialization;

import java.nio.ByteBuffer;

/**
 * Can not guarantee thread safe
 */
public interface Serializer {

    Object deser(ByteBuffer inBuffer);

    void ser(ByteBuffer outBuffer, Object obj);

}
