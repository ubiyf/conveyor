package log;

import java.util.Date;

public class AioLogger {
	
	private String loggerName;

	public AioLogger(String loggerName) {
		super();
		this.loggerName = loggerName;
	}
	
	public void writeLog(String logMsg, AioLoggerLevel level) {
		String detailMsg = addLogPrefix(logMsg, level);
		AioLogMsg wrappedMsg = new AioLogMsg(detailMsg);
	}
	
	public String getName() {
		return loggerName;
	}
	
	private String addLogPrefix(String logMsg, AioLoggerLevel level) {
		Date d = new Date();
		return String.format("%1$tF %1$tT:%1$tL [%2$s] %3$s %4$s %5$s\r\n", d, level, Thread.currentThread().getName(), loggerName, logMsg);
	}
	
}
