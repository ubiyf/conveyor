package decode;

import aio.AioClient;
import com.lmax.disruptor.EventTranslatorOneArg;

public class DecodeEventTranslator implements EventTranslatorOneArg<DecodeEvent, AioClient> {

    private static final DecodeEventTranslator INSTANCE = new DecodeEventTranslator();

    private DecodeEventTranslator() {}

    public static DecodeEventTranslator getInstance() {
        return INSTANCE;
    }

    @Override
    public void translateTo(DecodeEvent event, long sequence, AioClient arg0) {
        event.setClient(arg0);
    }

}
