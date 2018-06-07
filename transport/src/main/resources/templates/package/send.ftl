<!DOCTYPE html>
<html>
<#assign TITLE="发货清单">
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
            <form id="mainForm" action="/package/create" method="post">
                <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>
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
                                <th>仓库</th>
                                <th>库存数量</th>
                                <th>发货数量</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#list elements as e>
                            <tr>
                                <td>${e.id}</td>
                                <td>${e.product.productName}</td>
                                <td>${e.product.productSku}</td>
                                <td>${e.warehouse.name}</td>
                                <td>${e.quantity}</td>
                                <td>
                                    <div class="form-group">
                                        <input type="text" class="form-control" name="qty[]">
                                    </div>
                                </td>
                                <td>
                                    <a class="u-remove" href="javascript:void(0);" data-id="${e.id}">移除</a>
                                </td>
                            </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>

                    <div class="box-footer clearfix">
                        <a href="/stock/index" class="btn btn-primary">添加货品</a>
                        <a href="/stock/send/remove/all" class="btn btn-danger">移除全部</a>
                    </div>
                </div>

                <div class="box">
                    <h1>等待补充。。。。。</h1>
                </div>

                <div class="box">
                    <button id="submit" type="button" class="btn btn-primary">提交</button>
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
    $(function () {
        function isEmpty(A) {
            if (A === null || A === undefined || A === "") {
                return true;
            }
            return false;
        }

        $('.u-remove').click(function (e) {
            e.preventDefault();

            var id = $(this).attr('data-id');
            var self = $(this);

            $.ajax({
                url: '/stock/send/' + id + '/remove',
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
            radioClass: 'iradio_flat-green'
        });
    });
</script>
</body>
</html>
