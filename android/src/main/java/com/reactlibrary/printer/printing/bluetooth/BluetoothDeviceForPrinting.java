package com.reactlibrary.printer.printing.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.reactlibrary.printer.printing.interfaces.IDevice;
import com.reactlibrary.printer.printing.interfaces.IPrintingService;
import com.reactlibrary.printer.printing.DeviceConnectionStatusCallbacks;


public class BluetoothDeviceForPrinting implements IDevice {

    public BluetoothDevice device;
    public Context context;
    private BluetoothPrintingService service;

    @Override
    public String getIdentifier() {
        return BluetoothUtils.getIdentifier(device);
    }

    public String getDisplayName() {
        return BluetoothUtils.getDisplayName(device);
    }

    public BluetoothDeviceForPrinting(BluetoothDevice device, Context activity) {
        this.device = device;
        this.context = activity;
    }

    public int getBondState() {
        return device.getBondState();
    }

    public boolean createBond() {
        return BluetoothUtils.invokeSafely(this, device, "createBond", false);
    }

    public BluetoothSocket createRfcommSocket() throws Exception {
        return device.createRfcommSocketToServiceRecord(BluetoothUtils.PRINTER_UUID);
    }

    public BluetoothSocket createInsecureRfcommSocketToServiceRecord() throws Exception {
        return device.createInsecureRfcommSocketToServiceRecord(BluetoothUtils.PRINTER_UUID);
    }


    @Override
    public IPrintingService startService(DeviceConnectionStatusCallbacks callbacks) throws Exception {
        if (service != null) {
            service.close();
        }
        service = new BluetoothPrintingService(this, context);
        Log.i("BluetoothDevice", "create BluetoothPrintingService");
        service.setCallbacks(callbacks);
        service.open();
        return service;
    }
}
