package me.rubick.hufu.logistics.app.library.helper;


import me.rubick.hufu.logistics.app.library.Common;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import me.rubick.hufu.logistics.app.exception.BaseException;
import me.rubick.hufu.logistics.app.model.CompanyOrder;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


abstract class AbstractExcelWriter extends AbstractExcel {

    public void write(List<CompanyOrder> companyOrders, OutputStream outputStream) throws BaseException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        Object[][] data = getData(companyOrders);

        int rowCount = 0;
        for (Object[] aBook : data) {
            if (aBook == null) continue;

            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            for (Object field : aBook) {

                Cell cell = row.createCell(columnCount++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof BigDecimal) {
                    cell.setCellValue(field.toString());
                } else if (field instanceof Integer) {
                    cell.setCellValue(field.toString());
                } else if (field instanceof Date) {
                    cell.setCellValue(Common.dateConvertString((Date) field));
                }
            }
        }

        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new BaseException(e.getMessage());
        }
    }

    abstract Object[][] getData(List<CompanyOrder> orders);
}
