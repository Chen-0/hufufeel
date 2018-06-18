<!DOCTYPE html>
<html lang="en">
<#assign title="收取仓租费" />
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


            <form method="post" action="/admin/user/store_cost">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="table-responsive">
                    <table class="bordered">
                        <thead>
                        <tr>
                            <th>#</th>
                            <th>账号</th>
                            <th>用户名</th>
                        </tr>
                        </thead>

                        <tbody>
                        <#list elements as o>
                        <tr>
                            <td>${o.id} <input type="hidden" name="ids[]" value="${o.id}"></td>
                            <td>${o.username}</td>
                            <td>${o.name}</td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>


                <div class="row" style="margin-top: 20px;">
                    <div class="col-md-6 col-md-offset-3">

                        <div class="form-group">
                            <label id="total">仓租费（USD）</label>
                            <input class="form-control" name="total" id="total">
                        </div>

                        <button class="btn btn-primary" type="submit">提交</button>
                    </div>
                </div>

            </form>


        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
</body>
</html>