package me.rubick.hufu.logistics.app.library.helper;

import org.apache.poi.ss.usermodel.Row;
import me.rubick.hufu.logistics.app.model.CompanyOrder;

public class LgExcelReader extends AbstractExcelReader {
    @Override
    CompanyOrder parse(Row row) {
        CompanyOrder order = new CompanyOrder();
        order.setTrackingNumber(getValue(row, 0));
        order.setLgInfo(getValue(row, 1));
        return order;
    }
}
