<!DOCTYPE html>
<html lang="en">
<#assign title="库存管理" />
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

<#if success?? >
    <div class="alert alert-success alert-dismissable alert-message">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
    ${success}
    </div>
</#if>

    <div class="row">
        <div class="col-xs-8 col-xs-offset-2">
            <form class=" margin-bottom" role="form" method="get" action="/admin/stock/index">
                <div class="row">
                    <div class="form-group col-xs-5">
                        <label for="keyword">关键字：</label>
                        <input class="form-control" id="keyword" name="keyword" type="text" value="${keyword!}">
                    </div>

                    <div class="form-group col-xs-5">
                        <label for="uid">客户：</label>
                        <select class="form-control" name="uid" id="uid">
                            <#list users as u>
                                <option value="${u.id}">${u.name}</option>
                            </#list>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                <#list warehouses as w>
                    <label for="checkbox-id">
                        <#assign cc = false>
                        <#if ws??>
                            <#list ws as cw>
                                <#if cw == w.id>
                                    <#assign cc = true>
                                </#if>
                            </#list>
                        </#if>
                        <#if cc >
                            <input class="form-control x-radio flat-red" type="checkbox" name="w[]" value="${w.id}"
                                   checked> ${w.name}
                        <#else>
                            <input class="form-control x-radio flat-red" type="checkbox" name="w[]"
                                   value="${w.id}"> ${w.name}
                        </#if>

                    </label>
                </#list>
                </div>
                <div class="form-group">
                    <button type="submit" class="btn btn-primary">搜索</button>
                    <a href="/admin/stock/index" class="btn btn-default">重置</a>
                    <a class="btn btn-primary" href="/admin/stock/import">库存导入</a>
                </div>
            </form>

            <table class="table table-bordered table-striped table-condensed table-hover">
                <thead>
                <tr>
                    <th>编号</th>
                    <th>用户</th>
                    <th>货品SKU</th>
                    <th>货品名称</th>
                    <th>货品类型</th>
                    <th>仓库</th>
                    <th>数量</th>
                    <th>总重量</th>
                </tr>
                </thead>
                <tbody>
                <#list elements.getContent() as e>
                <tr>
                    <td>${e_index + 1}</td>
                    <th>${e.user.name}</th>
                    <td><a href="/admin/product/${e.product.id}/show">${e.product.productSku}</a></td>
                    <td><a href="/admin/product/${e.product. id}/show">${e.product.productName}</a></td>
                    <td>${e.product.type.value}</td>
                    <td>${e.warehouse.name}</td>
                    <td>${e.quantity}</td>
                    <td>${e.quantity * e.product.weight}</td>
                </tr>
                </#list>
                </tbody>
            </table>

        <#assign ff="&">
        <#if ws??>
            <#list ws as w>
                <#assign ff="${ff}&w%5B%5D=${w}">
            </#list>
        </#if>

            <nav aria-label="Page navigation" style="text-align: center;">
                <ul class="pagination">
                <#if elements.isFirst() != true >
                    <li>
                        <a href="/admin/stock/index?page=${elements.previousPageable().pageNumber}${ff}&keyword=${keyword!}"
                           aria-label="Previous">
                            <span aria-hidden="true">&laquo;</span>
                        </a>
                    </li>
                </#if>


                <#if elements.isLast() != true>
                    <li>
                        <a href="/admin/stock/index?page=${elements.nextPageable().pageNumber}${ff}&keyword=${keyword!}"
                           aria-label="Previous">
                            <span aria-hidden="true">&raquo;</span>
                        </a>
                    </li>
                </#if>
                </ul>

                <p>
                    当前第${elements.getNumber() + 1}页，一共${elements.getTotalPages()}页，一共${elements.getTotalElements()}条数据</p>
            </nav>


        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
<script>
    $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
        checkboxClass: 'icheckbox_flat-green',
        radioClass: 'iradio_flat-green'
    });
</script>
</body>
</html>