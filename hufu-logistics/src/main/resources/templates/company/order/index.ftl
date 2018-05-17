<!DOCTYPE html>
<html lang="en">
<#assign title="运单列表" />
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">运单管理（
            <#switch status>
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
                ）
            </h1>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div style="text-align: center;">
                <form class="form-horizontal" method="get" action="/order/index">
                    <input type="hidden" name="status" value="${status}">

                    <div class="form-group">
                        <label for="ts" class="col-sm-4 control-label">
                        <#switch status><#case 1>创建<#break ><#case 2>入库<#break ><#case 3>发货<#break ></#switch>
                            时间（开始）：</label>
                        <div class="col-sm-4">
                            <input type="text" id="ts" name="ts" class="form-control" placeholder="时间筛选"
                                   value="${ts!}">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="te" class="col-sm-4 control-label">
                        <#switch status><#case 1>创建<#break ><#case 2>入库<#break ><#case 3>发货<#break ></#switch>
                            时间（结束）：</label>
                        <div class="col-sm-4">
                            <input type="text" id="te" name="te" class="form-control" placeholder="时间筛选"
                                   value="${te!}">
                        </div>
                    </div>

                <#if status == 3>
                    <div class="form-group">
                        <label for="expressId" class="col-sm-4 control-label">渠道筛选：</label>
                        <div class="col-sm-4">
                            <select class="form-control" name="expressId" id="expressId">
                                <option value="-1">请选择</option>
                                <#list companyExpressList as express>
                                    <option value="${express.id}">${express.name}</option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </#if>

                    <div class="form-group">
                        <label for="search-keyword" class="col-sm-4 control-label">模糊查询：</label>
                        <div class="col-sm-4">
                            <input type="text" id="search-keyword" name="keyword" class="form-control"
                                   placeholder="批次、运单号" value="${keyword!}">
                        </div>
                    </div>


                    <div>
                        <button type="submit" class="btn btn-default" style="margin-right: 15px;">查询</button>
                        <a href="/order/index?status=${status}" class="btn btn-default">重置</a>
                    </div>
                </form>


                <div class="option">
                    <a class="btn btn-default" href="/order/excel/upload">导入</a>
                    <a class="btn btn-default" href="/order/excel/upload/lg">导入物流信息</a>
                    <button id="export" class="btn btn-default" type="button">多选</button>
                </div>

            </div>
            <div class="table-responsive">
                <form id="selectForm" action="/order/select" method="get">
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
                                <th>保额</th>
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
                                <th>保额</th>
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
                                <th>操作</th>
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
                                    <td>${order.insurance!}</td>
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
                                    <td>${order.insurance!}</td>
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
                                    <td><a href="/order/${order.id}/show">${order.trackingNumber!}</a></td>
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
                                    <td><a href="/order/${order.id}/update">修改</a></td>
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
                </form>

                <nav aria-label="Page navigation" style="text-align: center;">
                    <ul class="pagination">
                    <#if orders.isFirst() != true >
                        <li>
                            <a href="/order/index?keyword=${keyword!}&status=${status!}&page=${orders.previousPageable().pageNumber}&ts=${ts!}&te=${te!}&expressId=${expressId!}"
                               aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </#if>


                    <#if orders.isLast() != true>
                        <li>
                            <a href="/order/index?keyword=${keyword!}&status=${status!}&page=${orders.nextPageable().pageNumber}&ts=${ts!}&te=${te!}&expressId=${expressId!}"
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
    $('#ts').datepicker({
        format: "yyyy-mm-dd",
        keyboardNavigation: false,
        forceParse: false,
        autoclose: true
    });

    $('#te').datepicker({
        format: "yyyy-mm-dd",
        keyboardNavigation: false,
        forceParse: false,
        autoclose: true
    });

    $('#all').on('click', function (event) {
        if ($(this).prop("checked")) {
            $('input').prop('checked', true);
        } else {
            $('input').prop("checked", false);
        }
    });

    $('#export').click(function () {
        if ($("input[name='tracking_number[]']:checked").length > 0) {
            $('#selectForm').submit();
        } else {
            alert("请选择！");
        }
    });
</script>
</body>
</html>