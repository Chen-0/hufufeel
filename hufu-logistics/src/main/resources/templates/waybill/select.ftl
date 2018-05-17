<!DOCTYPE html>
<html lang="en">
<#assign title="多选运单"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">多选运单</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">
            <form role="form" method="get" action="#" id="main">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <textarea class="form-control" rows="8" name="tracking_number"
                          id="items"><#list orders as order>${order.trackingNumber} </#list></textarea>
                <div class="text-center" style="margin-top: 1.5rem;">
                    <button type="submit" class="btn btn-primary" id="btn">提交</button>
                </div>
            </form>
        </div>
    </div>

    <div class="row" style="margin-top: 2.5rem;">
        <div class="col-lg-offset-2 col-lg-8">
            <table class="table table-condensed table-bordered">
                <tr>
                    <td>
                        <button class="btn btn-success" type="button" id="export_lg">物流信息导出</button>
                    </td>
                    <td>
                        <a class="btn btn-primary"
                           href="/waybill/print?tracking_number=<#list orders as order>${order.trackingNumber} </#list>&flag=2"
                           target="_blank">
                            打印(Letter, Plugin Lodop)
                        </a>
                    </td>
                </tr>
            </table>

        <#--<a class="btn btn-success" href="#">-->
        <#--打印(Letter,网页直接打印)-->
        <#--</a>-->


            <div class="alert alert-warning" style="margin-top: 1rem;">
                <strong>提示：</strong><br>
                <p>1. 打印需要下载插件 <a href="http://www.lodop.net/download/CLodop_Setup_for_Win32NT_2.090.zip">32位C-Lodop独立版(CLodop2.090)</a>
                    并支持 IE 浏览器（极速模式），火狐浏览器， 360 浏览器。（不支持 Chrome 浏览器）</p>
                <p>2. 导出（广州渠道导出和 E 特快渠道导出）只能导出运单状态为已出库的运单，若运单状态不为已出库，那么将会跳过。</p>
                <p>3. 多选入库，如果运单的状态不为待入库，则此运单会被跳过。</p>
            </div>
        </div>
    </div>

    <div class="row" style="margin-top: 2.5rem;">
        <div class="col-lg-offset-2 col-lg-8">
            <table class="table table-striped table-bordered table-hover" id="dataTables">
                <thead>
                <tr>
                    <th>TrackingNumber</th>
                    <th>客户标示</th>
                    <th>用户等级</th>
                    <th>入库重</th>
                    <th>出库重</th>
                    <th>发货渠道</th>
                    <th>申报价格</th>
                    <th>原运费</th>
                    <th>现运费</th>
                    <th>保险费</th>
                    <th>补差价</th>
                    <th>额外费用</th>
                    <th>总费用</th>
                </tr>
                </thead>
                <tbody>
                <#list orders as order>
                <tr>
                    <td>${order.trackingNumber}</td>
                    <td>${order.customer}</td>
                    <td>${order.user.role.name}</td>
                    <td>${order.weight}</td>
                    <td>${order.outWeight}</td>
                    <td>${order.expressName!}</td>
                    <td>${order.declareCost!}</td>
                    <td>${order.price!}</td>
                    <td>${order.cost!}</td>
                    <td>${order.insuranceCost!}</td>
                    <td>${order.mendCost!}</td>
                    <td>${order.extra_cost!}</td>
                    <td>${order.allCost!}</td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>

</div>


<#include "/_layout/script.ftl"/>
<script>
    $(function () {
        var $main = $('#main');

        $('#export_lg').click(function () {
            $main.attr('action', '/waybill/export/lg');
            $main.attr('method', 'get');
            $main.submit();
        });

    })
</script>
</body>
</html>