package compute;

import com.lmax.disruptor.EventHandler;

public class ComputeEventHandler implements EventHandler<ComputeEvent> {



    @Override
    public void onEvent(ComputeEvent event, long sequence, boolean endOfBatch) throws Exception {

    }
}
