<!DOCTYPE html>
<html lang="en">
<#assign title="添加公告" />
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

    <div class="row" style="padding-bottom: 50px;">
        <div class="col-xs-8 col-xs-offset-2">

            <form class="xn-form" action="/admin/notice/create" method="post">
                <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>
                <@formGroup label="标题" name="title"></@formGroup>

                    <textarea name="context" class="textarea" placeholder="Place some text here"
                              style="width: 100%; height: 200px; font-size: 14px; line-height: 18px; border: 1px solid #dddddd; padding: 10px;"></textarea>

                <div class="form-group">
                    <button class="btn btn-primary" type="submit">提交</button>
                </div>
            </form>
        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
<script src="/static/common/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js"></script>
<script>
    $(function () {
        $('.textarea').wysihtml5()
    });
</script>
</body>
</html>