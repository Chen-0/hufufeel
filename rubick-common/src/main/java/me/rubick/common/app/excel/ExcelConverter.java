package me.rubick.common.app.excel;

import me.rubick.common.app.exception.BusinessException;
import org.apache.poi.ss.usermodel.Row;

import java.util.List;

public interface ExcelConverter<T> {

    T read(Row row) throws BusinessException ;
}
