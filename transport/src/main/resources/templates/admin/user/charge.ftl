<!DOCTYPE html>
<html lang="en">
<#assign title="修改余额"/>
<#include "*/admin/_layout/head.ftl" />

<body>
<#include "*/admin/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">${title} <small>${user.name}</small></h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">
            <form role="form" method="post" action="/admin/user/${user.id}/charge">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">${title}</h3>
                    </div>
                    <div class="panel-body">
                        <p class="margin-bottom">用户：${user.name} （NO.${user.hwcSn}）</p>
                        <p class="margin-bottom">余额：${user.usd}</p>
                        <@formGroup label="修改余额（正数充值，负数为减扣）" name="delta"></@formGroup>
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