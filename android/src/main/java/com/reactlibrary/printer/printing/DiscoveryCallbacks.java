package com.reactlibrary.printer.printing;

import com.reactlibrary.printer.printing.interfaces.IDevice;
import com.reactlibrary.printer.printing.interfaces.IDeviceDiscoverer;

public abstract  class DiscoveryCallbacks {
    public abstract  void onStatusChanged(IDeviceDiscoverer discoverer);
    public abstract void onDeviceDiscovered(IDevice device);
}
