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
                    <h3 class="box-title">1.选择货品</h3>
                </div>
                <!-- /.box-header -->
                <div class="box-body">
                    <table class="table table-bordered table-striped table-condensed table-hover">
                        <thead>
                        <tr>
                            <th>编号</th>
                            <th>商品名称</th>
                            <th>商品SKU</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list elements as e>
                        <tr>
                            <td>${e.id}</td>
                            <td>${e.productName}</td>
                            <td>${e.productSku}</td>
                            <td>
                                <a class="u-remove" href="javascript:void(0);" data-id="${e.id}">移除</a>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>

                <div class="box-footer clearfix">
                    <a href="/product/index" class="btn btn-primary">添加货品</a>
                </div>
            </div>

            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">2.选择仓库</h3>
                </div>
                <!-- /.box-header -->
                <div class="box-body">
                    <!-- radio -->
                    <div class="form-group">
                        <#list warehouses as w>
                            <label class="select-label">
                                <input type="radio" name="w" class="flat-red" value="${w.id}" <#if w_index==0>checked</#if>>
                                ${w.name}
                            </label>
                        </#list>
                    </div>
                </div>
            </div>

            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">3.选择服务</h3>
                </div>
                <!-- /.box-header -->
                <div class="box-body">
                    <!-- radio -->
                    <div class="form-group">
                    <#list warehouses as w>
                        <label class="select-label">
                            <input type="radio" name="w" class="flat-red" value="${w.id}" >
                        ${w.name}
                        </label>
                    </#list>
                    </div>
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
        $('.u-remove').click(function (e) {
            e.preventDefault();

            var id = $(this).attr('data-id');
            var self = $(this);

            $.ajax({
                url: '/product/package/' + id + '/remove',
                success: function (data) {
                    console.log(data);

                    if (data.success) {
                        self.parent().parent().remove();
                    }
                }
            });
        });

        $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
            checkboxClass: 'icheckbox_flat-green',
            radioClass   : 'iradio_flat-green'
        })
    })
</script>
</body>
</html>
