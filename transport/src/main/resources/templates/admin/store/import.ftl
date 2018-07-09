<!DOCTYPE html>
<html lang="en">
<#assign title="库存导入" />
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
            <form action="/admin/stock/import" method="post" enctype="multipart/form-data">
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
                        2.SKU 必须属于该客户<br>
                        3.模板中的第一列【客户编号】的单元格格式设置为 文本类型，不然0开头的数字，0会被去掉。<br>
                        4.现在只有一个仓库，所以仓库名称请填写【LA美西仓】<br>
                        <strong>模板：</strong><a href="/static/hwc/admin_stock_import_template.xlsx" download="库存导入模板.xlsx">点击下载模板</a>
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