package me.rubick.hufu.logistics.app.library.helper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import me.rubick.hufu.logistics.app.exception.BaseException;
import me.rubick.hufu.logistics.app.model.CompanyOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

abstract class AbstractExcelReader extends AbstractExcel {
    public List<CompanyOrder> read(MultipartFile file) throws BaseException {
        Workbook workbook = getWorkbook(file);
        List<CompanyOrder> orderList = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();

        boolean skip = true;

        while (iterator.hasNext()) {
            Row row = iterator.next();

            if (skip) {
                skip = false;
                continue;
            }

            CompanyOrder order = this.parse(row);

            check(order);

            if (order.getTrackingNumber() != null && order.getTrackingNumber().trim().length() > 0) {
                orderList.add(order);
            }
        }

        return orderList;
    }

    abstract CompanyOrder parse(Row row) throws BaseException;

    Boolean check(CompanyOrder order) throws BaseException {
        return true;
    }

    BigDecimal getBigDecimal(String value) {
        if (value == null) {
            return new BigDecimal(0);
        } else {
            value = value.trim();
            if (value.length() > 0) {
                return new BigDecimal(value);
            } else {
                return new BigDecimal(0);
            }
        }
    }

    Integer getInteger(String value) {
        if (value == null) {
            return 0;
        } else {
            try {
                value = value.trim();
                if (value.length() > 0) {
                    return Integer.valueOf(value);
                } else {
                    return 0;
                }
            } catch (Exception e) {
                return 0;
            }
        }
    }


    String getValue(Row row, int i) {
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
