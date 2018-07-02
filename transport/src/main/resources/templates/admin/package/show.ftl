<!DOCTYPE html>
<html lang="en">
<#if ele.type.ordinal() == 0>
    <#assign title="入库单详情" />
<#else>
    <#assign title="退货单详情" />
</#if>

<#include "*/admin/_layout/head.ftl" />

<body>
<#include "*/admin/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
            ${title} <small>${ele.sn}</small>
            </h1>
        </div>
    </div>

<#if success?? >
    <div class="alert alert-success alert-dismissable alert-message">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
    ${success}
    </div>
</#if>

    <div class="row">
        <div class="col-xs-8 col-xs-offset-2">

            <div class="box">
                <div class="box-body">
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>入库单号</th>
                            <th>参考号</th>
                            <th>仓库</th>
                            <th>SKU数</th>
                            <th>总件数</th>
                            <th>实际总件数</th>
                            <th>总重量</th>
                            <th>状态</th>
                            <th>创建时间</th>
                            <th>备注</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#assign tw = 0>
                        <#assign qty = ele.quantity>
                        <#if ele.status.ordinal() == 0 || ele.status.ordinal() == 3 || ele.status.ordinal() == 5>
                            <#assign qty = ele.expectQuantity>
                        </#if>
                        <#list ele.packageProducts as pp>
                            <#assign tw = tw + qty * pp.product.weight>
                        </#list>
                        <tr>
                            <td>${ele.cn}</td>
                            <td>${ele.referenceNumber}</td>
                            <td>${ele.warehouseName}</td>
                            <td>${ele.packageProducts?size}</td>
                            <td>${ele.expectQuantity}</td>
                            <td>${ele.quantity}</td>
                            <td>${tw}</td>
                            <td>${ele.status.getValue()}</td>
                            <td>${ele.createdAt?string}</td>
                            <td>${ele.comment!}</td>
                        </tr>
                        </tbody>
                    </table>


                    <table class="table table-bordered" style="margin-top: 25px;">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>货品名称</th>
                            <th>货品SKU</th>
                            <th>预估数量</th>
                            <th>实际数量</th>
                            <th>单件重量</th>
                            <th>总重量</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list ele.packageProducts as pp>
                        <tr>
                            <td>${pp_index + 1}</td>
                            <td><a href="/product/${pp.productId}/show">${pp.product.productName}</a></td>
                            <td>${pp.product.productSku}</td>
                            <td>${pp.expectQuantity}</td>
                            <td>${pp.quantity}</td>
                            <td>${pp.product.weight}</td>
                            <td>${pp.product.weight * pp.quantity}</td>
                            <td><a href="/product/${pp.productId}/show">货品详情</a></td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>

                <#if statements?exists && statements?size gt 0 >
                    <table class="table table-bordered" style="margin-top: 25px;">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>费用类型</th>
                            <th>费用说明</th>
                            <th>总额</th>
                            <th>状态</th>
                            <th>创建时间</th>
                            <th>交易时间</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list statements as e>
                            <tr>
                                <td>${e_index + 1}</td>
                                <td>${e.type.getValue()}</td>
                                <td>${e.comment!}</td>
                                <td>${e.total} USD</td>
                                <td>${e.status.getValue()}</td>
                                <td>${e.createdAt?string}</td>
                                <td>
                                    <#if e.payAt??>
                                        ${e.payAt?string}
                                    </#if>
                                </td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </#if>
                </div>

                <div class="box-footer clearfix">
                    <a class="btn btn-primary" href="/package/${ele.id}/print?type=sku" target="_blank">打印SKU</a>
                </div>
            </div>


        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
</body>
</html>