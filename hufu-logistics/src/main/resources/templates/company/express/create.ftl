<!DOCTYPE html>
<html lang="en">
<#assign title="添加大客户发货渠道"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">添加大客户发货渠道</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">
            <form role="form" method="post" action="/company/express/create" id="main">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">添加大客户发货渠道</h3>
                    </div>
                    <div class="panel-body">
                        <div class="form-group">
                            <label for="name">渠道名称（必填）：</label>
                            <input type="text" class="form-control" id="name" name="name">
                        </div>
                        <div class="form-group">
                            <label for="price">默认价格（元/磅）（必填）：</label>
                            <input type="text" class="form-control" id="price" name="price">
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