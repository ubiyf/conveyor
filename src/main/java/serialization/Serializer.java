package serialization;

import java.nio.ByteBuffer;

public interface Serializer {

    Object deser(ByteBuffer inBuffer);

    void ser(ByteBuffer outBuffer, Object obj);

}
