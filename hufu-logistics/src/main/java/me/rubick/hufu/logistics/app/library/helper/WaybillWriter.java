package me.rubick.hufu.logistics.app.library.helper;

import me.rubick.hufu.logistics.app.library.Common;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import me.rubick.hufu.logistics.app.exception.BaseException;
import me.rubick.hufu.logistics.app.model.Waybill;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class WaybillWriter {
    public void write(List<Waybill> waybills, OutputStream outputStream) throws BaseException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();

        Object[][] data = getData(waybills);

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

    private Object[][] getData(List<Waybill> waybills) {
        int rowNo = 1;
        Object[][] data = new Object[waybills.size() + 1][];

        data[0] = new Object[2];
        data[0][0] = "快递单号";
        data[0][1] = "物流信息";

        for (Waybill order : waybills) {
            if (order.getArrive() != null && order.getArrive().equals(5)) {
                data[rowNo] = new Object[2];
                data[rowNo][0] = order.getTrackingNumber();
                data[rowNo][1] = order.getLogistics_status();

                rowNo = rowNo + 1;
            }
        }

        return data;
    }
}
