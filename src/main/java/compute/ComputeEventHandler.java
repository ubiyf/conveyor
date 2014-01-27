package compute;

import aio.AioClient;
import com.lmax.disruptor.EventHandler;
import protocol.NetworkMessage;
import protocol.NetworkMessageHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ComputeEventHandler implements EventHandler<ComputeEvent> {

    private static final ComputeEventHandler INSTANCE = new ComputeEventHandler();

    private Map<Class, NetworkMessageHandler> networkMessageHandlers = new ConcurrentHashMap<>();

    private ComputeEventHandler() {
    }

    public static ComputeEventHandler getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEvent(ComputeEvent event, long sequence, boolean endOfBatch) throws Exception {
        AioClient client = event.getClient();
        Object networkMessage = client.readNetworkMessage();
        if (networkMessage instanceof NetworkMessage) {
            // if you write network message manually as a Java class
            // eg : Kryo or Json
            // you can implement your business logic in the process() method of NetworkMessage interface
            ((NetworkMessage) networkMessage).process(client);
        } else {
            // else you write network message as a prototype file, then generate Java class automatically
            // eg : protobuf/protostuff or thrift
            // you can register the involved handlers before server start and get it here
            Class networkMessageClass = networkMessage.getClass();
            NetworkMessageHandler nmh = networkMessageHandlers.get(networkMessageClass);
            if (nmh != null) {
                nmh.handle(networkMessage, client);
            } else {
                throw new RuntimeException("There is no handler for network message " + networkMessageClass);
            }
        }
    }

    public void registerNetworkMessageHandler(Class clazz, NetworkMessageHandler handler) {
        networkMessageHandlers.put(clazz, handler);
    }
}
