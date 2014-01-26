package log;

import com.lmax.disruptor.EventHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/*
 * Copyright 2014 Yang Fan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class AioLogEventHandler implements EventHandler<AioLogEvent> {

    private static final AioLogEventHandler INSTANCE = new AioLogEventHandler();

    private ByteBuffer buffer;

    private long fileSize = 100 * 1024 * 1024L;

    private String logFileNamePrefix;

    private int logFileNum;

    private long sizeCounter;

    private RandomAccessFile raf;

    public static AioLogEventHandler getInstance() {
        return INSTANCE;
    }

    private AioLogEventHandler() {
        buffer = ByteBuffer.allocate(256 * 1024);
        fileSize = AioLoggerConfig.getFileSize();
        logFileNamePrefix = AioLoggerConfig.getFileNamePrefix();
        initLogFile();
    }

    @Override
    public void onEvent(AioLogEvent event, long sequence, boolean endOfBatch) throws Exception {
        String log = event.getMessage();
        if (AioLoggerConfig.isEnableFile()) {
            writeLog(log);
        }
        if (AioLoggerConfig.isEnableConsole()) {
            System.out.print(log);
        }
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
        String result;
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

}
