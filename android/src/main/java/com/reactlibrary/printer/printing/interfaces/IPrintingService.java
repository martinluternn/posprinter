package com.reactlibrary.printer.printing.interfaces;

import com.reactlibrary.printer.printing.DeviceConnectionStatusCallbacks;

public interface IPrintingService {
    public byte[] read();
    public int write(byte[] data);
    public void setCallbacks(DeviceConnectionStatusCallbacks callbacks);
    public int getStatus();
}
