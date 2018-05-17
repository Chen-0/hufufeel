package me.rubick.hufu.logistics.app.library.helper;

import me.rubick.hufu.logistics.app.model.CompanyOrder;

import java.util.List;

public class LgExcelWriter extends AbstractExcelWriter {
    @Override
    Object[][] getData(List<CompanyOrder> orders) {
        int rowNo = 1;
        Object[][] data = new Object[orders.size() + 1][];

        data[0] = new Object[2];
        data[0][0] = "快递单号";
        data[0][1] = "物流信息";

        for (CompanyOrder order : orders) {
            if (order.getStatusId() != null && order.getStatusId().equals(3)) {
                data[rowNo] = new Object[2];
                data[rowNo][0] = order.getTrackingNumber();
                data[rowNo][1] = order.getLgInfo();

                rowNo = rowNo + 1;
            }
        }

        return data;
    }
}
