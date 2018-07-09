<!DOCTYPE html>
<html lang="en">
<#assign title="入库单 & 退货单管理" />
<#include "*/admin/_layout/head.ftl" />

<body>
<#include "*/admin/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
                ${title} （
                <#if status??>
                    <#switch status>
                        <#case 0> 待入库 <#break>
                        <#case 1> 已收货 <#break>
                        <#case 2> 已上架 <#break>
                        <#case 3> 已取消 <#break>
                        <#case 4> 已冻结 <#break>
                    </#switch>
                <#else >
                查看所有
                </#if>
                ）
            <#--<small><a href="/company/create">新增货品</a></small>-->
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
        <div class="col-xs-12">

            <div class="table-responsive">
                <table class="bordered">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>参考号</th>
                        <th>收件人</th>
                        <th>客户</th>
                        <th>仓库</th>
                        <th>类型</th>
                        <th>状态</th>
                        <th>提交时间</th>
                        <th>备注</th>
                        <th>操作</th>
                    </tr>
                    </thead>

                    <tbody>
                    <#list elements.getContent() as o>
                    <tr>
                        <td><a href="/admin/package/${o.id}/show">${o.cn}</a></td>
                        <td>${o.referenceNumber}</td>
                        <td>${o.contact}</td>
                        <td>${o.nickname} NO.${o.user.hwcSn}</td>
                        <td>${o.warehouseName}</td>
                        <td>${o.type.value}</td>
                        <td>${o.status.value}</td>
                        <td>${o.createdAt?string}</td>
                        <td>${o.comment!}</td>
                        <td>
                            <a href="/admin/package/${o.id}/show">详情</a>
                            <#switch o.status.ordinal()>
                                <#case 0>
                                    <a href="/admin/package/${o.id}/inbound">入库</a>
                                <#break>
                                <#case 1>
                                    <a href="/admin/package/${o.id}/publish">上架</a>
                                <#break >
                            </#switch>
                        </td>
                    </tr>
                    <#--<#list o.packageProducts as p>-->
                    <#--<tr class="table-sub-item">-->
                        <#--<td>商品</td>-->
                        <#--<td>${p.product.productName}</td>-->
                        <#--<td>${p.product.productSku}</td>-->
                        <#--<td>预计：${p.expectQuantity} 件</td>-->
                        <#--<#if o.status.ordinal() != 0 ||o.status.ordinal() != 3  >-->
                        <#--<td>实际：${p.quantity} 件</td>-->
                        <#--<td colspan="3"></td>-->
                        <#--<#else>-->
                            <#--<td colspan="4"></td>-->
                        <#--</#if>-->
                    <#--</tr>-->
                    <#--</#list>-->
                    </#list>
                    </tbody>
                </table>

                <nav aria-label="Page navigation" style="text-align: center;">
                    <ul class="pagination">
                    <#if elements.isFirst() != true >
                        <li>
                            <a href="/admin/package/index?page=${elements.previousPageable().pageNumber}&status=${status!}"
                               aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </#if>


                    <#if elements.isLast() != true>
                        <li>
                            <a href="/admin/package/index?page=${elements.nextPageable().pageNumber}&status=${status!}"
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
</div>

<#include "*/admin/_layout/script.ftl"/>
</body>
</html>