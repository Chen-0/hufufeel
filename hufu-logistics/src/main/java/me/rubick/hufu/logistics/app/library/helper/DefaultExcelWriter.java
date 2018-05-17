package me.rubick.hufu.logistics.app.library.helper;

import me.rubick.hufu.logistics.app.model.CompanyOrder;

import java.util.List;

public class DefaultExcelWriter extends AbstractExcelWriter {
    @Override
    Object[][] getData(List<CompanyOrder> orders) {
        int rowNo = 1;

        Object[][] data = new Object[orders.size() + 1][];

        data[0] = new Object[20];
        data[0][0] = "客户自编单号";
        data[0][1] = "发件人姓名";
        data[0][2] = "发件人电话";
        data[0][3] = "实收件人姓名";
        data[0][4] = "实收件人身份证号";
        data[0][5] = "实收件人手机";
        data[0][6] = "实具体地址";
        data[0][7] = "邮编";
        data[0][8] = "虚收件人姓名";
        data[0][9] = "虚收件人身份证号";
        data[0][10] = "虚收件人手机";
        data[0][11] = "商品名称";
        data[0][12] = "商品数量";
        data[0][13] = "商品重量(磅)";
        data[0][14] = "商品申报总价USD";
        data[0][15] = "保额";
        data[0][16] = "备注";
        data[0][17] = "出库重";
        data[0][18] = "物流状态";
        data[0][19] = "物流信息";

        for (CompanyOrder order : orders) {
            if (!order.getStatusId().equals(3)) {
                continue;
            }
            data[rowNo] = new Object[20];
            data[rowNo][0] = order.getTrackingNumber();
            data[rowNo][1] = order.getSender();
            data[rowNo][2] = order.getSenderPhone();
            data[rowNo][3] = order.getContact();
            data[rowNo][4] = order.getIdentity();
            data[rowNo][5] = order.getPhone();
            data[rowNo][6] = order.getAddress();
            data[rowNo][7] = order.getZipCode();
            data[rowNo][8] = "";
            data[rowNo][9] = "";
            data[rowNo][10] = "";
            data[rowNo][11] = order.getGoodsName();
            data[rowNo][12] = order.getQuantity();
            data[rowNo][13] = order.getOutWeight();
            data[rowNo][14] = order.getDeclared();
            data[rowNo][15] = order.getInsurance();
            data[rowNo][16] = order.getComment();
            data[rowNo][17] = order.getOutWeight();
            data[rowNo][18] = order.getLgInfo();
            data[rowNo][19] = order.getLgStatus();
            rowNo = rowNo + 1;
        }

        return data;
    }
}
