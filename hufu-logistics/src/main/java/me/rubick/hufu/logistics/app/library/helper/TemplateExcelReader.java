package me.rubick.hufu.logistics.app.library.helper;

import org.apache.poi.ss.usermodel.Row;
import me.rubick.hufu.logistics.app.exception.BaseException;
import me.rubick.hufu.logistics.app.model.CompanyOrder;

public class TemplateExcelReader extends AbstractExcelReader {
    @Override
    CompanyOrder parse(Row row) throws BaseException {
        CompanyOrder order = new CompanyOrder();
        order.setTrackingNumber(getValue(row, 3));
        order.setSender(getValue(row, 4));
        order.setSenderPhone(getValue(row, 5));
        order.setContact(getValue(row, 7));
        order.setIdentity(getValue(row, 8));
        order.setPhone(getValue(row, 9));
        order.setAddress(getValue(row, 10));
        order.setZipCode(getValue(row, 11));
        order.setGoodsName(getValue(row, 15));
        order.setStatusId(1);
        order.setQuantity(getBigDecimal(getValue(row, 16)));
        order.setInWeight(getBigDecimal(getValue(row, 17)));
        order.setDeclared(getBigDecimal(getValue(row, 18)));
        order.setInsurance(getInteger(getValue(row, 19)));
        order.setComment(getValue(row, 20));
        return order;
    }

    private boolean isPhone(String phone) {
        if (phone.length() != 11) {
            return false;
        }

        for (int i = 0; i < phone.length(); i++) {
            if (!Character.isDigit(phone.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    Boolean check(CompanyOrder order) throws BaseException {
        String phone = order.getPhone();
        if (!isPhone(phone)) {
            throw new BaseException("运单号：" + order.getTrackingNumber() + "中的联系人电话有误（长度不等于11位或不为数字）");
        }
        return true;
    }
}
