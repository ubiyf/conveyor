package compute;

import aio.AioClient;
import com.lmax.disruptor.EventTranslatorOneArg;

public class ComputeEventTranslator implements EventTranslatorOneArg<ComputeEvent, AioClient> {
    @Override
    public void translateTo(ComputeEvent event, long sequence, AioClient client) {
        event.setClient(client);
    }
}
