package com.reactlibrary.printer.printing.bluetooth;

import android.bluetooth.BluetoothDevice;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class BluetoothUtils {
    public static final UUID PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    public static <T> T invokeSafely(Object o, BluetoothDevice device, String methodName, T defaultValue, Object... args ){
        try{
            return BluetoothUtils.invoke(o,device,methodName,args);
        }catch (Exception e){
            return defaultValue;
        }
    }

    public static <T> T invoke(Object o, BluetoothDevice device, String methodName, Object... args) throws Exception {
        Method createBondMethod = device.getClass().getMethod(methodName);
        return (T)createBondMethod.invoke(device, args);
    }

    public static String getIdentifier(BluetoothDevice device){
        return device.getName() + '-'+device.getAddress();
    }
    public static String getDisplayName(BluetoothDevice device){
        return device.getName();
    }
}
