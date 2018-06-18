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
            <form role="form" method="post" action="/admin/user/create" id="main">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">${title}</h3>
                    </div>
                    <div class="panel-body">
                        <div class="form-group">
                            <label for="username">登陆账号：</label>
                            <input type="text" class="form-control" id="username" name="username" value="${username!}">
                        </div>
                        <div class="form-group">
                            <label for="password">登陆密码：</label>
                            <input type="text" class="form-control" id="password" name="password" value="${password!}">
                        </div>

                        <div class="form-group">
                            <label for="name">用户昵称：</label>
                            <input type="text" class="form-control" id="name" name="name" value="${name!}">
                        </div>

                        <#if error??>
                        <p class="margin text-danger">${error!}</p>
                        </#if>
                    </div>
                </div>

                <button type="submit" class="btn btn-default" id="btn">提交</button>
            </form>
        </div>
    </div>
</div>


<#include "*/admin/_layout/script.ftl"/>
</body>
</html>