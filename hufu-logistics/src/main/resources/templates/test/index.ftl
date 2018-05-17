<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${title!} - 虎芙货运管家</title>
<#--<link rel="shortcut icon" href="/favicon.ico"/>-->
<#--<link rel="bookmark" href="/favicon.ico" type="image/x-icon"/>-->
    <link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/metisMenu/2.7.0/metisMenu.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/startbootstrap-sb-admin-2/3.3.7+1/css/sb-admin-2.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker3.min.css">
    <link rel="stylesheet" href="/static/css/style.css">
    <link rel="stylesheet" type="text/css" href="//cdn.datatables.net/1.10.15/css/jquery.dataTables.css">
    <link rel="stylesheet"
          href="https://cdn.bootcss.com/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.min.css">
</head>
<body>

<table class="bordered">
    <thead>
    <tr>
    <#switch status>
        <#case 1>
            <th class="text-center">
                <input type="checkbox" id="all">
            </th>
            <th>批次</th>
            <th>大客户名称</th>
            <th>运单号</th>
            <th>收件人</th>
            <th>收件人电话</th>
            <th>收件人地址</th>
            <th>邮编</th>
            <th>商品信息</th>
            <th>商品数量</th>
            <th>入库重量</th>
            <th>申报价格</th>
            <th>创建时间</th>
            <th>操作</th>
            <#break >
        <#case 2>
            <th class="text-center">
                <input type="checkbox" id="all">
            </th>
            <th>批次</th>
            <th>大客户名称</th>
            <th>运单号</th>
            <th>收件人</th>
            <th>收件人电话</th>
            <th>收件人地址</th>
            <th>邮编</th>
            <th>商品信息</th>
            <th>商品数量</th>
            <th>入库重量</th>
            <th>入库时间</th>
            <th>申报价格</th>
            <th>操作</th>
            <#break >
        <#case 3>
            <th class="text-center">
                <input type="checkbox" id="all">
            </th>
            <th>批次</th>
            <th>大客户名称</th>
            <th>运单号</th>
            <th>收件人</th>
            <th>收件人电话</th>
            <th>收件人地址</th>
            <th>邮编</th>
            <th>商品信息</th>
            <th>商品数量</th>
            <th>出库重量</th>
            <th>出库时间</th>
            <th>渠道</th>
            <th>运费</th>
            <th>物流状态</th>
            <th>物流信息</th>
            <th>备注</th>
            <#break >
        <#case 99>
            <th class="text-center">
                <input type="checkbox" id="all">
            </th>
            <th>批次</th>
            <th>大客户名称</th>
            <th>运单号</th>
            <th>收件人</th>
            <th>收件人电话</th>
            <th>收件人地址</th>
            <th>邮编</th>
            <th>商品信息</th>
            <th>备注</th>
            <th>错误信息</th>
            <th>操作</th>
            <#break >
        <#case 100>
            <th class="text-center">
                <input type="checkbox" id="all">
            </th>
            <th>批次</th>
            <th>大客户名称</th>
            <th>运单号</th>
            <th>收件人</th>
            <th>收件人电话</th>
            <th>收件人地址</th>
            <th>邮编</th>
            <th>商品信息</th>
            <th>备注</th>
            <th>错误信息</th>
            <th>操作</th>
            <#break >
    </#switch>
    </tr>
    </thead>


    <tbody>
    <#list orders.getContent() as order>
    <tr>
        <#switch status>
            <#case 1>
                <td class="text-center">
                    <input type="checkbox" name="tracking_number[]"
                           value="${order.trackingNumber}"/>
                </td>
                <td>${order.batch!}</td>
                <td>${order.company.name!}</td>
                <td><a href="/order/${order.id}/update">${order.trackingNumber!}</a></td>
                <td>${order.contact!}</td>
                <td>${order.phone!}</td>
                <td>${order.address!}</td>
                <td>${order.zipCode!}</td>
                <td>${order.goodsName!}</td>
                <td>${order.quantity!}</td>
                <td>${order.inWeight!}</td>
                <td>${order.declared!}</td>
                <td><#if order.createdAt?exists>${order.createdAt?datetime} </#if></td>
                <td><a href="/order/prepare/${order.id}">入库</a></td>
                <#break >
            <#case 2>
                <td class="text-center">
                    <input type="checkbox" name="tracking_number[]"
                           value="${order.trackingNumber}"/>
                </td>
                <td>${order.batch!}</td>
                <td>${order.company.name!}</td>
                <td><a href="/order/${order.id}/update">${order.trackingNumber!}</a></td>
                <td>${order.contact!}</td>
                <td>${order.phone!}</td>
                <td>${order.address!}</td>
                <td>${order.zipCode!}</td>
                <td>${order.goodsName!}</td>
                <td>${order.quantity!}</td>
                <td>${order.inWeight!}</td>
                <td><#if order.inTime?exists>${order.inTime?datetime} </#if></td>
                <td>${order.declared!}</td>
                <td>
                    <a href="/order/send/${order.id}">发货</a>
                    <a href="/order/reject/${order.id}">拒绝</a>
                </td>
                <#break>
            <#case 3>
                <td class="text-center">
                    <input type="checkbox" name="tracking_number[]"
                           value="${order.trackingNumber}"/>
                </td>
                <td>${order.batch!}</td>
                <td>${order.company.name!}</td>
                <td><a href="/order/${order.id}/update">${order.trackingNumber!}</a></td>
                <td>${order.contact!}</td>
                <td>${order.phone!}</td>
                <td>${order.address!}</td>
                <td>${order.zipCode!}</td>
                <td>${order.goodsName!}</td>
                <td>${order.quantity!}</td>
                <td>${order.outWeight!}</td>
                <td><#if order.outTime?exists>${order.outTime?datetime} </#if></td>
                <td>${order.companyExpress.name!}</td>
                <td>${order.total!}</td>
                <td>${order.lgStatus!}</td>
                <td>${order.lgInfo!}</td>
                <td>${order.comment!}</td>
                <#break >
            <#case 99>
                <td class="text-center">
                    <input type="checkbox" name="tracking_number[]"
                           value="${order.trackingNumber}"/>
                </td>
                <td>${order.batch!}</td>
                <td>${order.company.name!}</td>
                <td><a href="/order/${order.id}/update">${order.trackingNumber!}</a></td>
                <td>${order.contact!}</td>
                <td>${order.phone!}</td>
                <td>${order.address!}</td>
                <td>${order.zipCode!}</td>
                <td>${order.goodsName!}</td>
                <td>${order.comment!}</td>
                <td>${order.errorMsg!}</td>
                <td><a href="/order/${order.id}/recover">恢复</a></td>
                <#break >
            <#case 100>
                <td class="text-center">
                    <input type="checkbox" name="tracking_number[]"
                           value="${order.trackingNumber}"/>
                </td>
                <td>${order.batch!}</td>
                <td>${order.company.name!}</td>
                <td><a href="/order/${order.id}/update">${order.trackingNumber!}</a></td>
                <td>${order.contact!}</td>
                <td>${order.phone!}</td>
                <td>${order.address!}</td>
                <td>${order.zipCode!}</td>
                <td>${order.goodsName!}</td>
                <td>${order.comment!}</td>
                <td>${order.errorMsg!}</td>
                <td><a href="/order/${order.id}/check">查看</a></td>
                <#break >
        </#switch>
    </tr>
    </#list>
    </tbody>
</table>
</body>
<script src="//cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="https://cdn.bootcss.com/moment.js/2.18.1/moment.min.js"></script>
<script src="//cdn.bootcss.com/metisMenu/2.7.0/metisMenu.min.js"></script>
<script src="//cdn.bootcss.com/startbootstrap-sb-admin-2/3.3.7+1/js/sb-admin-2.min.js"></script>
<script src="//cdn.bootcss.com/bootstrap-datepicker/1.6.4/js/bootstrap-datepicker.min.js"></script>
<script type="text/javascript" charset="utf8" src="//cdn.datatables.net/1.10.15/js/jquery.dataTables.js"></script>
<script src="//cdn.bootcss.com/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
<script src="/static/js/script.js"></script>
<script>
    $(document).ready(function () {
        $.ajaxSetup({
            headers: {
                'X-CSRF-TOKEN': '${_csrf.token}'
            },
            dataType: 'json',
            type: 'post'
        });
    });
</script>
</html>
