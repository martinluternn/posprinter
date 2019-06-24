package com.reactlibrary.printer.printing;

import com.reactlibrary.printer.printing.interfaces.IDevice;
import com.reactlibrary.printer.printing.interfaces.IPrintingService;

public abstract  class DeviceConnectionStatusCallbacks {
    public abstract  void onStatusChanged(IDevice device, IPrintingService service);

}
