<!DOCTYPE html>
<html lang="en">
<#assign title="新增用户"/>
<#include "*/admin/_layout/head.ftl" />

<body>
<#include "*/admin/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">${title}</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">
            <form class="xn-form" role="form" method="post" action="/admin/user/create">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">${title}</h3>
                    </div>
                    <div class="panel-body">
                        <@formGroup key="登陆账号" name="username"></@formGroup>
                        <@formGroup key="登陆密码" name="password"></@formGroup>
                        <@formGroup key="客户昵称" name="name"></@formGroup>
                        <@formGroup key="客服电话" name="csPhone"></@formGroup>
                        <@formGroup key="客服QQ" name="csQQ"></@formGroup>
                        <@formGroup key="客户编号" name="hwcSn"></@formGroup>
                    </div>

                    <div class="panel-footer clearfix">
                        <button type="submit" class="btn btn-default" id="btn">提交</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>


<#include "*/admin/_layout/script.ftl"/>
</body>
</html>