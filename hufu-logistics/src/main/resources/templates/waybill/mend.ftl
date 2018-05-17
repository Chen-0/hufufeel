<!DOCTYPE html>
<html lang="en">
<#assign title="补差价"/>
<#include "/_layout/head.ftl" />

<body>
<style>
    p {
        margin: 0;
    }
</style>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">补差价</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-3 col-lg-6">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Tracking Number：${waybill.trackingNumber}</h3>
                </div>
                <div class="panel-body">
                    <div class="text-center">
                        <img src="/barcode/${waybill.trackingNumber}" alt="">
                    </div>
                    <table class="table table-bordered">
                        <tr>
                            <td colspan="3">收货信息 ：${waybill.userAddress.addressa}</td>
                        </tr>
                        <tr>
                            <td>用户邮箱：${waybill.user.username}</td>
                            <td>客户标识 ：${waybill.customer!}</td>
                            <td>身份证号码 ：${waybill.userAddress.identity}</td>
                        </tr>

                        <tr>
                            <td>仓 库 ：${waybill.storageNo}</td>
                            <td>运输渠道 ：${waybill.expressName!}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td colspan="3"><p>海外单号 ：${waybill.expressnum!}</p></td>
                        </tr>
                        <tr>
                            <td colspan="3"><p>合箱订单号 ：${waybill.old_tracking_number!}</p></td>
                        </tr>
                        <tr>
                            <td>选择的操作要求 ：${waybill.operation!}</td>
                            <td colspan="2">备注 ：${waybill.beizhu!}</td>
                        </tr>
                        <tr>
                            <td colspan="3">申报价格 ：${waybill.declareCost!}</td>
                        </tr>
                        <tr>
                            <td>额外收费 ：${waybill.extra_cost!}</td>
                            <td colspan="2">额外收费原因 ：${waybill.extra_cost_reason!}</td>
                        </tr>
                        <tr>
                            <td>补差价 ：${waybill.mendCost!}</td>
                            <td colspan="2">补差原因 ：${waybill.mend_reason!}</td>
                        </tr>
                    </table>
                </div>
            </div>

            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">货物信息</h3>
                </div>
                <div class="panel-body">
                    <table class="table">
                        <thead>
                        <tr>
                            <td width="18%">货物分类（必填）：</td>
                            <td width="18%">货物子分类（必填）：</td>
                            <td width="18%">英文品牌名：</td>
                            <td width="18%">货物明细：</td>
                            <td width="18%">数 量：</td>
                        </tr>
                        </thead>
                        <tbody id="container">
                        <#list waybill.goods as goods>
                        <tr>
                            <td>${goods.gooda}</td>
                            <td>${goods.goodb}</td>
                            <td>${goods.brandname}</td>
                            <td>${goods.goodname}</td>
                            <td>${goods.contnum}</td>

                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>

            <form role="form" method="post" action="/waybill/${waybill.id}/mend" id="main">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">补差价信息</h3>
                    </div>
                    <div class="panel-body">
                        <div class="form-group">
                            <label for="outweight">货物出库重（磅）：</label>
                            <input type="text" class="form-control" id="outweight" name="outweight">
                        </div>
                        <div class="form-group">
                            <label for="mendcost">补差价（必填）：</label>
                            <input type="text" class="form-control" id="mendcost" name="mendcost">
                        </div>
                        <div class="form-group">
                            <label for="mendReason">补差价原因：（必填）：</label>
                            <input type="text" class="form-control" id="mendReason" name="mendReason">
                        </div>
                    </div>
                    <div class="panel-footer">
                        <button class="btn btn-primary" type="submit">提交</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>


<#include "/_layout/script.ftl"/>

</body>
</html>
