package me.rubick.common.app.exception;

/**
 * Excel 导入异常
 */
public class ExcelImportException extends Exception {

    private int row;

    private ExcelImportException() {
        super();
    }

    public ExcelImportException(int row, String message) {
        super(message);
        this.row = row;
    }

    public int getRow() {
        return row;
    }
}
