export interface PrinterCommand {
    type: string;
    [key: string]: any;
}

/**
 * set printer for initial setup
 */
function setPrinter(command: number, value: number) {
    return {
        type: 'setPrinter',
        command,
        value,
    };
}

/**
 * set font before you want to print text with custom font
 * @param width
 * @param height
 * @param bold
 * @param underline
 */
function setFont(width: number, height: number, bold: number, underline: number) {
    return {
        type: 'setFont',
        width,
        height,
        bold,
        underline,
    };
}

/**
 * print text
 * @param text
 */
function printText(text: string) {
    return {
        type: 'printText',
        text,
    };
}

/**
 * print image (note: image will always download to storage first before printed)
 * @param url
 */
function printImageFromStorage(url: string) {
    return {
        type: 'printImageFromStorage',
        url,
    };
}

/**
 * to create pattern separator with length
 * @param pattern
 * @param length
 */
function generatePattern(pattern: string, length: number) {
    let totalPattern = '';
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
function printSeparator30(pattern: string) {
    return printLine(generatePattern(pattern, 30));
}

/**
 * print separator with length 46
 * @param pattern
 */
function printSeparator46(pattern: string) {
    return printLine(generatePattern(pattern, 46));
}

/**
 * to generated two side info text left and right
 * @param key
 * @param value
 * @param length
 */
function generateKeyValuePair(key: string, value: string, length: number) {
    let spaceLength = length - key.length - value.length;
    if (spaceLength < 1) {
        const diff = 1 - spaceLength;
        key = key.substr(0, key.length - diff);
        spaceLength = 1;
    }

    const space = generatePattern('     ', spaceLength);

    return key + space + value;
}

/**
 * print two sided text with space length 30
 * @param key
 * @param value
 */
function printKeyValue30(key: string, value: string) {
    return printLine(generateKeyValuePair(key, value, 30));
}

/**
 * print two sided text with space length 46
 * @param key
 * @param value
 */
function printKeyValue46(key: string, value: string) {
    return printLine(generateKeyValuePair(key, value, 46));
}

/**
 * print text with new line
 * @param text
 */
function printLine(text: string) {
    return printText(text + '\n');
}

/**
 * set multiple char
 * @param x
 * @param y
 */
function setCharacterMultiple(x: number, y: number) {
    return {
        type: 'setCharacterMultiple',
        x,
        y,
    };
}

/**
 * set left margin for printer receipt
 * @param x
 * @param y
 */
function setLeftMargin(x: number, y: number) {
    return {
        type: 'setLeftMargin',
        x,
        y,
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
function printBarCode(barcodeType: number, param1: number, param2: number, param3: number, content: string) {
    return {
        type: 'printBarCode',
        barcodeType,
        param1,
        param2,
        param3,
        content,
    };
}

const PrinterConstants = {
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

const printerCommand = {
    setPrinter,
    setFont,
    printText,
    printImageFromStorage,
    printLine,
    printSeparator30,
    printSeparator46,
    printKeyValue30,
    printKeyValue46,
    setCharacterMultiple,
    printBarCode,
    setLeftMargin,
};

const Printertools = {
    generateKeyValuePair,
    generatePattern,
};

export {
    PrinterConstants,
    printerCommand,
    Printertools,
};
