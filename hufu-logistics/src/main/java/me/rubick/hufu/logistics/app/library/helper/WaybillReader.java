package me.rubick.hufu.logistics.app.library.helper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import me.rubick.hufu.logistics.app.exception.BaseException;
import me.rubick.hufu.logistics.app.model.Waybill;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WaybillReader extends AbstractExcel {

    public List<Waybill> read(MultipartFile file) throws BaseException {
        Workbook workbook = getWorkbook(file);
        List<Waybill> orderList = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();

        boolean skip = true;

        while (iterator.hasNext()) {
            Row row = iterator.next();

            if (skip) {
                skip = false;
                continue;
            }

            Waybill order = this.parse(row);

            if (order.getTrackingNumber() != null && order.getTrackingNumber().trim().length() > 0) {
                orderList.add(order);
            }
        }

        return orderList;
    }

    private Waybill parse(Row row) {
        Waybill order = new Waybill();
        order.setTrackingNumber(getValue(row, 0));
        order.setLogistics_status(getValue(row, 1));
        return order;
    }


    private String getValue(Row row, int i) {
        Cell cell = row.getCell(i);
        Integer cellNo = i + 1;
        String result = "";
        if (cell == null) {
//            throw new BaseException("Excel 导入错误：第" + row.getRowNum() + "行中的第" + cellNo + "个单元格为空元素");
            return result;
        }


        switch (cell.getCellType()) {
//            case Cell.CELL_TYPE_BOOLEAN:
//                result = (String) cell.getBooleanCellValue();
//                break;
            case Cell.CELL_TYPE_NUMERIC:
                double d = cell.getNumericCellValue();
                result = doubleTrans2(d);
                break;
            case Cell.CELL_TYPE_STRING:
                result = cell.getStringCellValue().trim();
                break;
        }

        return result;
    }

    private String doubleTrans2(double num) {
        if (Math.round(num) - num == 0) {
            return String.valueOf((long) num);
        }
        return String.valueOf(num);
    }
}
