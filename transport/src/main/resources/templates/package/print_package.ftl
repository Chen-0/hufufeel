<!DOCTYPE html>
<html>
<head>
    <meta content="text/html;charset=UTF-8" http-equiv="Content-Type"/>
    <title>打印入库清单</title>
    <meta content="none" name="msapplication-config"/>
    <meta content="authenticity_token" name="csrf-param"/>
    <script src="/static/js/lodop.js"></script>
</head>
<body style="display: none; visibility: hidden;">

<#--<p>如下是几个div包裹的超文本内容，现在演示用这些内容组合起来打印输出<b>两个发货单</b>，点<a href="javascript:PreviewMytable();">打印预览</a>看一下。</p>-->

<div id="tt">
    <h1 style="text-align: center">入库清单</h1>
</div>
<div id="div1">
    <TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
        <TBODY>
        <TR>
            <TD width="50%">目标仓库：${ele.warehouseName}</TD>
            <TD width="50%">运单时间：${ele.createdAt?string}</TD>
        </TR>
        <TR>
            <TD width="50%">入库单号：${ele.sn}</TD>
            <TD width="50%">客户编号：${ele.user.name}(NO.${ele.user.hwcSn})</TD>
        </TR>
        <TR>
            <TD>参考号：${ele.referenceNumber!}</TD>
            <TD>预计总数：${ele.expectQuantity}</TD>
        </TR>
        </TBODY>
    </TABLE>
</div>

<div id="div2">

    <TABLE border=1 cellSpacing=0 cellPadding=1 width="100%" style="border-collapse:collapse" bordercolor="#333333">
        <thead>
        <TR>
            <TD width="10%">
                <DIV align=center><b>箱号</b></DIV>
            </TD>
            <TD width="25%">
                <DIV align=center><b>货品SKU</b></DIV>
            </TD>
            <TD width="10%">
                <DIV align=center><b>货品名称</b></DIV>
            </TD>
            <TD width="10%">
                <DIV align=center><b>预收数</b></DIV>
            </TD>
            <TD width="10%">
                <DIV align=center><b>实收数</b></DIV>
            </TD>
            <TD width="15%">
                <DIV align=center><b>重量</b></DIV>
            </TD>
            <TD width="20%">
                <DIV align=center><b>申报名称</b></DIV>
            </TD>
            <TD width="20%">
                <DIV align=center><b>申报金额</b></DIV>
            </TD>
        </TR>
        </thead>
        <TBODY>
        <#list ele.packageProducts as pp>
        <TR>
            <TD>${pp_index + 1}</TD>
            <TD>${pp.product.productSku}</TD>
            <TD>${pp.product.productName}</TD>
            <TD>${pp.expectQuantity}</TD>
            <TD></TD>
            <TD></TD>
            <TD>${pp.product.quotedName}</TD>
            <TD>${pp.product.quotedPrice}</TD>
        </TR>
        </#list>
    </TABLE>
</div>

<script>
    function PreviewMytable() {
        var LODOP = getLodop();
        LODOP.PRINT_INIT("虎芙货运管家 - 打印入库清单");

        LODOP.ADD_PRINT_HTM(30, "5%", "90%", 109, document.getElementById("tt").innerHTML);
        LODOP.ADD_PRINT_BARCODE(80, "%10", 270, 60, "128A", "${ele.sn}");
        LODOP.ADD_PRINT_HTM(160, "5%", "90%", 109, document.getElementById("div1").innerHTML);

        var strStyle = "<style> table,td,th {border-width: 1px;border-style: solid;border-collapse: collapse}</style>";
        LODOP.ADD_PRINT_TABLE(300, "5%", "90%", 1000, strStyle + document.getElementById("div2").innerHTML);
        LODOP.SET_PRINT_STYLEA(0, "Vorient", 3);

        LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);
        LODOP.SET_PRINT_STYLEA(0, "LinkedItem", 1);


        LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);

        LODOP.SET_PRINT_STYLEA(0, "Horient", 1);
        LODOP.ADD_PRINT_TEXT(3, 34, 200, 20, "虎芙货运管家 - www.hufufeel.com");
        LODOP.SET_PRINT_STYLEA(0, "ItemType", 1);
        LODOP.PREVIEW();
    }

    setTimeout(function () {
        PreviewMytable();
    }, 1000);
</script>


</body>
