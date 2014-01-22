package decode;

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
        event.getClient().wantWrite();
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
