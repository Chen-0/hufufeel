<!DOCTYPE html>
<html lang="en">
<#assign title="货品管理" />
<#include "*/admin/_layout/head.ftl" />

<body>
<#include "*/admin/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
                入库 <small>${o.referenceNumber}</small>
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
        <div class="col-xs-10 col-xs-offset-1">
            <form action="/admin/package/${o.id}/inbound" method="post">
                <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>
            <div class="table-responsive">
                <table class="bordered">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>参考号</th>
                        <th>客户</th>
                        <th>仓库</th>
                        <th>状态</th>
                        <th>提交时间</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr>
                        <td>${o.id}</td>
                        <td>${o.referenceNumber}</td>
                        <td>${o.nickname}</td>
                        <td>${o.warehouseName}</td>
                        <td>${o.status.getValue()}</td>
                        <td>${o.createdAt?string}</td>
                    </tr>
                    <#list o.packageProducts as p>
                    <tr class="table-sub-item">
                        <td>商品<input type="hidden" name="p[]" value="${p.product.id}"></td>
                        <td>${p.product.productName}</td>
                        <td>${p.product.productSku}</td>
                        <td>预计：${p.expectQuantity} 件</td>
                        <td colspan="2">实际数量（件）：<input type="text" class="form-control" placeholder="" name="qty[]"></td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
                    <div class="text-center">
                        <button class="btn btn-primary" type="submit">提交</button>
                    </div>
            </form>
            </div>
        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
</body>
</html>