package compute;

import com.lmax.disruptor.EventFactory;

public class ComputeEventFactory implements EventFactory<ComputeEvent> {
    @Override
    public ComputeEvent newInstance() {
        return new ComputeEvent();
    }
}
