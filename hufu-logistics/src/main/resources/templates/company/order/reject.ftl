<!DOCTYPE html>
<html lang="en">
<#assign title="拒绝发货"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">拒绝发货</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">
            <form role="form" method="post" action="/order/reject/${order.id}" id="main">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                <table class="table table-striped table-bordered table-hover">
                    <tr>
                        <td>批次</td>
                        <td>${order.batch!}</td>
                    </tr>

                    <tr>
                        <td>大客户名称</td>
                        <td>${order.company.name!}</td>
                    </tr>

                    <tr>
                        <td>运单号</td>
                        <td>${order.trackingNumber!}</td>
                    </tr>

                    <tr>
                        <td>收件人</td>
                        <td>${order.contact!}</td>
                    </tr>
                    <tr>
                        <td>收件人电话</td>
                        <td>${order.phone!}</td>
                    </tr>
                    <tr>
                        <td>收件人地址</td>
                        <td>${order.address!}</td>
                    </tr>
                    <tr>
                        <td>邮编：</td>
                        <td>${order.zipCode!}</td>
                    </tr>
                    <tr>
                        <td>商品信息：</td>
                        <td>${order.goodsName!}</td>
                    </tr>
                    <tr>
                        <td>商品数量：</td>
                        <td>${order.quantity!}</td>
                    </tr>
                    <tr>
                        <td>申报价格：</td>
                        <td>${order.declared!}</td>
                    </tr>

                    <tr>
                        <td>单价（元/磅）：</td>
                        <td id="price">0</td>
                    </tr>

                    <tr>
                        <td>费用：</td>
                        <td id="total">0</td>
                    </tr>

                    <tr>
                        <td>拒绝发货原因：</td>
                        <td>
                            <textarea class="form-control" rows="3" name="error_msg"></textarea>
                        </td>
                    </tr>

                </table>

                <button type="submit" class="btn btn-default" id="btn">提交</button>
            </form>
        </div>
    </div>
</div>


<#include "/_layout/script.ftl"/>
</body>
</html>
