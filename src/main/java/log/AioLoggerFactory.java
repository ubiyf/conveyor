package log;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AioLoggerFactory implements ILoggerFactory {

    ConcurrentMap<String, Logger> loggerMap;

    public AioLoggerFactory() {
        loggerMap = new ConcurrentHashMap<>();
    }

    @Override
    public Logger getLogger(String name) {
        Logger slf4jLogger = loggerMap.get(name);
        if (slf4jLogger != null) {
            return slf4jLogger;
        } else {
            AioLogger log4jLogger = new AioLogger(name);

            Logger newInstance = new AioLoggerAdapter(log4jLogger);
            Logger oldInstance = loggerMap.putIfAbsent(name, newInstance);
            return oldInstance == null ? newInstance : oldInstance;
        }
    }

}
