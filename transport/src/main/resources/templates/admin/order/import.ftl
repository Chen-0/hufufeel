<!DOCTYPE html>
<html lang="en">
<#assign title="出库单导入" />
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
        <div class="col-xs-8 col-xs-offset-2">
            <form action="/admin/order/import" method="post" enctype="multipart/form-data">
                <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>
                <div class="form-group">
                    <label for="express_no">文件上传</label>
                    <input name="file" type="file">

                    <#if ferror?exists && ferror["file"]?exists>
                        <p class="text-danger">${ferror["file"]}</p>
                    </#if>

                    <p class="help-block">
                        温情提醒：<br>
                        1.发货单号必须存在于系统中<br>
                        2.运单的状态必须为待审核（若不是待审核状态，处理时会跳过）<br>
                        3.快递公司可不填<br>
                        4.导入的订单将自动更新为待发货状态<br>
                        <strong>模板：</strong><a href="/static/hwc/admin_order_import_template.xlsx" download="出库单导入模板.xlsx">点击下载模板</a>
                    </p>
                </div>

                <div class="form-group">
                    <button class="btn btn-primary" type="submit">提交</button>
                </div>
            </form>
        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
</body>
</html>