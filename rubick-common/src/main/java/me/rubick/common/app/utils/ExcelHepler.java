package me.rubick.common.app.utils;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.excel.ExcelConverter;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.CommonException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class ExcelHepler<T> {
    private Workbook getWorkbook(File file) throws CommonException {
        log.info(file.getName());
        String[] strings = file.getName().split("\\.");
        String excelFilePath = strings[strings.length - 1];
        Workbook workbook = null;

        try {
            InputStream inputStream = new FileInputStream(file);
            switch (excelFilePath) {
                case "xlsx":
                    workbook = new XSSFWorkbook(inputStream);
                    break;
                case "xls":
                    workbook = new HSSFWorkbook(inputStream);
                    break;
                default:
                    throw new CommonException("请上传正确的 Excel 文件（包括 .xlsx 和 .xls）");

            }
        } catch (IOException e) {
            throw new CommonException(e.getMessage());
        }

        return workbook;
    }

    public List<T> readToObject(File file, ExcelConverter<T> converter) throws CommonException, BusinessException {
        Workbook workbook = getWorkbook(file);

        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = sheet.iterator();

        boolean skip = true;

        List<T> items = new ArrayList<>();

        while (iterator.hasNext()) {
            Row row = iterator.next();

            if (skip) {
                skip = false;
                continue;
            }

            items.add(converter.read(row));
        }

        return items;
    }

    public static String getValue(Row row, int i, boolean allowNull) throws BusinessException {
        Cell cell = row.getCell(i);
        String cellValue = "";

        if (!(cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
            if (allowNull) {
                return "";
            } else {
                throw new BusinessException("错误发生在" + row.getRowNum() + "行，" + i +"个单元格， 该单元格为空。（若此行没有数据，请把整行删除）");
            }
        }

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                cellValue = cell.getStringCellValue();
                break;

            case Cell.CELL_TYPE_FORMULA:
                cellValue = cell.getCellFormula();
                break;

            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = cell.getDateCellValue().toString();
                } else {
                    cellValue = Double.toString(cell.getNumericCellValue());
                }
                break;

            case Cell.CELL_TYPE_BLANK:
                cellValue = "";
                break;

            case Cell.CELL_TYPE_BOOLEAN:
                cellValue = Boolean.toString(cell.getBooleanCellValue());
                break;

        }

        log.info("RowNO:{} cell={} value={}", row.getRowNum(), i, cellValue);
        return cellValue.trim();
    }

    private static String doubleTrans2(double num) {
        if (Math.round(num) - num == 0) {
            return String.valueOf((long) num);
        }
        return String.valueOf(num);
    }
}
