<!DOCTYPE html>
<html lang="en">
<#assign title="海淘客运单列表" />
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">海淘客运单列表（
            <#switch status>
                <#case 0>
                    货物入库
                    <#break >
                <#case 1>
                    未支付运单
                    <#break >
                <#case 3>
                    发货管理
                    <#break >
                <#case 4>
                    补差发货管理
                    <#break >
                <#case 5>
                    运单管理
                    <#break >
                <#case 99>
                    合并订单管理
                    <#break >
            </#switch>
                ）
            </h1>
        </div>
    </div>

<#if success?exists && (success?length > 0) >
    <div class="alert alert-success alert-dismissable alert-message">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
    ${success}
    </div>
</#if>

    <div class="row">
        <div class="col-md-12">
            <div style="text-align: center; margin-bottom: 1.5rem;">
                <form class="form-horizontal" method="get" action="/waybill/index">
                    <input type="hidden" name="s" value="${status}">

                    <div class="form-group">
                        <label for="search-keyword" class="col-sm-4 control-label">查询：</label>
                        <div class="col-sm-4">
                            <input type="text" id="search-keyword" name="keyword" class="form-control"
                                   placeholder="HUFU单号、快递单号" value="${keyword!}">
                        </div>
                    </div>

                    <div>
                        <button type="submit" class="btn btn-default" style="margin-right: 15px;">查询</button>
                        <a href="/waybill/index?s=${status}" class="btn btn-default">重置</a>
                    <#switch status>
                        <#case 0>
                            <a class="btn btn-primary" href="/waybill/create">货物入库</a>
                            <#break >
                    </#switch>
                    </div>
                </form>

            <#switch status>
                <#case 3>
                    <div class="option">
                        <button id="export" class="btn btn-default" type="button">多选打印运单</button>
                    </div>
                    <#break >
                <#case 5>
                    <div class="option">
                        <button id="export" class="btn btn-default" type="button">多选打印运单</button>
                        <a class="btn btn-default" href="/waybill/import/lg">导入物流信息</a>
                    </div>

                    <#break >
            </#switch>
            </div>


            <div class="table-responsive">
                <form id="selectForm" action="/waybill/select" method="get">
                    <table class="table table-bordered table-hover table-condensed">
                        <thead>
                        <tr>
                        <#switch status>
                            <#case 0>
                                <th>快递单号</th>
                                <th>客户标示</th>
                                <th>快递公司</th>
                                <th>入库仓库</th>
                                <th>状 态</th>
                                <th>创建时间</th>
                                <th>入库</th>
                                <th>删除</th>
                                <#break >
                            <#case 1>
                                <th>TrackingNumber</th>
                                <th>快递单号</th>
                                <th>客户标示</th>
                                <th>快递公司</th>
                                <th>入库仓库</th>
                                <th>状 态</th>
                                <th>创建时间</th>
                                <th>删除</th>
                                <#break >
                            <#case 3>
                                <th class="text-center">
                                    <input type="checkbox" id="all">
                                </th>
                                <th>TrackingNumber</th>
                                <th>客户标示</th>
                                <th>用户等级</th>
                                <th>入库重</th>
                                <th>发货渠道</th>
                                <th>申报价格</th>
                                <th>原运费</th>
                                <th>现运费</th>
                                <th>保险费</th>
                                <th>额外收费</th>
                                <th>补差价</th>
                                <th>总费用</th>
                                <th>支付状态</th>
                                <th>发 货</th>
                                <th>补差价</th>
                            <#--<th>退款处理</th>-->
                                <#break >
                            <#case 4>
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
                                <th>支付状态</th>
                                <th>发 货</th>
                                <#break >
                            <#case 5>
                                <th class="text-center">
                                    <input type="checkbox" id="all">
                                </th>
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
                                <th>出库时间</th>
                                <th>更新物流信息</th>
                                <#break >
                            <#case 99>
                                <th>快递单号</th>
                                <th>HUFU单号</th>
                                <th>快递公司</th>
                                <th>入库重</th>
                                <th>入库仓库</th>
                                <th>仓位号</th>
                                <th>状 态</th>
                                <th>创建时间</th>
                                <th>操作</th>
                                <#break >
                        </#switch>
                        </tr>
                        </thead>

                        <tbody>
                        <#list orders.getContent() as order>
                        <tr>
                            <#switch status>
                                <#case 0>
                                    <td>${order.expressnum!}</td>
                                    <td>${order.customer!}</td>
                                    <td>${order.express!}</td>
                                    <td>${order.agea!}</td>
                                    <td>${order.isOurGood!}</td>
                                    <td><#if order.createTime?exists>${order.createTime?datetime} </#if></td>
                                    <td><a href="/waybill/${order.id}/in">入库</a></td>
                                    <td><a href="#" data-type="destroy" data-id="${order.id}">删除</a></td>
                                    <#break >
                                <#case 1>
                                    <td><a href="/waybill/${order.id}/update">${order.trackingNumber}</a></td>
                                    <td>${order.expressnum!}</td>
                                    <td>${order.customer!}</td>
                                    <td>${order.express!}</td>
                                    <td>${order.agea!}</td>
                                    <td>${order.isOurGood!}</td>
                                    <td><#if order.createTime?exists>${order.createTime?datetime} </#if></td>
                                    <td><a href="#" data-type="destroy" data-id="${order.id}">删除</a></td>
                                    <#break >
                                <#case 3>
                                    <td class="text-center">
                                        <input type="checkbox" name="trackingNumber[]"
                                               value="${order.trackingNumber}"/>
                                    </td>
                                    <td><a href="/waybill/${order.id}/update">${order.trackingNumber}</a></td>
                                    <td>${order.customer!}</td>
                                    <td>${order.user.role.name}</td>
                                    <td>${order.weight!}</td>
                                    <td>${order.expressName!}</td>
                                    <td>${order.declareCost!}</td>
                                    <td>${order.price!}</td>
                                    <td>${order.cost!}</td>
                                    <td>${order.insuranceCost!}</td>
                                    <td>${order.extra_cost!}</td>
                                    <td>${order.mendCost!}</td>
                                    <td>${order.allCost!}</td>
                                    <td>${order.paymentStatus!}</td>
                                    <td><a href="/waybill/${order.id}/send">发货</a></td>
                                    <td><a href="/waybill/${order.id}/mend">补差价</a></td>
                                <#--<td>退款处理</td>-->
                                    <#break >
                                <#case 4>
                                    <td><a href="/waybill/${order.id}/update">${order.trackingNumber}</a></td>
                                    <td>${order.customer!}</td>
                                    <td>${order.user.role.name}</td>
                                    <td>${order.weight!}</td>
                                    <td>${order.outWeight!}</td>
                                    <td>${order.expressName!}</td>
                                    <td>${order.declareCost!}</td>
                                    <td>${order.price!}</td>
                                    <td>${order.cost!}</td>
                                    <td>${order.insuranceCost!}</td>
                                    <td>${order.mendCost!}</td>
                                    <td>${order.extra_cost!}</td>
                                    <td>${order.allCost!}</td>
                                    <td>${order.paymentStatus!}</td>
                                    <td><a href="/waybill/${order.id}/send">发货</a></td>
                                    <#break >
                                <#case 5>
                                    <td class="text-center">
                                        <input type="checkbox" name="trackingNumber[]"
                                               value="${order.trackingNumber}"/>
                                    </td>
                                    <td><a href="/waybill/${order.id}/update">${order.trackingNumber}</a></td>
                                    <td>${order.customer!}</td>
                                    <td>${order.user.role.name}</td>
                                    <td>${order.weight!}</td>
                                    <td>${order.outWeight!}</td>
                                    <td>${order.expressName!}</td>
                                    <td>${order.declareCost!}</td>
                                    <td>${order.price!}</td>
                                    <td>${order.cost!}</td>
                                    <td>${order.insuranceCost!}</td>
                                    <td>${order.mendCost!}</td>
                                    <td>${order.extra_cost!}</td>
                                    <td>${order.allCost!}</td>
                                    <td><#if order.outTime?exists>${order.outTime?date}</#if></td>
                                    <td><a href="/waybill/${order.id}/update/lg">更新物流信息</a></td>
                                    <#break >
                                <#case 99>
                                    <td>${order.expressnum!}</td>
                                    <td><a href="/waybill/${order.id}/update">${order.trackingNumber}</a></td>
                                    <td>${order.express!}</td>
                                    <td>${order.weight!}</td>
                                    <td>${order.agea!}</td>
                                    <td>${order.storageNo!}</td>
                                    <td>已经入库</td>
                                    <td><#if order.createTime?exists>${order.createTime?datetime} </#if></td>
                                    <td><a href="/waybill/${order.id}/recover"
                                           onclick="return confirm('确定执行恢复操作？')">恢复</a></td>
                                    <#break >
                            </#switch>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </form>

                <nav aria-label="Page navigation" style="text-align: center;">
                    <ul class="pagination">
                    <#if orders.isFirst() != true >
                        <li>
                            <a href="/waybill/index?keyword=${keyword!}&s=${status!}&page=${orders.previousPageable().pageNumber}"
                               aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </#if>


                    <#if orders.isLast() != true>
                        <li>
                            <a href="/waybill/index?keyword=${keyword!}&s=${status!}&page=${orders.nextPageable().pageNumber}"
                               aria-label="Previous">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </#if>
                    </ul>

                    <p>
                        当前第${orders.getNumber() + 1}页，一共${orders.getTotalPages()}页，一共${orders.getTotalElements()}条数据</p>
                </nav>
            </div>
        </div>
    </div>
</div>


<#include "/_layout/script.ftl"/>
<script>
    $(function () {
        $('a[data-type=destroy]').click(function (e) {
            e.preventDefault();
            if (!confirm('你确定删除该运单，删除后将无法恢复')) {
                return 0;
            }
            var id = $(this).attr('data-id');

            $.ajax({
                url: '/waybill/' + id + '/destroy',
                success: function (data) {
                    if (data.code == 0) {
                        alert('删除成功！');
                        window.location.reload();
                    }
                }
            });
        });

        $('#all').on('click', function (event) {
            if ($(this).prop("checked")) {
                $('input').prop('checked', true);
            } else {
                $('input').prop("checked", false);
            }
        });

        $('#export').click(function () {
            if ($("input[name='trackingNumber[]']:checked").length > 0) {
                $('#selectForm').submit();
            } else {
                alert("请选择！");
            }
        });
    })
</script>
</body>
</html>