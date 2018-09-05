<!DOCTYPE html>
<html lang="en">
<#assign title="换标更新"/>
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
            <form role="form" method="post" action="/admin/switch_sku/${ele.id}/update">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">${title}</h3>
                    </div>
                    <div class="panel-body">
                        <p class="margin-bottom">SKU：${ele.sku}</p>
                        <p class="margin-bottom">换标文件：<a href="/file/${ele.doc.name}" download="${ele.user.name}-NO.${ele.user.hwcSn}换标文件.pdf">文件下载</a></p>
                        <p class="margin-bottom">用户：${ele.user.name}</p>
                        <@formGroup label="件数" name="qty"></@formGroup>
                        <@formGroup label="装箱尺寸" name="size"></@formGroup>
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