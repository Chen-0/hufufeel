<!DOCTYPE html>
<html>
<#include "*/_layout/head.ftl" />

<body class="hold-transition skin-black-light sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">
<#include "*/_layout/aside.ftl" />

    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                Blank page
                <small>it all starts here</small>
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
                <div class="box-header with-border">
                    <h3 class="box-title">Bordered Table</h3>
                </div>
                <!-- /.box-header -->
                <div class="box-body">

                    <form class="form-inline margin-bottom" role="form" method="get" action="/product/index">
                        <div class="form-group">
                            <label for="keyword">关键字：</label>
                            <input class="form-control" id="keyword" name="keyword" type="text" value="${keyword!}">
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">搜索</button>
                            <a href="/product/index" class="btn btn-default">重置</a>
                        </div>
                    </form>

                    <table class="table table-bordered table-striped">
                        <thead>
                        <tr>
                            <th width="1px"><input type="checkbox"></th>
                            <th>编号</th>
                            <th>商品名称</th>
                            <th>商品SKU</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list elements.getContent() as e>
                        <tr>
                            <th><input type="checkbox"></th>
                            <td>${e.id}</td>
                            <td>${e.productName}</td>
                            <td>${e.productSku}</td>
                            <td>
                                <a class="x-remove" href="/product/${e.id}/remove">删除</a>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
                <!-- /.box-body -->

                <#assign BASEURL="/product/index?keyword=${keyword}&status=${_STATUS}&page="/>
                <#include "*/_layout/v2.0/components/pages.ftl">
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
