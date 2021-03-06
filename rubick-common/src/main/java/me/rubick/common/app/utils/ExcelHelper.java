package me.rubick.common.app.utils;

import lombok.extern.slf4j.Slf4j;
import me.rubick.common.app.excel.ExcelConverter;
import me.rubick.common.app.excel.ExcelRow;
import me.rubick.common.app.exception.BusinessException;
import me.rubick.common.app.exception.CommonException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class ExcelHelper<T> {

    public static List<ExcelRow> read(File file) throws CommonException, BusinessException {
        ExcelHelper<ExcelRow> helper = new ExcelHelper<>();

        List<ExcelRow> excelRows = helper.readToObject(file, new ExcelConverter<ExcelRow>() {
            @Override
            public ExcelRow read(Row row) throws BusinessException {
                ExcelRow excelRow = new ExcelRow();

                excelRow.setA(getValue(row, 0));
                excelRow.setB(getValue(row, 1));
                excelRow.setC(getValue(row, 2));
                excelRow.setD(getValue(row, 3));
                excelRow.setE(getValue(row, 4));
                excelRow.setF(getValue(row, 5));
                excelRow.setG(getValue(row, 6));
                excelRow.setH(getValue(row, 7));
                excelRow.setI(getValue(row, 8));
                excelRow.setJ(getValue(row, 9));
                excelRow.setK(getValue(row, 10));
                excelRow.setL(getValue(row, 11));
                excelRow.setM(getValue(row, 12));
                excelRow.setN(getValue(row, 13));
                excelRow.setO(getValue(row, 14));

                if (
                        StringUtils.hasText(excelRow.getA()) ||
                        StringUtils.hasText(excelRow.getB()) ||
                        StringUtils.hasText(excelRow.getC()) ||
                        StringUtils.hasText(excelRow.getD())) {
                    return excelRow;
                }

               return null;
            }
        });

        return excelRows;
    }

    public static Workbook getWorkbook(File file) throws CommonException {
        log.info(file.getName());
        String[] strings = file.getName().split("\\.");
        String excelFilePath = strings[strings.length - 1];
        Workbook workbook = null;

        try (InputStream inputStream = new FileInputStream(file)) {
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

            T item = converter.read(row);

            if (!ObjectUtils.isEmpty(item)) {
                items.add(item);
            }
        }

        return items;
    }

    public static BigDecimal getDecimal(Row row, int i) throws BusinessException {
        try {
            return new BigDecimal(getValue(row, i, false));
        } catch (NumberFormatException e) {
            int x = row.getRowNum() + 1;
            int y = i + 1;
            throw new BusinessException("错误发生在" + x + "行" + "，第" + y + "列，请输入数字！");
        }

    }

    public static String getValue(Row row, int i) throws BusinessException {
        return getValue(row, i, true);
    }

    public static String getValue(Row row, int i, boolean allowNull) throws BusinessException {
        Cell cell = row.getCell(i);
        String cellValue = "";

        if (!(cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK)) {
            if (allowNull) {
                return "";
            } else {
                int x = row.getRowNum() + 1;
                int y = i + 1;
                throw new BusinessException("错误发生在" + x + "行，" + y + "个单元格， 该单元格为空。（若此行没有数据，请把整行删除）");
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
                    //读取数字类型时，会读取很多位小数点，用以下解决方法
                    cellValue = NumberToTextConverter.toText(cell.getNumericCellValue());
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
