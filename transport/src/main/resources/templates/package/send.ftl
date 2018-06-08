<!DOCTYPE html>
<html>
<#assign TITLE="新建发货单">
<#include "*/_layout/head.ftl" />

<body class="hold-transition skin-black-light sidebar-mini">
<div class="wrapper">
<#include "*/_layout/aside.ftl" />

    <div class="content-wrapper">
        <section class="content-header">
            <h1>新建发货单</h1>

            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="#">Examples</a></li>
                <li class="active">Blank page</li>
            </ol>
        </section>

        <section class="content">
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">1.选择发货仓库</h3>
                </div>
                <div class="box-body">
                    <form id="mainForm" role="form">
                        <div class="form-group">
                        <#list warehouses as w>
                            <label class="select-label">
                                <input type="radio" name="w" class="x-radio flat-red" value="${w.id}"
                                       <#if w_index==0>checked</#if>>
                            ${w.name}
                            </label>
                        </#list>
                        </div>
                    </form>
                </div>
            </div>


            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">2.选择货品</h3>
                </div>
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
                        </tbody>
                    </table>
                </div>
                <div class="box-footer clearfix">
                    <button class="btn btn-primary" type="button">添加货品</button>
                </div>
            </div>

            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">3.基本服务</h3>
                </div>
                <div class="box-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label class="col-xs-1 control-label text-center">派送方式*</label>
                            <div class="col-xs-5">
                                <select class="form-control" id="channels" name="dc">
                                    <option value="1">测试1</option>
                                    <option value="2">测试2</option>
                                </select>
                            </div>

                            <label class="col-xs-1 control-label text-center">保险类型*</label>
                            <div class="col-xs-5">
                                <select class="form-control" id="channels" name="dc">
                                    <option value="HEIGHT">高保</option>
                                    <option value="LOW">低保</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-1 control-label text-center" style="text-align: center;">销售平台*</label>
                            <div class="col-xs-5">
                                <select class="form-control" id="channels" name="dc">
                                    <option value="1">我母鸡啊1</option>
                                    <option value="2">我母鸡啊2</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-1 control-label text-center" style="text-align: center;">参考号*</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="channels" name="dc">
                            </div>

                            <label class="col-xs-1 control-label text-center" style="text-align: center;">交易号*</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="channels" name="dc">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-1 control-label text-center" style="text-align: center;">备注</label>
                            <div class="col-xs-11">
                                <textarea class="form-control" placeholder="（可不填）"></textarea>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">4.收货人信息</h3>
                </div>
                <div class="box-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label class="col-xs-1 control-label text-center" style="text-align: center;">国家*</label>
                            <div class="col-xs-5">
                                <select class="form-control" id="channels" name="dc">
                                    <option value="1">我母鸡啊1</option>
                                    <option value="2">我母鸡啊2</option>
                                </select>
                            </div>

                            <label class="col-xs-1 control-label text-center">姓名*</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="channels" name="dc">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-1 control-label text-center">州/省*</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="channels" name="dc">
                            </div>

                            <label class="col-xs-1 control-label text-center">电话*</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="channels" name="dc">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-1 control-label text-center">城市</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="channels" name="dc">
                            </div>

                            <label class="col-xs-1 control-label text-center">Email</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="channels" name="dc">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-1 control-label text-center">邮编</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="channels" name="dc">
                            </div>

                            <label class="col-xs-1 control-label text-center">身份证</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="channels" name="dc">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-1 control-label text-center">公司</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="channels" name="dc">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-xs-1 control-label text-center">街道</label>
                            <div class="col-xs-5">
                                <textarea class="form-control"></textarea>
                            </div>

                            <label class="col-xs-1 control-label text-center">门牌号</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="channels" name="dc">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="box-footer clearfix">
                    <button class="btn btn-primary btn-lg" type="button">提交</button>
                </div>
            </div>
        </section>
    </div>

<#include "*/_layout/footer.ftl"/>
</div>

<#include "*/_layout/script.ftl" />
<script>
    $(function () {
        $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
            checkboxClass: 'icheckbox_flat-green',
            radioClass: 'iradio_flat-green'
        });
    });
</script>
</body>
</html>
