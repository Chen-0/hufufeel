<!DOCTYPE html>
<html lang="en">
<#assign title="恢复运单"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">恢复运单</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">

            <form role="form" method="post" action="/order/${order.id}/recover" id="main">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                <table class="table table-striped table-bordered table-hover">
                    <tr>
                        <td>批次</td>
                        <td>${order.batch}</td>
                    </tr>

                    <tr>
                        <td>运单号</td>
                        <td>${order.trackingNumber}</td>
                    </tr>

                    <tr>
                        <td>收件人</td>
                        <td>${order.contact}</td>
                    </tr>
                    <tr>
                        <td>收件人电话</td>
                        <td>${order.phone}</td>
                    </tr>
                    <tr>
                        <td>收件人地址</td>
                        <td>${order.address}</td>
                    </tr>
                    <tr>
                        <td>邮编：</td>
                        <td>${order.zipCode}</td>
                    </tr>
                    <tr>
                        <td>商品信息：</td>
                        <td>${order.goodsName}</td>
                    </tr>
                    <tr>
                        <td>商品数量：</td>
                        <td>${order.quantity}</td>
                    </tr>
                    <tr>
                        <td>申报价格：</td>
                        <td>${order.declared}</td>
                    </tr>
                    <tr>
                        <td>入库重量：</td>
                        <td>${order.inTime}</td>
                    </tr>
                    <tr>
                        <td>拒绝发货原因：</td>
                        <td>${order.errorMsg}</td>
                    </tr>
                </table>

                <button class="btn btn-default" type="submit">恢复</button>
            </form>
        </div>
    </div>
</div>

<#include "/_layout/script.ftl"/>
</body>
</html>
