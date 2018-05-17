package me.rubick.hufu.logistics.app.library.helper;

import me.rubick.hufu.logistics.app.model.CompanyOrder;

import java.util.List;

public class GuangZhuoExcelWriter extends AbstractExcelWriter {

    @Override
    Object[][] getData(List<CompanyOrder> orders) {
        int rowNo = 1;

        Object[][] data = new Object[orders.size() + 1][];

        data[0] = new Object[35];
        data[0][0] = "NO";
        data[0][1] = "报关单号";
        data[0][2] = "进口总运";
        data[0][3] = "启动城市";
        data[0][4] = "袋口";
        data[0][5] = "进口快件运单";
        data[0][6] = "发件人";
        data[0][7] = "发件人地址";
        data[0][8] = "发件人电话";
        data[0][9] = "收件人";
        data[0][10] = "收件人电话";
        data[0][11] = "城市";
        data[0][12] = "邮编";
        data[0][13] = "收件人地址";
        data[0][14] = "内件名称";
        data[0][15] = "总数量";
        data[0][16] = "总价";
        data[0][17] = "包裹毛重";
        data[0][18] = "税号";
        data[0][19] = "商品名称";
        data[0][20] = "品牌";
        data[0][21] = "名称";
        data[0][22] = "规格型号";
        data[0][23] = "申报数量";
        data[0][24] = "单位";
        data[0][25] = "申报重量";
        data[0][26] = "保额";

        data[0][27] = "单价";
        data[0][28] = "币种";
        data[0][29] = "身份证件号码";
        data[0][30] = "运输工具";
        data[0][31] = "客户编号";
        data[0][32] = "购物小票号码";
        data[0][33] = "价格网址";
        data[0][34] = "发货人国别";

        for (CompanyOrder order : orders) {
            if (!order.getStatusId().equals(3)) {
                continue;
            }
            data[rowNo] = new Object[35];
            data[rowNo][0] = rowNo;
            data[rowNo][5] = order.getTrackingNumber();
            data[rowNo][6] = order.getSender();
            data[rowNo][8] = order.getSenderPhone();
            data[rowNo][9] = order.getContact();
            data[rowNo][10] = order.getPhone();
            data[rowNo][12] = order.getZipCode();
            data[rowNo][13] = order.getAddress();
            data[rowNo][14] = order.getGoodsName();
            data[rowNo][15] = order.getQuantity();
            data[rowNo][16] = order.getTotal();
            data[rowNo][17] = order.getOutWeight();
            data[rowNo][19] = order.getGoodsName();
            data[rowNo][23] = order.getQuantity();
            data[rowNo][25] = order.getOutWeight();
            data[rowNo][26] = order.getInsurance();
            data[rowNo][28] = "CNY";
            data[rowNo][29] = order.getIdentity();
            rowNo = rowNo + 1;
        }

        return data;
    }
}
