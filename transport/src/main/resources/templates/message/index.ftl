<!DOCTYPE html>
<html>
<#assign TITLE="消息中心">
<#include "*/_layout/head.ftl" />

<body class="hold-transition skin-black-light sidebar-mini">
<div class="wrapper">
<#include "*/_layout/aside.ftl" />

    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                消息中心
            </h1>
            <ol class="breadcrumb">
                <li><a href="/user/center"><i class="fa fa-dashboard"></i> 个人中心</a></li>
                <li class="active"><a href="/message/index"><i class="fa fa-dashboard"></i> 消息中心</a></li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">

            <div class="box">
                <div class="box-body">
                    <a class="pull-right margin-bottom" href="/message/read/all">全部已读</a>
                    <table class="table table-bordered table-striped table-condensed table-hover">
                        <thead>
                        <tr>
                            <th>编号</th>
                            <th>消息内容</th>
                            <th>时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list elements.getContent() as e>
                        <tr>
                            <td>${e.id}</td>
                            <td>${e.context}</td>
                            <td>${e.createdAt?string}</td>
                            <td>
                                <a href="/m/r?id=${e.id}">查看详情</a>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <!-- /.box-body -->

                <div class="box-footer clearfix">
                <#assign BASEURL="/message/index?page="/>
                <#include "*/_layout/v2.0/components/pages.ftl">
                </div>
            </div>


        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

<#include "*/_layout/footer.ftl"/>
</div>

<#include "*/_layout/script.ftl" />
</body>
</html>
