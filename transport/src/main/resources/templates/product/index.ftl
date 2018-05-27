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

                    <form id="selectForm" action="/product/select" method="post">
                        <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>
                        <table class="table table-bordered table-striped table-condensed table-hover">
                            <thead>
                            <tr>
                                <th width="1px"><input type="checkbox" id="select_all"></th>
                                <th>编号</th>
                                <th>商品名称</th>
                                <th>商品SKU</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#list elements.getContent() as e>
                            <tr>
                                <th><input class="x-checkbox" type="checkbox" name="trackingNumber[]" value="${e.id}"></th>
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
                    </form>
                </div>
                <!-- /.box-body -->

                <div class="box-footer clearfix">
                    <button id="addToSend" class="btn btn-primary">添加到发货清单</button>
                    <#--<a href="/product/ready_to_send" class="btn btn-success">去发货</a>-->
                <#assign BASEURL="/product/index?keyword=${keyword}&status=${_STATUS}&page="/>
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
<script>
    $(function () {
        $('#select_all').click(function () {
            if($(this).is(":checked")) {
                changeSelect(true);
            } else {
                changeSelect(false);
            }
        });

        function changeSelect(value) {
            $('.x-checkbox').prop("checked", value);
        }


        $("#addToSend").click(function (e) {
            e.preventDefault();

            if($(".x-checkbox:checked").length === 0) {
                alert("请选择一件或多件商品");
                return;
            }

            $('#selectForm').submit();
        });
    })
</script>
</body>
</html>
