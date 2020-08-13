<!DOCTYPE html>
<html>
<#assign TITLE="导入货品">
<#include "*/_layout/head.ftl" />

<body class="hold-transition skin-black-light sidebar-mini">
<div class="wrapper">
<#include "*/_layout/aside.ftl" />

    <div class="content-wrapper">
        <section class="content-header">
            <h1>
            ${TITLE}
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="#">Examples</a></li>
                <li class="active">Blank page</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">
            <form id="mainForm" action="/product/import" method="post" enctype="multipart/form-data">
                <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>

                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">上传Excel文件</h3>
                    </div>
                    <!-- /.box-header -->
                    <div class="box-body">
                        <div class="row">
                            <div class="col-xs-12 col-md-4">
                                <div class="form-group">
                                    <label for="file">文件</label>
                                    <input type="file" id="file" name="file">
                                    <#if warn ??>
                                        <p class="text-danger margin">${warn!}</p>
                                    <#else>
                                        <p class="margin">导入货品模板：<a href="/static/hwc/product_import_template.xlsx" download="货品导入模板.xlsx">点击下载</a></p>
                                    </#if>

                                </div>
                            </div>
                        </div>

                        <#if ferror?exists && ferror["file"]?exists>
                        <div class="row">
                            <div class="col-xs-12 col-md-4">
                            <p class="text-danger">${ferror["file"]}</p>
                            </div>
                        </div>
                        </#if>

                        <button id="submit" type="submit" class="btn btn-primary">提交</button>
                    </div>
                </div>
            </form>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

<#include "*/_layout/footer.ftl"/>
</div>

<#include "*/_layout/script.ftl" />
<script>
    $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
        checkboxClass: 'icheckbox_flat-green',
        radioClass: 'iradio_flat-green'
    });
</script>
</body>
</html>
