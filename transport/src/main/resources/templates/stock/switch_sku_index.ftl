<!DOCTYPE html>
<html>
<#assign TITLE = "换标记录">

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

            <div class="box">
                <div class="box-body">
                    <table class="table table-bordered table-striped table-condensed table-hover">
                        <thead>
                        <tr>
                            <th>编号</th>
                            <th>原SKU</th>
                            <th>提交时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list elements as e>
                        <tr>
                            <td>${e_index + 1}</td>
                            <td>${e.sku}</td>
                            <td>${e.createdAt?string}</td>
                            <td>
                                <a href="/file/${e.doc.name}" download="换标文件.pdf">文件下载</a>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>

                <div class="box-footer clearfix">
                    <a href="/stock/switch_sku" class="btn btn-primary">提交换标信息</a>
                </div>
            </div>
        </section>
    </div>
<#include "*/_layout/footer.ftl"/>
</div>

<#include "*/_layout/script.ftl" />
</body>
</html>
