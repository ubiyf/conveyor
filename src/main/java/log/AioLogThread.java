package log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class AioLogThread {
	
	private ByteBuffer buffer;

	private long fileSize = 100 * 1024 * 1024L;
	
	private String logFileNamePrefix;
	
	private int logFileNum;
	
	private long sizeCounter;
	
	private RandomAccessFile raf;

	private static final AioLogThread instance = new AioLogThread();
	
	private AioLogThread() {
		buffer = ByteBuffer.allocate(256 * 1024);
		fileSize = AioLoggerConfig.getFileSize();
		logFileNamePrefix = AioLoggerConfig.getFileNamePrefix();
		initLogFile();
//		this.registerMessageHandler(AioLogMsg.class, this);
	}
	
	public static AioLogThread getInstance() {
		return instance;
	}
	
	public void setFileNamePrefix(String nPrefix) {
		logFileNamePrefix = nPrefix;
		
	}
	
	public void initLogFile() {
		logFileNum = 0;
		sizeRolling();
	}
	
	private void checkRolling() {
		// check size
		try {
			if (raf.length() >= fileSize) {
				sizeRolling();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeLog(String logMsg) {
		checkRolling();
		int offset = 0;
		int chunk = 0;
		final byte[] bytes = logMsg.getBytes();
		int length = bytes.length;
		sizeCounter = sizeCounter + length;
        do {
            if (length > buffer.remaining()) {
                flush();
            }
            chunk = Math.min(length, buffer.remaining());
            buffer.put(bytes, offset, chunk);
            offset += chunk;
            length -= chunk;
        } while (length > 0);

        flush();
	}
	
	public void flush() {
        buffer.flip();
        try {
            raf.write(buffer.array(), 0, buffer.limit());
        } catch (final IOException ex) {
//            final String msg = "Error writing to RandomAccessFile " + getName();
//            throw new RuntimeException(msg, ex);
        }
        buffer.clear();
    }
	
	private void sizeRolling() {
		logFileNum ++;
		sizeCounter = 0L;
		
		String fileName = getLogFileName();
		File f = new File(fileName);
		if (!f.exists() || f.length() < fileSize) {
			try {
				raf = new RandomAccessFile(f, "rw");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				raf.seek(raf.length());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			sizeRolling();
		}
	}
	
	private String getLogFileName() {
		String index = num2String(logFileNum);
		return logFileNamePrefix + "-" + index + ".log";
	}
	
	private String num2String(int num) {
		String result = null;
		if (logFileNum < 10) {
			result = "0" + num;
		} else {
			result = "" + num;
		}
		return result;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	public void onMessage(AioLogMsg message) {
		AioLogMsg logMsg = (AioLogMsg) message;
		String log = logMsg.getLog();
		if (AioLoggerConfig.isEnableFile()) {
			writeLog(log);
		}
		if (AioLoggerConfig.isEnableConsole()) {
			System.out.print(log);
		}
	}

}
