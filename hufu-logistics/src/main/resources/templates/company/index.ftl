<!DOCTYPE html>
<html lang="en">
<#assign title="大客户用户管理" />
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
                大客户用户管理
                <small><a href="/company/create">新增用户</a></small>
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
                            <td>大客户名称</td>
                            <th>登陆账号</th>
                            <td>余额</td>
                            <th>操作</th>
                        </tr>
                        </thead>

                        <tbody>
                        <#list companies as o>
                            <#if !o.isAdmin() >
                            <tr>
                                <td>${o.id}</td>
                                <td>${o.name}</td>
                                <td>${o.username}</td>
                                <td>${o.money}</td>
                                <td><a href="/company/${o.id}/update">修改信息</a></td>
                            </tr>
                            </#if>
                        </#list>
                        </tbody>
                    </table>
                </form>

            </div>
        </div>
    </div>
</div>

<#include "/_layout/script.ftl"/>
</body>
</html>