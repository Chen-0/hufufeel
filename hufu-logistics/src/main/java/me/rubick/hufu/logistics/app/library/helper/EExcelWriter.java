package me.rubick.hufu.logistics.app.library.helper;

import me.rubick.hufu.logistics.app.model.CompanyOrder;

import java.util.List;

public class EExcelWriter extends me.rubick.hufu.logistics.app.library.helper.AbstractExcelWriter {
    @Override
    Object[][] getData(List<CompanyOrder> orders) {
        int rowNo = 2;

        Object[][] data = new Object[orders.size() + 2][];

        data[0] = new Object[30];
        data[0][0] = "S.No";
        data[0][1] = "customer_hawb";
        data[0][2] = "e Express no#";
        data[0][3] = "receiver_name";
        data[0][4] = "receiver_phone";
        data[0][5] = "receiver_address1";
        data[0][6] = "receiver_city";
        data[0][7] = "receiver_province";
        data[0][8] = "receiver_Zip";
        data[0][9] = "s_content1";
        data[0][10] = "Tax_code1";
        data[0][11] = "s_price1";
        data[0][12] = "s_pieces1";
        data[0][13] = "s_weight1";
        data[0][14] = "s_content2";
        data[0][15] = "Tax_code2";
        data[0][16] = "s_price2";
        data[0][17] = "s_pieces2";
        data[0][18] = "s_weight2";
        data[0][19] = "s_content3";
        data[0][20] = "Tax_code3";
        data[0][21] = "s_price3";
        data[0][22] = "s_pieces3";
        data[0][23] = "s_weight3";
        data[0][24] = "declare_currency";
        data[0][25] = "declare_value";
        data[0][26] = "insurance";

        data[0][27] = "weight";
        data[0][28] = "shipment_date";
        data[0][29] = "duty_paid";


        data[1] = new Object[30];
        data[1][0] = "序號";
        data[1][1] = "參考編號";
        data[1][2] = "e特快單號";
        data[1][3] = "收件人姓名";
        data[1][4] = "收件人電話(11位手机号)";
        data[1][5] = "收件人地址 1";
        data[1][6] = "收件人城市";
        data[1][7] = "收件人省份";
        data[1][8] = "郵編";
        data[1][9] = "貨物名稱 1";
        data[1][10] = "稅則號 1";
        data[1][11] = "單價 1";
        data[1][12] = "件數 1";
        data[1][13] = "單個重量 1";
        data[1][14] = "貨物名稱 2";
        data[1][15] = "稅則號 2";
        data[1][16] = "單價 2";
        data[1][17] = "件數 2";
        data[1][18] = "單個重量 2";
        data[1][19] = "貨物名稱 3";
        data[1][20] = "稅則號 3";
        data[1][21] = "單價 3";
        data[1][22] = "件數 3";
        data[1][23] = "單個重量 3";
        data[1][24] = "申報貨幣";
        data[1][25] = "申報價值";
        data[1][26] = "保額";

        data[1][27] = "總重";
        data[1][28] = "提單日期";
        data[1][29] = "代付税金(Yes=DDP,No=DDU)";

        for (CompanyOrder order : orders) {
            if (!order.getStatusId().equals(3)) {
                continue;
            }
            data[rowNo] = new Object[30];
            data[rowNo][0] = rowNo;
            data[rowNo][1] = order.getTrackingNumber();
            data[rowNo][3] = order.getContact();
            data[rowNo][4] = order.getPhone();
            data[rowNo][5] = order.getAddress();
            data[rowNo][8] = order.getZipCode();
            data[rowNo][9] = order.getGoodsName();
            data[rowNo][11] = order.getTotal();
            data[rowNo][12] = order.getQuantity();
            data[rowNo][13] = order.getOutWeight();
            data[rowNo][24] = "USD";
            data[rowNo][25] = order.getDeclared();
            data[rowNo][26] = order.getInsurance();
            data[rowNo][27] = order.getOutWeight();
            data[rowNo][28] = order.getOutTime();
            data[rowNo][29] = "Yes";

            rowNo = rowNo + 1;
        }

        return data;
    }
}
