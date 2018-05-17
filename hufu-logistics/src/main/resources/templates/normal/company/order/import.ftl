<!DOCTYPE html>
<html lang="en">
<#assign title="从文件导入数据"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">从文件导入数据</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">

            <form role="form" method="post" action="/order/excel/parse" enctype="multipart/form-data">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <input type="hidden" name="company_id" value="${company.id}">

                <div class="well">
                    <h4>2017-05-28 更新说明：</h4>
                    <p >虎芙导入模板已经更新，请大家及时下载最新模板，以免上传失败。</p>
                </div>



                <div class="form-group">
                    <label>Excel文件：</label>
                    <input type="file" name="file">
                    <p class="help-block">虎芙导入模板：<a href="/static/doc/template.xlsx"download="虎芙导入模板_2017-05-28.xlsx">虎芙导入模板_2017-05-28.xlsx</a></p>
                </div>


                <button type="submit" id="btn" class="btn btn-default">提交</button>
            </form>

        </div>
    </div>
</div>

<#include "/_layout/script.ftl"/>
</body>
</html>
