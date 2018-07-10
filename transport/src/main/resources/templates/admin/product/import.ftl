<!DOCTYPE html>
<html lang="en">
<#assign title="货品导入" />
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
            <form action="/admin/product/import" method="post" enctype="multipart/form-data">
                <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>
                <div class="form-group">
                    <label for="express_no">文件上传</label>
                    <input name="file" type="file">

                    <#if ferror?exists && ferror["file"]?exists>
                        <p class="text-danger">${ferror["file"]}</p>
                    </#if>

                    <p class="help-block">
                        温情提醒：<br>
                        1.客户必须存在于系统中<br>
                        2.系统不会自动把客户编号拼接在SKU上，请手动拼接。（简单来说，SKU字段您输入什么，系统会保存什么）<br>
                        <strong>模板：</strong><a href="/static/hwc/admin_sku_import_template.xlsx" download="货品导入模板.xlsx">点击下载模板</a>
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