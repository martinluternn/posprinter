package com.reactlibrary.printer.printing;

import android.content.Context;

import com.reactlibrary.printer.printing.interfaces.IPrintingService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PrintingService implements IPrintingService {
    protected String identifier;
    protected InputStream inputStream;
    protected OutputStream outputStream;
    protected int status;
    protected Context context;
    protected DeviceConnectionStatusCallbacks callbacks;


    public PrintingService(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void setCallbacks(DeviceConnectionStatusCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public int getStatus() {
        return status;
    }

    public int write(byte[] data) {
        try {
            if (this.outputStream != null) {
                this.outputStream.write(data);
                this.outputStream.flush();
                return 0;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public byte[] read() {
        byte[] readBuff = null;
        try {
            int readLength = this.inputStream.available();
            if (this.inputStream != null && readLength > 0) {
                readBuff = new byte[readLength];
                this.inputStream.read(readBuff);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return toBytes(-1);
        }
        return readBuff;
    }

    byte[] toBytes(int i)
    {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);

        return result;
    }

    public synchronized byte[] read(int timeout) {
        byte[] receiveBytes = null;

        try {

            while (this.inputStream.available() <= 0) {
                timeout -= 50;
                if (timeout <= 0) {
                    break;
                }

                try {
                    Thread.sleep(50L);
                } catch (Exception var4) {
                    var4.printStackTrace();
                }
            }
            int readLength = this.inputStream.available();
            if (readLength > 0) {
                receiveBytes = new byte[readLength];
                this.inputStream.read(receiveBytes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return receiveBytes;
    }

}
