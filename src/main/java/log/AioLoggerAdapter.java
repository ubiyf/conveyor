package log;

import org.slf4j.Logger;
import org.slf4j.Marker;

public class AioLoggerAdapter implements Logger {

    private final AioLogger logger;

    public AioLoggerAdapter(AioLogger logger) {
        super();
        this.logger = logger;
    }

    @Override
    public void debug(String arg0) {
        if (isDebugEnabled()) {
            logger.writeLog(arg0, AioLoggerLevel.DEBUG);
        }
    }

    @Override
    public void debug(String arg0, Object arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(String arg0, Object... arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(String arg0, Throwable arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(Marker arg0, String arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(String arg0, Object arg1, Object arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(Marker arg0, String arg1, Object arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(Marker arg0, String arg1, Object... arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(Marker arg0, String arg1, Throwable arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void debug(Marker arg0, String arg1, Object arg2, Object arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(String arg0) {
        if (isErrorEnabled()) {
            logger.writeLog(arg0, AioLoggerLevel.ERROR);
        }
    }

    @Override
    public void error(String arg0, Object arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(String arg0, Object... arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(String arg0, Throwable arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(Marker arg0, String arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(String arg0, Object arg1, Object arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(Marker arg0, String arg1, Object arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(Marker arg0, String arg1, Object... arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(Marker arg0, String arg1, Throwable arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void error(Marker arg0, String arg1, Object arg2, Object arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public void info(String arg0) {
        if (isInfoEnabled()) {
            logger.writeLog(arg0, AioLoggerLevel.INFO);
        }
    }

    @Override
    public void info(String arg0, Object arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(String arg0, Object... arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(String arg0, Throwable arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(Marker arg0, String arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(String arg0, Object arg1, Object arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(Marker arg0, String arg1, Object arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(Marker arg0, String arg1, Object... arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(Marker arg0, String arg1, Throwable arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void info(Marker arg0, String arg1, Object arg2, Object arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isDebugEnabled() {
        if (AioLoggerConfig.getLevel().getValue() >= AioLoggerLevel.DEBUG.getValue()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isDebugEnabled(Marker arg0) {
        return false;
    }

    @Override
    public boolean isErrorEnabled() {
        if (AioLoggerConfig.getLevel().getValue() >= AioLoggerLevel.ERROR.getValue()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isErrorEnabled(Marker arg0) {
        return false;
    }

    @Override
    public boolean isInfoEnabled() {
        if (AioLoggerConfig.getLevel().getValue() >= AioLoggerLevel.INFO.getValue()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isInfoEnabled(Marker arg0) {
        return false;
    }

    @Override
    public boolean isTraceEnabled() {
        if (AioLoggerConfig.getLevel().getValue() >= AioLoggerLevel.TRACE.getValue()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isTraceEnabled(Marker arg0) {
        return false;
    }

    @Override
    public boolean isWarnEnabled() {
        if (AioLoggerConfig.getLevel().getValue() >= AioLoggerLevel.WARN.getValue()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isWarnEnabled(Marker arg0) {
        return false;
    }

    @Override
    public void trace(String arg0) {
        if (isTraceEnabled()) {
            logger.writeLog(arg0, AioLoggerLevel.TRACE);
        }
    }

    @Override
    public void trace(String arg0, Object arg1) {

    }

    @Override
    public void trace(String arg0, Object... arg1) {

    }

    @Override
    public void trace(String arg0, Throwable arg1) {

    }

    @Override
    public void trace(Marker arg0, String arg1) {

    }

    @Override
    public void trace(String arg0, Object arg1, Object arg2) {

    }

    @Override
    public void trace(Marker arg0, String arg1, Object arg2) {

    }

    @Override
    public void trace(Marker arg0, String arg1, Object... arg2) {

    }

    @Override
    public void trace(Marker arg0, String arg1, Throwable arg2) {

    }

    @Override
    public void trace(Marker arg0, String arg1, Object arg2, Object arg3) {

    }

    @Override
    public void warn(String arg0) {
        if (isWarnEnabled()) {
            logger.writeLog(arg0, AioLoggerLevel.WARN);
        }
    }

    @Override
    public void warn(String arg0, Object arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(String arg0, Object... arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(String arg0, Throwable arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(Marker arg0, String arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(String arg0, Object arg1, Object arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(Marker arg0, String arg1, Object arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(Marker arg0, String arg1, Object... arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(Marker arg0, String arg1, Throwable arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void warn(Marker arg0, String arg1, Object arg2, Object arg3) {
        // TODO Auto-generated method stub

    }

}
