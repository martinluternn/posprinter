
# react-native-pos-printer

react native module for printing receipt on pos printer.

For now it's only support for android

# Prerequisites
You must install the following library to your react native project

1. moment
2. RNFetchBlob (optional) if you want to print with image

## Getting started

`$ npm install react-native-pos-printer --save`

### Mostly automatic installation

`$ react-native link react-native-pos-printer`

### Manual installation

<!-- #### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-pos-printer` and add `RNPosPrinter.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNPosPrinter.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)< -->

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

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNPosPrinter.sln` in `node_modules/react-native-pos-printer/windows/RNPosPrinter.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Pos.Printer.RNPosPrinter;` to the usings at the top of the file
  - Add `new RNPosPrinterPackage()` to the `List<IReactPackage>` returned by the `Packages` method

## Usage
```javascript
import RNPosPrinter from 'react-native-pos-printer';
```

### Init Printer
for the first time need to initialize the pos printer, the return will be `Promise<void>`
`RNPosPrinter.init(true);`
#### Option
`isDebug: boolean //default false`

### Get All Printer Devices
to get all printer devices you just need to call this one, the return will be `Promise<Printer[]>`
`RNPosPrinter.getDevices()`

### Scan Device
to scan all devices printer, the return will be `Promise<boolean>`
`RNPosPrinter.scanDevices(callback);`
#### Option
`callback: Callback`

### Stop Scan Device
to stop scanning all devices printer, the return will be `Promise<boolean>`
`RNPosPrinter.stopScanDevices();`

### Connect Device
to connect to specific pos printer, the return will be `Promise<any>`
`RNPosPrinter.connectDevice(deviceId, timeout);`
#### Option
`deviceId: string`
`timeout: number //default 30000 ms`

### Connect And Print
to connect and automatically print on specific pos printer, the return will be `Promise<any>`
`RNPosPrinter.connectAndPrintReceipt(deviceId);`
#### Option
`deviceId: string`

### Print Example
to print on pos printer
`RNPosPrinter.printTestReceipt(true);`
#### Option
`storageUrl: string //optional, if want to print image`

### Misc
if you want to use directly the printer command you can access on 
`RNPosPrinter.printerModule`