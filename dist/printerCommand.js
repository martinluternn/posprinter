"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
/**
 * set printer for initial setup
 */
function setPrinter(command, value) {
    return {
        type: 'setPrinter',
        command: command,
        value: value,
    };
}
/**
 * set font before you want to print text with custom font
 * @param width
 * @param height
 * @param bold
 * @param underline
 */
function setFont(width, height, bold, underline) {
    return {
        type: 'setFont',
        width: width,
        height: height,
        bold: bold,
        underline: underline,
    };
}
/**
 * print text
 * @param text
 */
function printText(text) {
    return {
        type: 'printText',
        text: text,
    };
}
/**
 * print image (note: image will always download to storage first before printed)
 * @param url
 */
function printImageFromStorage(url) {
    return {
        type: 'printImageFromStorage',
        url: url,
    };
}
/**
 * to create pattern separator with length
 * @param pattern
 * @param length
 */
function generatePattern(pattern, length) {
    var totalPattern = '';
    while (totalPattern.length < length) {
        totalPattern += pattern;
    }
    totalPattern = totalPattern.substr(0, length);
    return totalPattern;
}
/**
 * print separator with length 30
 * @param pattern
 */
function printSeparator30(pattern) {
    return printLine(generatePattern(pattern, 30));
}
/**
 * print separator with length 46
 * @param pattern
 */
function printSeparator46(pattern) {
    return printLine(generatePattern(pattern, 46));
}
/**
 * to generated two side info text left and right
 * @param key
 * @param value
 * @param length
 */
function generateKeyValuePair(key, value, length) {
    var spaceLength = length - key.length - value.length;
    if (spaceLength < 1) {
        var diff = 1 - spaceLength;
        key = key.substr(0, key.length - diff);
        spaceLength = 1;
    }
    var space = generatePattern('     ', spaceLength);
    return key + space + value;
}
/**
 * print two sided text with space length 30
 * @param key
 * @param value
 */
function printKeyValue30(key, value) {
    return printLine(generateKeyValuePair(key, value, 30));
}
/**
 * print two sided text with space length 46
 * @param key
 * @param value
 */
function printKeyValue46(key, value) {
    return printLine(generateKeyValuePair(key, value, 46));
}
/**
 * print text with new line
 * @param text
 */
function printLine(text) {
    return printText(text + '\n');
}
/**
 * set multiple char
 * @param x
 * @param y
 */
function setCharacterMultiple(x, y) {
    return {
        type: 'setCharacterMultiple',
        x: x,
        y: y,
    };
}
/**
 * set left margin for printer receipt
 * @param x
 * @param y
 */
function setLeftMargin(x, y) {
    return {
        type: 'setLeftMargin',
        x: x,
        y: y,
    };
}
/**
 * print barcode
 * @param barcodeType
 * @param param1
 * @param param2
 * @param param3
 * @param param4
 */
function printBarCode(barcodeType, param1, param2, param3, content) {
    return {
        type: 'printBarCode',
        barcodeType: barcodeType,
        param1: param1,
        param2: param2,
        param3: param3,
        content: content,
    };
}
var PrinterConstants = {
    Command: {
        INIT_PRINTER: 0,
        WAKE_PRINTER: 1,
        PRINT_AND_RETURN_STANDARD: 2,
        PRINT_AND_NEWLINE: 3,
        PRINT_AND_ENTER: 4,
        MOVE_NEXT_TAB_POSITION: 5,
        DEF_LINE_SPACING: 6,
        PRINT_AND_WAKE_PAPER_BY_LNCH: 0,
        PRINT_AND_WAKE_PAPER_BY_LINE: 1,
        CLOCKWISE_ROTATE_90: 4,
        ALIGN: 13,
        ALIGN_LEFT: 0,
        ALIGN_CENTER: 1,
        ALIGN_RIGHT: 2,
        LINE_HEIGHT: 10,
        CHARACTER_RIGHT_MARGIN: 11,
        FONT_MODE: 16,
        FONT_SIZE: 17,
    },
    BarcodeType: {
        UPC_A: 0,
        UPC_E: 1,
        JAN13: 2,
        JAN8: 3,
        CODE39: 4,
        ITF: 5,
        CODABAR: 6,
        CODE93: 72,
        CODE128: 73,
        PDF417: 100,
        DATAMATRIX: 101,
        QRCODE: 102,
    },
};
exports.PrinterConstants = PrinterConstants;
var printerCommand = {
    setPrinter: setPrinter,
    setFont: setFont,
    printText: printText,
    printImageFromStorage: printImageFromStorage,
    printLine: printLine,
    printSeparator30: printSeparator30,
    printSeparator46: printSeparator46,
    printKeyValue30: printKeyValue30,
    printKeyValue46: printKeyValue46,
    setCharacterMultiple: setCharacterMultiple,
    printBarCode: printBarCode,
    setLeftMargin: setLeftMargin,
};
exports.printerCommand = printerCommand;
var Printertools = {
    generateKeyValuePair: generateKeyValuePair,
    generatePattern: generatePattern,
};
exports.Printertools = Printertools;
//# sourceMappingURL=printerCommand.js.map