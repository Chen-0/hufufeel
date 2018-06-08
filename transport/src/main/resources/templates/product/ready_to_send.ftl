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
            <form action="/package/create" method="post">
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
                            <th>大约重量（KG）</th>
                            <th>数量（件）</th>
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
                                <div class="form-group">
                                    <input type="hidden" name="p[]" value="${e.id}">
                                    <input type="text" class="form-control" name="weight[]" placeholder="单件重量*数量">
                                </div>
                            </td>
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
                    <a href="/product/index" class="btn btn-primary">添加货品</a>
                    <a href="/product/package/remove_all" class="btn btn-danger">移除全部</a>
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
                                <input type="radio" name="w" class="x-radio flat-red" value="${w.id}" <#if w_index==0>checked</#if>>
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
                    <div class="row">
                        <div class="col-xs-4">
                            <div class="form-group">
                                <label>选择派送方式：</label>
                                <select class="form-control" id="channels" name="dc">
                                    <option value="">1</option>
                                    <option>2</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-primary">提交</button>
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
        });


        var channels = $('#channels');
        $('input[type="radio"].flat-red').on('ifChecked', function(event){ //ifCreated 事件应该在插件初始化之前绑定
            console.log($(this).prop('checked'));
            console.log($(this).val());

            var id = $(this).val();

            $.ajax({
                url: '/channel/select',
                data: {
                    id: id
                },
                success: function (data) {
                    var values = data.data;

                    channels.empty();

                    for(var i = 0; i < values.length; i ++) {
                        var item = values[i];
                        channels.append("<option value='"+item.id+"'>"+item.name+"</option>");
                    }
                }
            });
        });

        (function () {
            var radio = $('input[type="radio"]:checked');
            var id = radio.val();

            $.ajax({
                url: '/channel/select',
                data: {
                    id: id
                },
                success: function (data) {
                    var values = data.data;

                    channels.empty();

                    for(var i = 0; i < values.length; i ++) {
                        var item = values[i];
                        channels.append("<option value='"+item.id+"'>"+item.name+"</option>");
                    }
                }
            })
        })();


    })
</script>
</body>
</html>
