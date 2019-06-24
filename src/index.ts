import {
    DeviceEventEmitter,
    NativeModules,
    Platform,
} from 'react-native';

import { Printer } from './model';

const isAndroid: boolean = Platform.OS === 'android';

import RNFetchBlob from 'rn-fetch-blob';

import {
    PrinterCommand,
    printerCommand,
    PrinterConstants,
    Printertools,
} from './printerCommand';

type Callback = (devices: Printer[]) => void;

class PrinterModule {

    public printerModule: any;
    private callback: Callback | null | undefined;
    private deviceEventEmitter: any;
    private isDebug: boolean = false;

    constructor() {
        this.printerModule = NativeModules.RNPosPrinter;
        this.log('Printer module is null ' + !!this.printerModule);
    }

    // initiate pos printer
    public init(isDebug: boolean = false): Promise<void> {
        this.isDebug = isDebug;
        if (!isAndroid)
            return Promise.reject('IOS is not supported');
        return this.printerModule.init();
    }

    // get all pos printer devices
    public getDevices(): Promise<Printer[]> {
        if (!isAndroid)
            return Promise.reject('IOS is not supported');
        return this.printerModule.getDevices();
    }

    // scan all pos printer devices
    public scanDevices(callback: Callback): Promise<boolean> {
        if (!isAndroid)
            return Promise.reject('IOS is not supported');
        this.log('start scanning devices');
        this.callback = callback;
        this.printerModule.scanDevices();
        this.listenToNativeEvent(true);
        return Promise.resolve(true);
    }

    // stop scannning
    public stopScanDevices(): Promise<boolean> {
        if (!isAndroid)
            return Promise.reject('IOS is not supported');
        this.callback = null;
        this.printerModule.stopScanningDevices();
        if (this.deviceEventEmitter)
            this.deviceEventEmitter.remove();
        this.log('stop scanning devices');
        return Promise.resolve(true);
    }

    // connect to pos printer device
    public connectDevice(deviceID: string, timeout: number = 30000): Promise<any> {
        if (!isAndroid)
            return Promise.reject('IOS is not supported');
        if (this.deviceEventEmitter)
            this.deviceEventEmitter.remove();
        return this.printerModule.connectDevice(deviceID, timeout);
    }

    // connect and print to pos printer device
    public connectAndPrintReceipt(deviceId: string): Promise<void> {
        return this.connectDevice(deviceId, 3000)
            .then(() => {
                return this.initiatePrintReceipt();
            });
    }

    // listen all changed state from native events
    private listenToNativeEvent(start: boolean): void {
        if (start) {
            this.deviceEventEmitter = DeviceEventEmitter
                .addListener('available_bluetooth_devices', (devices: Printer[]) => {
                    this.log('available devices' + JSON.stringify(devices));
                    if (this.callback)
                        this.callback(devices);
                });
            return;
        }

        if (this.deviceEventEmitter)
            this.deviceEventEmitter.remove();
    }

    // log message error
    private log(message: string) {
        if (this.isDebug) {
            // tslint:disable-next-line:no-console
            console.log('POS Printer', message);
        }
    }

    // initiate printer with image before print
    private initiatePrintReceipt() {
        try {
            const dirs = RNFetchBlob.fs.dirs;
            return RNFetchBlob
                .config({
                    // response data will be saved to this path if it has access right.
                    path: dirs.SDCardDir + '/image.png',
                })
                .fetch('GET', 'download url')
                .then((res: any) => {
                    this.log('print with image, download image success');
                    return this.printTestReceipt(res.path());
                })
                .catch(e => {
                    // fallback if something went wrong with rn fetch blob
                    this.log('print with image, fallback print without image' + JSON.stringify(e));
                    return this.printTestReceipt();
                });
        } catch (e) {
            // fallback if something went wrong with rn fetch blob
        }
    }

    // printer all commands to printer
    private async printTestReceipt(storageUrl?: string) {
        const cmd: PrinterCommand[] = [
            printerCommand.setPrinter(PrinterConstants.Command.ALIGN, PrinterConstants.Command.ALIGN_CENTER),
            printerCommand.setFont(1, 0, 2, 0),
        ];

        cmd.push(printerCommand.printLine('RECEIPT TITLE'));
        cmd.push(printerCommand.printSeparator30('-----'));
        cmd.push(printerCommand.printKeyValue30(Printertools.generateKeyValuePair('', 'Price', 0), '50$'));
        cmd.push(printerCommand.printLine(''));
        if (storageUrl)
            cmd.push(printerCommand.printImageFromStorage(storageUrl));
        cmd.push(printerCommand.printLine(''));
        cmd.push(printerCommand.setFont(-2, -2, 0, 0));
        cmd.push(printerCommand.printText('end of receipt'));
        cmd.push(printerCommand.printLine(''));

        try {
            await this.printerModule.addCommands(cmd);
            if (this.deviceEventEmitter)
                this.deviceEventEmitter.remove();
            return;
        } catch (e) { /** get rid of the linter error */ }
    }
}

const printer: PrinterModule = new PrinterModule();
export default printer;
