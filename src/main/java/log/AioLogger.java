package log;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.Date;
import java.util.concurrent.Executors;

public class AioLogger {
	
	private String loggerName;

    private static final Disruptor<AioLogEvent> logDisruptor = new Disruptor<>(
            AioLogEventFactory.getInstance(),
            AioLoggerConfig.getLogMsgBufferSize(),
            Executors.newFixedThreadPool(1),
            ProducerType.MULTI,
            new BlockingWaitStrategy());

    static {
        logDisruptor.handleEventsWith(AioLogEventHandler.getInstance());
        logDisruptor.start();
    }

	public AioLogger(String loggerName) {
		super();
		this.loggerName = loggerName;
	}
	
	public void writeLog(String logMsg, AioLoggerLevel level) {
		String detailMsg = addLogPrefix(logMsg, level);
        logDisruptor.publishEvent(AioLogEventTransaltor.getInstance(), detailMsg);
	}
	
	public String getName() {
		return loggerName;
	}
	
	private String addLogPrefix(String logMsg, AioLoggerLevel level) {
		Date d = new Date();
		return String.format("%1$tF %1$tT:%1$tL [%2$s] %3$s %4$s %5$s\r\n", d, level, Thread.currentThread().getName(), loggerName, logMsg);
	}
	
}
