package com.reactlibrary.printer.printing.interfaces;

import com.reactlibrary.printer.printing.DeviceConnectionStatusCallbacks;

public interface IDevice {
    public abstract String getIdentifier();
    public abstract String getDisplayName();
    public abstract IPrintingService startService(DeviceConnectionStatusCallbacks callbacks) throws Exception;
}

