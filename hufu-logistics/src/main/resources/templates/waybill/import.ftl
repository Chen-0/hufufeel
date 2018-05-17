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

            <form role="form" method="post" action="/waybill/import/lg" enctype="multipart/form-data">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                <div class="form-group">
                    <label>Excel文件：</label>
                    <input type="file" name="file">
                    <p class="help-block">虎芙导入模板：<a href="http://www.hufufeel.com/Logistics/Public/lg_template.xlsx"
                                                    download="海淘客物流信息导入模板.xlsx">点击下载</a></p>
                </div>


                <button type="submit" id="btn" class="btn btn-default">提交</button>
            </form>
        </div>
    </div>
</div>

<#include "/_layout/script.ftl"/>
</body>
</html>
