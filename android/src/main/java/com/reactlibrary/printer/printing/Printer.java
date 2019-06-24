package com.reactlibrary.printer.printing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.android.print.sdk.Barcode;
import com.android.print.sdk.CanvasPrint;
import com.android.print.sdk.PrinterType;
import com.android.print.sdk.Table;
import com.android.print.sdk.util.Utils;
import com.reactlibrary.R;
import com.reactlibrary.printer.printing.interfaces.IPrintingService;

import java.io.File;
import java.io.FileInputStream;

public class Printer {
    private IPrintingService service;
    private static final long serialVersionUID = 1L;
    private static String TAG = "PrinterInstance";
    private String charsetName = "gbk";
    private final String SDK_VERSION = "3.0";

    public Printer(IPrintingService service) {
        this.service = service;
    }

    public Printer() {
    }

    public void setService(IPrintingService service) {
        this.service = service;
    }

    public String getEncoding() {
        return this.charsetName;
    }

    public void setEncoding(String charsetName) {
        this.charsetName = charsetName;
    }

    public String getSDK_Vesion() {
        return "3.0";
    }

    public boolean isConnected() {
        if (this.service == null) {
            return false;
        }

        return this.service.getStatus() == DeviceConnectionStatus.CONNECTED;
    }

    public int printText(String content) {
        byte[] data = null;

        try {
            if (this.charsetName != "") {
                data = content.getBytes(this.charsetName);
            } else {
                data = content.getBytes();
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return this.sendByteData(data);
    }

    public int sendByteData(byte[] data) {
        if (data != null) {
            return this.service.write(data);
        } else {
            return -1;
        }
    }

    public int printImageFromStorage(final String url) {
        byte[] data = null;

        File file = new File(url);
        FileInputStream streamIn;
        Bitmap bitmap = null;
        try {
            streamIn = new FileInputStream(file);
            bitmap = BitmapFactory.decodeStream(streamIn);

            streamIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bitmap != null) {
            Bitmap bmScale = Bitmap.createScaledBitmap(bitmap, 250, 250, false);
            Bitmap blackWhiteBitmap = PrinterImageUtils.convertToBlackAndWhiteImage(PrinterImageUtils.addPadding(bmScale, 69, 0));
            data = PrinterImageUtils.decodeBitmap(blackWhiteBitmap);
        }
        return this.sendByteData(data);
    }

    public int setFont(int mWidth, int mHeight, int mBold, int mUnderline) {
        int mFontSize = 0;
        int mFontMode = 0;
        int mRetVal = 0;
        if (mBold != 0 && mBold != 1) {
            mRetVal = 3;
        } else {
            mFontMode |= mBold << 3;
        }

        if (mUnderline != 0 && mUnderline != 1) {
            mRetVal = 4;
        } else {
            mFontMode |= mUnderline << 7;
        }

        this.setPrinter(16, mFontMode);
        if (mWidth >= 0 && mWidth <= 7) {
            mFontSize |= mWidth << 4;
        } else {
            mRetVal = 1;
        }

        if (mHeight >= 0 && mHeight <= 7) {
            mFontSize |= mHeight;
        } else {
            mRetVal = 2;
        }

        this.setPrinter(17, mFontSize);
        return mRetVal;
    }

    public int printTable(Table table) {
        return this.printText(table.getTableText());
    }

    public void init() {
        this.setPrinter(0);
    }

    public byte[] read() {
        return this.service.read();
    }

    public boolean setPrinter(int command) {
        byte[] arrayOfByte = null;
        switch (command) {
            case 0:
                arrayOfByte = new byte[]{27, 64};
                break;
            case 1:
                arrayOfByte = new byte[]{0};
                break;
            case 2:
                arrayOfByte = new byte[]{12};
                break;
            case 3:
                arrayOfByte = new byte[]{10};
                break;
            case 4:
                arrayOfByte = new byte[]{13};
                break;
            case 5:
                arrayOfByte = new byte[]{9};
                break;
            case 6:
                arrayOfByte = new byte[]{27, 50};
        }

        this.sendByteData(arrayOfByte);
        return true;
    }

    public boolean setPrinter(int command, int value) {
        byte[] arrayOfByte = new byte[3];
        switch (command) {
            case 0:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 74;
                break;
            case 1:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 100;
                break;
            case 2:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 33;
                break;
            case 3:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 85;
                break;
            case 4:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 86;
                break;
            case 5:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 87;
                break;
            case 6:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 45;
                break;
            case 7:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 43;
                break;
            case 8:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 105;
                break;
            case 9:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 99;
                break;
            case 10:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 51;
                break;
            case 11:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 32;
                break;
            case 12:
                arrayOfByte[0] = 28;
                arrayOfByte[1] = 80;
                break;
            case 13:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 97;
                if (value > 2 || value < 0) {
                    return false;
                }
            case 14:
            case 15:
            default:
                break;
            case 16:
                arrayOfByte[0] = 27;
                arrayOfByte[1] = 33;
                break;
            case 17:
                arrayOfByte[0] = 29;
                arrayOfByte[1] = 33;
        }

        arrayOfByte[2] = (byte) value;
        this.sendByteData(arrayOfByte);
        return true;
    }

    public void setCharacterMultiple(int x, int y) {
        byte[] arrayOfByte = new byte[]{29, 33, 0};
        if (x >= 0 && x <= 7 && y >= 0 && y <= 7) {
            arrayOfByte[2] = (byte) (x * 16 + y);
            this.sendByteData(arrayOfByte);
        }

    }

    public void setLeftMargin(int nL, int nH) {
        byte[] arrayOfByte = new byte[]{29, 76, (byte) nL, (byte) nH};
        this.sendByteData(arrayOfByte);
    }

    public int printBarCode(Barcode barcode) {
        return this.sendByteData(barcode.getBarcodeData());
    }

    public void setPrintModel(boolean isBold, boolean isDoubleHeight, boolean isDoubleWidth, boolean isUnderLine) {
        byte[] arrayOfByte = new byte[]{27, 33, 0};
        int a = 0;
        if (isBold) {
            a += 8;
        }

        if (isDoubleHeight) {
            a += 16;
        }

        if (isDoubleHeight) {
            a += 32;
        }

        if (isDoubleHeight) {
            a += 128;
        }

        arrayOfByte[2] = (byte) a;
        this.sendByteData(arrayOfByte);
    }

    public void cutPaper() {
        byte[] cutCommand = new byte[]{29, 86, 66, 0};
        this.sendByteData(cutCommand);
    }

    public void ringBuzzer(byte time) {
        byte[] buzzerCommand = new byte[]{29, 105, time};
        this.sendByteData(buzzerCommand);
    }

    public void openCashbox(boolean cashbox1, boolean cashbox2) {
        byte[] drawCommand;
        if (cashbox1) {
            drawCommand = new byte[]{27, 112, 0, 50, 50};
            this.sendByteData(drawCommand);
        }

        if (cashbox2) {
            drawCommand = new byte[]{27, 112, 1, 50, 50};
            this.sendByteData(drawCommand);
        }

    }

}
