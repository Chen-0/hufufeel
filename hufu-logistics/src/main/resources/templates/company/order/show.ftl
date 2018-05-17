<!DOCTYPE html>
<html lang="en">
<#assign title="运单详情"/>
<#include "/_layout/head.ftl" />

<style>
    td {
        width: calc(100% / 6);
    }
</style>

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">运单详情</h1>
        </div>
    </div>

    <div class="row" style="padding-top: 50px;;">
        <#--<div class="col-lg-offset-2 col-lg-8">-->
        <div style="width: 80%; margin: 0 auto;">

            <div class="panel panel-primary">
                <div class="panel-heading">
                    运单：${order.trackingNumber}
                </div>
                <div class="panel-body">
                    <table class="table table-striped table-bordered table-hover">
                        <tr>
                            <td width="10%">批次：</td>
                            <td width="20%">${order.batch!}</td>

                            <td width="10%">运单号：</td>
                            <td width="20%">${order.trackingNumber}</td>

                            <td width="10%">所属：</td>
                            <td width="20%">${order.company.name!}</td>
                        </tr>


                        <tr>
                            <td>收件人：</td>
                            <td>${order.contact!}</td>

                            <td>收件人电话：</td>
                            <td>${order.phone!}</td>

                            <td>邮编：</td>
                            <td>${order.zipCode!}</td>
                        </tr>

                        <tr>
                            <td>收件人地址：</td>
                            <td colspan="4">${order.address!}</td>
                            <td>发件人电话：${order.senderPhone!}</td>
                        </tr>

                        <tr>
                            <td>商品信息：</td>
                            <td colspan="5">${order.goodsName!}</td>
                        </tr>
                        <tr>
                            <td>商品数量：</td>
                            <td>${order.quantity!}</td>

                            <td>入库重量：</td>
                            <td>${order.inWeight!}</td>

                            <td>出库重量：</td>
                            <td>${order.outWeight!}</td>
                        </tr>

                        <tr>
                            <td>申报价格：</td>
                            <td>${order.declared!}</td>

                            <td>保额：</td>
                            <td>${order.insurance!}</td>
                        </tr>

                        <tr>
                            <td>运单状态：</td>
                            <td>
                            <#switch order.statusId>
                                <#case 1>
                                    待入库
                                    <#break >
                                <#case 2>
                                    待出库
                                    <#break >
                                <#case 3>
                                    已发货
                                    <#break >
                                <#case 99>
                                    含错误
                                    <#break >
                                <#case 100>
                                    待审核
                                    <#break >
                            </#switch>
                            </td>

                            <td>运输渠道：</td>
                            <td>
                            <#if order.companyExpress??>
                                ${order.companyExpress.name}
                            </#if>
                            </td>


                            <td>费用：</td>
                            <td>${order.total!}</td>
                        </tr>

                        <tr>
                            <td>创建时间：</td>
                            <td>
                            <#if order.createdAt?? >
                            ${order.createdAt?string}
                            </#if>
                            </td>


                            <td>入库时间：</td>
                            <td>
                            <#if order.inTime??>
                            ${order.inTime?string}
                            </#if>
                            </td>


                            <td>出库时间：</td>
                            <td><#if order.outTime??>
                            ${order.outTime?string}
                            </#if></td>
                        </tr>

                        <tr>
                            <td>备注：</td>
                            <td colspan="5">${order.comment!}</td>
                        </tr>

                        <tr>
                            <td>物流状态：</td>
                            <td>${order.lgStatus!}</td>

                            <td>
                                物流信息：
                            </td>
                            <td colspan="3">
                            <#if order.lgInfo??>
                                <pre>${order.lgInfo}</pre>
                            </#if>
                            </td>
                        </tr>

                    </table>
                </div>
            </div>


        </div>
    </div>
</div>


<#include "/_layout/script.ftl"/>
</body>
</html>
