<!DOCTYPE html>
<html>
<#assign TITLE="新建入库单">
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
            <form id="mainForm" action="/package/import" method="post" enctype="multipart/form-data">
                <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>

                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">1.选择仓库</h3>
                    </div>
                    <!-- /.box-header -->
                    <div class="box-body">
                        <!-- radio -->
                        <div class="form-group">
                        <#list warehouses as w>
                            <label class="select-label">
                                <input type="radio" name="wid" class="x-radio flat-red" value="${w.id}"
                                       <#if w_index==0>checked</#if>>
                            ${w.name}
                            </label>
                        </#list>
                        </div>
                    </div>
                </div>

                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">2.上传Excel文件</h3>
                    </div>
                    <!-- /.box-header -->
                    <div class="box-body">
                        <div class="row">
                            <div class="col-xs-12 col-md-4">
                                <div class="form-group">
                                    <label for="file">文件</label>
                                    <input type="file" class="form-control" id="file" name="file">
                                </div>
                            </div>
                        </div>

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
</body>
</html>
