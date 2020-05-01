<!DOCTYPE html>
<html lang="en">
<#assign title="已删除的入库单 & 退货单" />
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
        <div class="col-xs-12">

            <div class="table-responsive">
                <table class="bordered">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>平台单号</th>
                        <th>快递单号</th>
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
                    <#list elements as o>
                    <tr>
                        <td><a href="/admin/package/${o.id}/show">${o.cn}</a></td>
                        <td>${o.referenceNumber}</td>
                        <td>${o.searchNo!}</td>
                        <td>${o.contact}</td>
                        <td>${o.nickname} NO.${o.user.hwcSn}</td>
                        <td>${o.warehouseName}</td>
                        <td>${o.type.value}</td>
                        <td>${o.status.value}</td>
                        <td>${o.createdAt?string}</td>
                        <td>${o.comment!}</td>
                        <td>
                            <a href="/admin/package/${o.id}/show">详情</a>
                            <a href="/admin/package/${o.id}/recover">恢复</a>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>

            </div>
        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
</body>
</html>