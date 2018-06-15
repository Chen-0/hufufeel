<!DOCTYPE html>
<html lang="en">
<#assign title="货品上架" />
<#include "*/admin/_layout/head.ftl" />

<body>
<#include "*/admin/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
            ${title}
            </h1>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-10 col-xs-offset-1">
            <form action="/admin/package/${o.id}/publish" method="post">
                <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>

                <div class="panel panel-info">
                    <div class="panel-heading">
                    ${title} 参考号：${o.referenceNumber!}
                    </div>
                    <div class="panel-body">
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
                                    <td>商品</td>
                                    <td>${p.product.productName}</td>
                                    <td>${p.product.productSku}</td>
                                    <td>预计：${p.expectQuantity} 件</td>
                                    <td>单件重量：${p.product.weight} KG</td>
                                    <td>
                                        实际数量（件）${p.quantity}
                                    </td>
                                </tr>
                                </#list>
                                </tbody>
                            </table>
                        </div>

                        <div class="row" style="margin-top: 25px;">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label for="total">上架费用（USD）（自动计算）</label>
                                    <input type="text" class="form-control" id="total" name="total" value="${total}">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer clearfix">
                        <button class="pull-right btn btn-primary" type="submit">提交</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
</body>
</html>