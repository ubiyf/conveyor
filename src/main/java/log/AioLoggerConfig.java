package log;

import java.io.FileInputStream;
import java.util.Properties;

public class AioLoggerConfig {

    private static AioLoggerLevel level = AioLoggerLevel.DEBUG;

    private static int fileSize = 100 * 1024 * 1024;

    private static String fileNamePrefix = "aio-logger";

    private static boolean enableConsole = true;

    private static boolean enableFile = true;

    private static int logMsgBufferSize = 1024;

    public static void loadConfig(String configFileName) {
        Properties properties = new Properties();
        FileInputStream inStream;
        try {
            inStream = new FileInputStream(configFileName);
            properties.load(inStream);
            String levelStr = properties.getProperty("log_level");
            if (!isBlankString(levelStr)) {
                level = AioLoggerLevel.valueOf(levelStr);
            }
            String fnpStr = properties.getProperty("log_file_name");
            if (!isBlankString(fnpStr)) {
                fileNamePrefix = fnpStr;

            }
            String lfsStr = properties.getProperty("log_file_size");
            if (!isBlankString(lfsStr)) {
                fileSize = Integer.parseInt(lfsStr);
            }
            String consoleStr = properties.getProperty("log_enable_console");
            if ("false".equals(consoleStr)) {
                enableConsole = false;
            }
            String fileStr = properties.getProperty("log_enable_file");
            if ("false".equals(fileStr)) {
                enableFile = false;
            }
            String msgBufferStr = properties.getProperty("log_msg_buffer_size");
            if (!isBlankString(msgBufferStr)) {
                logMsgBufferSize = Integer.parseInt(msgBufferStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("load conf failed:" + configFileName);
        }
    }

    private static boolean isBlankString(String str) {
        return null == str || "".equals(str);
    }

    public static AioLoggerLevel getLevel() {
        return level;
    }

    public static int getFileSize() {
        return fileSize;
    }

    public static String getFileNamePrefix() {
        return fileNamePrefix;
    }

    public static boolean isEnableConsole() {
        return enableConsole;
    }

    public static boolean isEnableFile() {
        return enableFile;
    }

    public static int getLogMsgBufferSize() {
        return logMsgBufferSize;
    }
}
