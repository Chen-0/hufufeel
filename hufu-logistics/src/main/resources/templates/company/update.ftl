<!DOCTYPE html>
<html lang="en">
<#assign title="更新用户信息"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid" style="padding-bottom: 48px;">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">更新用户信息</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">

            <form role="form" method="post" action="/company/${company.id}/update">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                <div class="form-group">
                    <label>登陆账号：</label>
                    <input class="form-control" name="username" value="${company.username!}">
                </div>

                <div class="form-group">
                    <label>重置密码：</label>
                    <input class="form-control" name="password" >
                    <p>如不需要重置密码，请不要填写任何内容</p>
                </div>

                <div class="form-group">
                    <label>大客户名称：</label>
                    <input class="form-control" name="name" value="${company.name!}">
                </div>

                <div class="form-group">
                    <label>余额：</label>
                    <input class="form-control" name="money" value="${company.money!}">
                </div>

                <#list company.companyRExpressList as o >
                <div class="row">
                    <div class="col-lg-6">
                        <div class="form-group">
                            <label>渠道名称：</label>
                            <input class="form-control" type="text" value="${o.companyExpress.name}" disabled>
                            <input type="hidden" name="ids[]" value="${o.id}">
                        </div>
                    </div>
                    <div class="col-lg-6">
                        <div class="form-group">
                            <label>价格：</label>
                            <input class="form-control" type="text" name="price[]" value="${o.price}">
                        </div>
                    </div>
                </div>
                </#list>


                <button type="submit" id="btn" class="btn btn-default">提交</button>
            </form>
        </div>
    </div>
</div>

<#include "/_layout/script.ftl"/>
</body>
</html>
