<!DOCTYPE html>
<html lang="en">
<#assign title="打印运单信息"/>
<#include "/_layout/head.ftl" />
<style media=print>
    .PageNext {
        page-break-after: always;
    }
    @media print {
        .noneprint{display:none;}
    }
    @page {
        size: A4;
    }
    .beforeHeader,
    .afterFooter {
        display: none;
    }

    @media print {
        /* 打印时显示 */
        .beforeHeader,
        .afterFooter {
            display: none;
        }
    }

    p {
        margin: 0;
    }
</style>

<body>
<div class="container-fluid">
<#list waybills as waybill>
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
                            <td width="18%">货物分类：</td>
                            <td width="18%">货物子分类：</td>
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
        </div>
    </div>
    <div class="PageNext"></div>
</#list>

<#include "/_layout/script.ftl"/>
<script type="text/javascript">
    $(document).ready(function () {
        setTimeout(function () {
            window.print();
        }, 1000);
    });

</script>

</body>
</html>
