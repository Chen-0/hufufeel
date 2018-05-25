package me.rubick.hufu.logistics.app.library.helper;

import me.rubick.hufu.logistics.app.exception.BaseException;
import me.rubick.hufu.logistics.app.library.Common;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

abstract class AbstractExcel {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    Workbook getWorkbook(MultipartFile file) throws BaseException {
        if (file.isEmpty()) {
            throw new BaseException("请上传文件");
        }
        String excelFilePath = Common.getSuffix(file.getOriginalFilename());
        Workbook workbook = null;

        try {
            switch (excelFilePath) {
                case "xlsx":
                    workbook = new XSSFWorkbook(file.getInputStream());
                    break;
                case "xls":
                    workbook = new HSSFWorkbook(file.getInputStream());
                    break;
                default:
                    throw new BaseException("请上传正确的 Excel 文件（包括 .xlsx 和 .xls）");

            }
        } catch (IOException e) {
            throw new BaseException(e.getMessage());
        }

        return workbook;
    }
}
