<!DOCTYPE html>
<html lang="en">
<#assign title="新增大客户"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">新增大客户</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">
            <form role="form" method="post" action="/company/create" id="main">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">新增大客户</h3>
                    </div>
                    <div class="panel-body">
                        <div class="form-group">
                            <label for="username">登陆账号：</label>
                            <input type="text" class="form-control" id="username" name="username">
                        </div>
                        <div class="form-group">
                            <label for="password">登陆密码：</label>
                            <input type="text" class="form-control" id="password" name="password">
                        </div>

                        <div class="form-group">
                            <label for="name">大客户名称：</label>
                            <input type="text" class="form-control" id="name" name="name">
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn btn-default" id="btn">入库</button>
            </form>
        </div>
    </div>
</div>


<#include "/_layout/script.ftl"/>
</body>
</html>