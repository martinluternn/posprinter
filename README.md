
# react-native-pos-printer

react native module for printing receipt on pos printer.

For now it's only support for android

# Prerequisites
You must install the following library to your react native project

1. RNFetchBlob (optional) if you want to print with image

## Getting started

`$ npm install react-native-pos-printer --save`

### Mostly automatic installation

`$ react-native link react-native-pos-printer`

### Manual installation

#### iOS

Still not supported yet

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNPosPrinterPackage;` to the imports at the top of the file
  - Add `new RNPosPrinterPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-pos-printer'
  	project(':react-native-pos-printer').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-pos-printer/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-pos-printer')
  	```

## Usage
```javascript
import RNPosPrinter from 'react-native-pos-printer';
```

### Init Printer
For the first time need to initialize the pos printer, the return will be `Promise<void>`
```javascript
RNPosPrinter.init(true);
```
#### Option
```javascript
isDebug: boolean //default false
```



### Get All Printer Devices
To get all printer devices you just need to call this one, the return will be `Promise<Printer[]>`
```javascript
RNPosPrinter.getDevices()
```


### Scan Device
To scan all devices printer, the return will be `Promise<boolean>`
```javascript
RNPosPrinter.scanDevices(callback);
```
#### Option
```javascript
callback: Callback
```



### Stop Scan Device
To stop scanning all devices printer, the return will be `Promise<boolean>`
```javascript
RNPosPrinter.stopScanDevices();
```



### Connect Device
To connect to specific pos printer, the return will be `Promise<any>`
```javascript
RNPosPrinter.connectDevice(deviceId, timeout);
```
#### Option
```javascript
deviceId: string
timeout: number //default 30000 ms
```



### Connect And Print
To connect and automatically print on specific pos printer, the return will be `Promise<any>`
```javascript
RNPosPrinter.connectAndPrintReceipt(deviceId);
```
#### Option
```javascript
deviceId: string
```



### Print Example
To print on pos printer
```javascript
RNPosPrinter.printTestReceipt(storageUrl);
```
#### Option
```javascript
storageUrl: string //optional, if want to print image
```



### Misc
If you want to use directly the printer command you can access on 
```javascript
RNPosPrinter.printerModule
```