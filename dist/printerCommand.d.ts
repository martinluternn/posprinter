export interface PrinterCommand {
    type: string;
    [key: string]: any;
}
/**
 * set printer for initial setup
 */
declare function setPrinter(command: number, value: number): {
    type: string;
    command: number;
    value: number;
};
/**
 * set font before you want to print text with custom font
 * @param width
 * @param height
 * @param bold
 * @param underline
 */
declare function setFont(width: number, height: number, bold: number, underline: number): {
    type: string;
    width: number;
    height: number;
    bold: number;
    underline: number;
};
/**
 * print text
 * @param text
 */
declare function printText(text: string): {
    type: string;
    text: string;
};
/**
 * print image (note: image will always download to storage first before printed)
 * @param url
 */
declare function printImageFromStorage(url: string): {
    type: string;
    url: string;
};
/**
 * to create pattern separator with length
 * @param pattern
 * @param length
 */
declare function generatePattern(pattern: string, length: number): string;
/**
 * print separator with length 30
 * @param pattern
 */
declare function printSeparator30(pattern: string): {
    type: string;
    text: string;
};
/**
 * print separator with length 46
 * @param pattern
 */
declare function printSeparator46(pattern: string): {
    type: string;
    text: string;
};
/**
 * to generated two side info text left and right
 * @param key
 * @param value
 * @param length
 */
declare function generateKeyValuePair(key: string, value: string, length: number): string;
/**
 * print two sided text with space length 30
 * @param key
 * @param value
 */
declare function printKeyValue30(key: string, value: string): {
    type: string;
    text: string;
};
/**
 * print two sided text with space length 46
 * @param key
 * @param value
 */
declare function printKeyValue46(key: string, value: string): {
    type: string;
    text: string;
};
/**
 * print text with new line
 * @param text
 */
declare function printLine(text: string): {
    type: string;
    text: string;
};
/**
 * set multiple char
 * @param x
 * @param y
 */
declare function setCharacterMultiple(x: number, y: number): {
    type: string;
    x: number;
    y: number;
};
/**
 * set left margin for printer receipt
 * @param x
 * @param y
 */
declare function setLeftMargin(x: number, y: number): {
    type: string;
    x: number;
    y: number;
};
/**
 * print barcode
 * @param barcodeType
 * @param param1
 * @param param2
 * @param param3
 * @param param4
 */
declare function printBarCode(barcodeType: number, param1: number, param2: number, param3: number, content: string): {
    type: string;
    barcodeType: number;
    param1: number;
    param2: number;
    param3: number;
    content: string;
};
declare const PrinterConstants: {
    Command: {
        INIT_PRINTER: number;
        WAKE_PRINTER: number;
        PRINT_AND_RETURN_STANDARD: number;
        PRINT_AND_NEWLINE: number;
        PRINT_AND_ENTER: number;
        MOVE_NEXT_TAB_POSITION: number;
        DEF_LINE_SPACING: number;
        PRINT_AND_WAKE_PAPER_BY_LNCH: number;
        PRINT_AND_WAKE_PAPER_BY_LINE: number;
        CLOCKWISE_ROTATE_90: number;
        ALIGN: number;
        ALIGN_LEFT: number;
        ALIGN_CENTER: number;
        ALIGN_RIGHT: number;
        LINE_HEIGHT: number;
        CHARACTER_RIGHT_MARGIN: number;
        FONT_MODE: number;
        FONT_SIZE: number;
    };
    BarcodeType: {
        UPC_A: number;
        UPC_E: number;
        JAN13: number;
        JAN8: number;
        CODE39: number;
        ITF: number;
        CODABAR: number;
        CODE93: number;
        CODE128: number;
        PDF417: number;
        DATAMATRIX: number;
        QRCODE: number;
    };
};
declare const printerCommand: {
    setPrinter: typeof setPrinter;
    setFont: typeof setFont;
    printText: typeof printText;
    printImageFromStorage: typeof printImageFromStorage;
    printLine: typeof printLine;
    printSeparator30: typeof printSeparator30;
    printSeparator46: typeof printSeparator46;
    printKeyValue30: typeof printKeyValue30;
    printKeyValue46: typeof printKeyValue46;
    setCharacterMultiple: typeof setCharacterMultiple;
    printBarCode: typeof printBarCode;
    setLeftMargin: typeof setLeftMargin;
};
declare const Printertools: {
    generateKeyValuePair: typeof generateKeyValuePair;
    generatePattern: typeof generatePattern;
};
export { PrinterConstants, printerCommand, Printertools, };
