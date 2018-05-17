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

        <#switch flag>
            <#case 1>
                <form role="form" method="post" action="/order/excel/parse" enctype="multipart/form-data">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                    <div class="well">
                        <h4>2017-05-30 更新说明：</h4>
                        <p >虎芙导入模板已经更新，请大家及时下载最新模板，以免上传失败。</p>
                    </div>

                    <div class="form-group">
                        <label for="company">选择大客户：</label>
                        <select id="company" class="form-control" name="company_id">
                            <#list companies as company>
                                <option value="${company.id}">${company.name}</option>
                            </#list>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Excel文件：</label>
                        <input type="file" name="file">
                        <p class="help-block">虎芙导入模板：<a href="/static/doc/template.xlsx"download="虎芙导入模板_2017-05-30.xlsx">点击下载</a></p>
                    </div>


                    <button type="submit" id="btn" class="btn btn-default">提交</button>
                </form>
                <#break >
            <#case 2>
            <form role="form" method="post" action="/order/excel/lg" enctype="multipart/form-data">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                <div class="form-group">
                    <label>Excel文件：</label>
                    <input type="file" name="file">
                    <p class="help-block">虎芙导入模板：<a href="http://www.hufufeel.com/Logistics/Public/lg_template.xlsx"
                                                    download="虎芙导入模板.xlsx">点击下载</a></p>
                </div>


                <button type="submit" id="btn" class="btn btn-default">提交</button>
            </form>
                <#break >
        </#switch>
        </div>
    </div>
</div>

<#include "/_layout/script.ftl"/>
</body>
</html>
