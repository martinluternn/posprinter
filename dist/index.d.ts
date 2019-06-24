import { Printer } from './model';
declare type Callback = (devices: Printer[]) => void;
declare class PrinterModule {
    printerModule: any;
    private callback;
    private deviceEventEmitter;
    private isDebug;
    constructor();
    init(isDebug?: boolean): Promise<void>;
    getDevices(): Promise<Printer[]>;
    scanDevices(callback: Callback): Promise<boolean>;
    stopScanDevices(): Promise<boolean>;
    connectDevice(deviceID: string, timeout?: number): Promise<any>;
    connectAndPrintReceipt(deviceId: string): Promise<void>;
    private listenToNativeEvent;
    private log;
    private initiatePrintReceipt;
    private printTestReceipt;
}
declare const printer: PrinterModule;
export default printer;
