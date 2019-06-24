package com.reactlibrary.printer.printing.interfaces;

import com.reactlibrary.printer.printing.DiscoveryCallbacks;

import java.util.List;

public interface IDeviceDiscoverer {

    public abstract String getName();

    public abstract int getStatus();

    public abstract void setCallbacks(DiscoveryCallbacks callbacks);

    public abstract List<IDevice> getDevices();

    public abstract void startScanningDevices();

    public abstract void stopScanningDevices();

}

