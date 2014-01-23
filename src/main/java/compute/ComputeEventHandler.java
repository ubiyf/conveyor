package compute;

import com.lmax.disruptor.EventHandler;

public class ComputeEventHandler implements EventHandler<ComputeEvent> {

    @Override
    public void onEvent(ComputeEvent event, long sequence, boolean endOfBatch) throws Exception {
        // TODO decode the network message

        // TODO get involved network message handler

        // TODO invoke the handle method to do the business
    }

}
