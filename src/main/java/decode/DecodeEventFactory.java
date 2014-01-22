package decode;

import com.lmax.disruptor.EventFactory;

public class DecodeEventFactory implements EventFactory<DecodeEvent> {

    private static final DecodeEventFactory INSTANCE = new DecodeEventFactory();

    public static DecodeEventFactory getInstance() {
        return INSTANCE;
    }

    private DecodeEventFactory () {}

    @Override
    public DecodeEvent newInstance() {
        return new DecodeEvent();
    }
}
