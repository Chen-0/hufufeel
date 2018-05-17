<!DOCTYPE html>
<html lang="en">
<#assign title="发货"/>
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
            <h1 class="page-header">发货</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-3 col-lg-6">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Tracking Number：${waybill.trackingNumber!}</h3>
                </div>
                <div class="panel-body">
                    <div class="text-center">
                        <img src="/barcode/${waybill.trackingNumber!}" alt="">
                    </div>
                    <table class="table table-bordered">
                        <tr>
                            <td colspan="3"><p>HUFU单号 ：${waybill.trackingNumber!}</p></td>
                        </tr>
                        <tr>
                            <td colspan="3"><p>海外单号 ：${waybill.expressnum!}</p></td>
                        </tr>
                        <tr>
                            <td>用户邮箱：${waybill.user.username}</td>
                            <td>客户标识 ：${waybill.customer!}</td>
                            <td></td>
                        </tr>

                        <tr>
                            <td>仓 库 ：${waybill.storageNo}</td>
                            <td>运输渠道 ：${waybill.expressName!}</td>
                            <td></td>
                        </tr>
                        <tr>
                            <td colspan="3"><p>入库重：${waybill.weight!}</p></td>
                        </tr>

                    </table>
                </div>

                <div class="panel-footer" style="text-align: center;">
                    <a href="/waybill/index?s=${status}" class="btn btn-primary btn-lg">确定</a>
                </div>
            </div>

        </div>
    </div>
</div>


<#include "/_layout/script.ftl"/>

</body>
</html>
