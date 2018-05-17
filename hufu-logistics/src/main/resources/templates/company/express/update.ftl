<!DOCTYPE html>
<html lang="en">
<#assign title="更新用户信息"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">更新用户信息</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">

            <form role="form" method="post" action="/company/express/${express.id}/update">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                <div class="form-group">
                    <label>渠道名称：</label>
                    <input class="form-control" name="name" value="${express.name!}">
                </div>

                <div class="form-group">
                    <label>默认价格（元/磅）：</label>
                    <input class="form-control" name="price" value="${express.price!}">
                </div>

                <button type="submit" id="btn" class="btn btn-default">提交</button>
            </form>
        </div>
    </div>
</div>

<#include "/_layout/script.ftl"/>
</body>
</html>
