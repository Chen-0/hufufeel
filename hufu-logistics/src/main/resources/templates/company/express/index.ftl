<!DOCTYPE html>
<html lang="en">
<#assign title="大客户发货渠道" />
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
                大客户发货渠道 <small><a href="/company/express/create">新建发货渠道</a></small>
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
        <div class="col-md-6 col-md-offset-3">

            <div class="table-responsive">
                <form id="selectForm" action="/waybill/select" method="get">
                    <table class="table table-bordered table-hover table-condensed">
                        <thead>
                        <tr>
                            <td>#</td>
                            <td>渠道名称</td>
                            <th>默认价格（元/磅）</th>
                            <td>操作</td>
                        </tr>
                        </thead>

                        <tbody>
                        <#list expresses as o>
                        <tr>
                            <td>${o.id}</td>
                            <td>${o.name}</td>
                            <td>${o.price}</td>
                            <td><a href="/company/express/${o.id}/update">修改</a></td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </form>

            </div>
        </div>
    </div>
</div>

<form method="post" action="" id="mainForm">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
</form>

<#include "/_layout/script.ftl"/>
</body>
</html>