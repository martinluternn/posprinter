package com.reactlibrary.printer.printing.bluetooth;
import com.reactlibrary.printer.printing.interfaces.IDevice;
import com.reactlibrary.printer.printing.interfaces.IDeviceDiscoverer;
import com.reactlibrary.printer.printing.DiscoveryCallbacks;
import com.reactlibrary.printer.printing.DiscoveryStatus;
import com.reactlibrary.printer.printing.PrinterUtils;
import com.reactlibrary.printer.printing.SelectiveBroadcastReceiver;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BluetoothDiscoverer implements IDeviceDiscoverer {

    private DiscoveryCallbacks callbacks;
    private Map<String, BluetoothDevice> bluetoothDeviceDict = new HashMap<>();
    private BluetoothDiscoverer ref = this;
    private Context context;
    private BluetoothAdapter bluetoothAdapter;
    int status = DiscoveryStatus.NOT_READY;

    public BluetoothDiscoverer(Context activity) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = activity;
    }

    @Override
    public String getName() {
        return "Bluetooth";
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setCallbacks(DiscoveryCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    @Override
    public List<IDevice> getDevices() {
        stopDiscovery();
        List<IDevice> devicesForPrinting = new ArrayList<IDevice>();

        if (bluetoothAdapter == null) {
            return devicesForPrinting;
        }

        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
        if (pairedDevice.size() > 0) {
            for (BluetoothDevice device : pairedDevice) {
                String deviceIdentifier = BluetoothUtils.getIdentifier(device);
                bluetoothDeviceDict.put(deviceIdentifier, device);
            }
        }

        if (bluetoothDeviceDict.keySet().size() > 0) {
            for (String deviceIdentifier : bluetoothDeviceDict.keySet()) {
                devicesForPrinting.add(convertToBluetoothDevice(bluetoothDeviceDict.get(deviceIdentifier)));
            }
        }

        return devicesForPrinting;
    }

    private IDevice convertToBluetoothDevice(BluetoothDevice device) {
        return new BluetoothDeviceForPrinting(device, context);
    }

    private void getBondedDevices() {
        Set<BluetoothDevice> pairedDevice = bluetoothAdapter.getBondedDevices();
        if (pairedDevice.size() > 0) {
            for (BluetoothDevice device : pairedDevice) {
                String deviceIdentifier = BluetoothUtils.getIdentifier(device);
                bluetoothDeviceDict.put(deviceIdentifier, device);
                ref.callbacks.onDeviceDiscovered(ref.convertToBluetoothDevice(device));
            }
        }
    }

    @Override
    public void startScanningDevices() {
        setStatus(DiscoveryStatus.NOT_READY);
        this.bluetoothDeviceDict.clear();
        if (!isBluetoothOn()) {
            if (bluetoothAdapter != null) {
                bluetoothAdapter.enable();
            } else {
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableBTIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(enableBTIntent);
                IntentFilter actionStateChangedIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                PrinterUtils.reregister(context, broadcastReceiverForStateChanged, actionStateChangedIntent);
            }

            if (!isBluetoothOn()) {
                Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                enableBTIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(enableBTIntent);
                IntentFilter actionStateChangedIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                PrinterUtils.reregister(context, broadcastReceiverForStateChanged, actionStateChangedIntent);
            }
        } else {
            activateBluetoothDiscovery();
        }
    }

    private void activateBluetoothDiscovery() {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
        discoverableIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(discoverableIntent);
        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        PrinterUtils.reregister(context, broadcastReceiverForScanModeChange, intentFilter);
    }

    private void activateBluetoothReceiver() {
        bluetoothDeviceDict.clear();
//        this.getBondedDevices();
        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        PrinterUtils.reregister(context, broadcastReceiverForActionFound, discoverDevicesIntent);
        restartDiscovery();

    }

    private BroadcastReceiver broadcastReceiverForScanModeChange = new SelectiveBroadcastReceiver(
            BluetoothAdapter.ACTION_SCAN_MODE_CHANGED) {
        @Override
        public void onReceiveFilteredIntent(Context context, Intent intent) {
            int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

            switch (mode) {
                // Device is in Discoverable Mode
                case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                    activateBluetoothReceiver();
                    ref.setStatus(DiscoveryStatus.IS_DISCOVERING);
                    break;
                // Device not in discoverable mode
                case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                    ref.setStatus(DiscoveryStatus.READY);
                    break;
                case BluetoothAdapter.SCAN_MODE_NONE:
                    break;
                case BluetoothAdapter.STATE_CONNECTING:
                    break;
                case BluetoothAdapter.STATE_CONNECTED:
                    break;
            }
        }
    };

    private BroadcastReceiver broadcastReceiverForStateChanged = new SelectiveBroadcastReceiver(
            BluetoothAdapter.ACTION_STATE_CHANGED) {
        @Override
        public void onReceiveFilteredIntent(Context context, Intent intent) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    break;
                case BluetoothAdapter.STATE_ON:
                    activateBluetoothDiscovery();
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    break;
            }
        }
    };

    private BroadcastReceiver broadcastReceiverForActionFound = new SelectiveBroadcastReceiver(
            BluetoothDevice.ACTION_FOUND) {
        @Override
        public void onReceiveFilteredIntent(Context context, Intent intent) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String deviceIdentifier = BluetoothUtils.getIdentifier(device);
            bluetoothDeviceDict.put(deviceIdentifier, device);
            ref.callbacks.onDeviceDiscovered(ref.convertToBluetoothDevice(device));
        }
    };

    public boolean isBluetoothOn() {
        return bluetoothAdapter.isEnabled();
    }

    private void restartDiscovery() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
    }

    private void setStatus(int status) {
        this.status = status;
        if (callbacks != null) {
            callbacks.onStatusChanged(this);
        }
    }

    private void stopDiscovery() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    @Override
    public void stopScanningDevices() {
        stopDiscovery();
    }
}
