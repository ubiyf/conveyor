package log;

import java.io.FileInputStream;
import java.util.Properties;

public class AioLoggerConfig {
	
	private static AioLoggerLevel level = AioLoggerLevel.DEBUG;
	
	private static int fileSize = 100 * 1024 * 1024;
	
	private static String fileNamePrefix = "nb-logger";
	
	private static boolean enableConsole = true;
	
	private static boolean enableFile = true;
	
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error("load conf failed:" + configFileName);
		}
	}
	
	private static boolean isBlankString(String str) {
		if (null == str || "".equals(str)) {
			return true;
		} else {
			return false;
		}
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
}
