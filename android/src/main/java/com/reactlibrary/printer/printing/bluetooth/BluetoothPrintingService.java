package com.reactlibrary.printer.printing.bluetooth;


import android.annotation.TargetApi;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.reactlibrary.printer.printing.interfaces.IPrintingService;
import com.reactlibrary.printer.printing.DeviceConnectionStatus;
import com.reactlibrary.printer.printing.PrinterUtils;
import com.reactlibrary.printer.printing.PrintingService;
import com.reactlibrary.printer.printing.SelectiveBroadcastReceiver;

public class BluetoothPrintingService extends PrintingService implements IPrintingService {
    private static final String TAG = "BluetoothPort";
    private BluetoothDeviceForPrinting deviceForPrinting;
    private BluetoothSocket socket;
    private BluetoothPrintingService ref = this;

    private BroadcastReceiver broadcastReceiverForStateChanged =
            new SelectiveBroadcastReceiver(BluetoothDevice.ACTION_BOND_STATE_CHANGED) {
                @Override
                public void onReceiveFilteredIntent(Context context, Intent intent) {
                    BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if (!ref.deviceForPrinting.device.equals(device)) {
                        return;
                    }
                    switch (device.getBondState()) {
                        case BluetoothDevice.BOND_NONE:
                            setStatus(DeviceConnectionStatus.FAILED);
                            break;
                        case BluetoothDevice.BOND_BONDING:
                            break;
                        case BluetoothDevice.BOND_BONDED:
                            try {
                                connect();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                    }
                }
            };


    public BluetoothPrintingService(BluetoothDeviceForPrinting deviceForPrinting, Context context) {
        super(deviceForPrinting.getIdentifier());
        this.deviceForPrinting = deviceForPrinting;
        setStatus(DeviceConnectionStatus.CLOSED);
        this.context = context;
    }

    public void open() throws Exception {
        if (deviceForPrinting.getBondState() == BluetoothDevice.BOND_NONE) {
            pair();
        } else if (deviceForPrinting.getBondState() == BluetoothDevice.BOND_BONDED) {
            tryConnect();
        }
    }

    private boolean pair() {
        close();
        IntentFilter boundFilter = new IntentFilter(
                BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        PrinterUtils.reregister(context, broadcastReceiverForStateChanged, boundFilter);
        return deviceForPrinting.createBond();
    }

    private void tryConnect() throws Exception {
        close();
        try {
            this.connect();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void prepareSocket() throws Exception {
        close();
        boolean isSuccessful = false;
        String errMsg = "";
        try {
            connectByDefault();
            isSuccessful = true;
        } catch (Exception e) {
            e.printStackTrace();
            errMsg = e.getMessage();
        }
        if (!isSuccessful) {
            try {
                connectAlternatively();
                isSuccessful = true;
            } catch (Exception e) {
                e.printStackTrace();
                errMsg = e.getMessage();
            }
        }
        if (!isSuccessful) {
            setStatus(DeviceConnectionStatus.FAILED);
            throw new Exception(errMsg);
        }
    }

    private void connect() throws Exception {
        if (socket == null || status != DeviceConnectionStatus.CONNECTED) {
            prepareSocket();
        }
        setStatus(DeviceConnectionStatus.CONNECTED, true);
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (Exception e) {
            close();
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    private void connectByDefault() throws Exception {
        socket = this.deviceForPrinting.createInsecureRfcommSocketToServiceRecord();
        socket.connect();
    }

    @TargetApi(10)
    private void connectAlternatively() throws Exception {
        socket = this.deviceForPrinting.createRfcommSocket();
        socket.connect();
    }

    public int write(byte[] data) {
        try {
            if (outputStream != null) {
                outputStream.write(data);
                outputStream.flush();
                return 0;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void setStatus(int state) {
        setStatus(state, false);
    }

    private void setStatus(int state, boolean hasExplicitCallback) {
        int oldStatus = this.status;
        this.status = state;
        boolean needToCallback = hasExplicitCallback || oldStatus != this.status;
        if (callbacks != null && needToCallback) {
            callbacks.onStatusChanged(this.deviceForPrinting, this);
        }
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket = null;
        setStatus(DeviceConnectionStatus.CLOSED);
    }
}
