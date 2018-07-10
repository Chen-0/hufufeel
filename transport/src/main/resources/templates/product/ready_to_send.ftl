<!DOCTYPE html>
<html>
<#if TYPE == 0>
    <#assign TITLE="创建入库单">
<#else>
    <#assign TITLE="创建退货单">
</#if>
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

        <#if TYPE == 1>
            <div class="callout callout-warning">
                <h4>虎芙管家收货地址</h4>
                <ul>
                    <li>电话：951-314-8768 或 951-310-7615</li>
                    <li>街道：2260 S ARCHIBALD AVE STE D</li>
                    <li>州：CA</li>
                    <li>城市：ONTARIO</li>
                </ul>
            </div>
        </#if>


            <form id="mainForm" action="/package/create" method="post">
                <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>
                <input type="hidden" name="type" value="${TYPE}" id="type">
                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">1.选择货品</h3>
                    </div>

                    <div class="box-body">
                        <table class="table table-bordered table-striped table-condensed table-hover">
                            <thead>
                            <tr>
                                <th>编号</th>
                                <th>商品名称</th>
                                <th>商品SKU</th>
                                <th>数量（件）</th>
                                <th>大约重量（KG）</th>
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
                                        <input type="text" class="form-control" name="qty[]">
                                    </div>
                                </td>
                                <td>
                                    <span class="x-weight">0</span>
                                    <input type="hidden" name="x-input-weight" value="${e.weight}">
                                    <input type="hidden" name="weight[]">
                                    <input type="hidden" name="p[]" value="${e.id}">
                                <#--<div class="form-group">-->
                                <#--<input type="hidden" name="p[]" value="${e.id}">-->
                                <#--<input type="text" class="form-control" name="weight[]" placeholder="单件重量*数量">-->
                                <#--</div>-->
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
                        <a href="/product/index?status=1&type=${TYPE}" class="btn btn-primary">添加货品</a>
                        <a href="/product/package/remove_all?type=${TYPE}" class="btn btn-danger">移除全部</a>
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
                                <input type="radio" name="w" class="x-radio flat-red" value="${w.id}"
                                       <#if w_index==0>checked</#if>>
                            ${w.name}
                            </label>
                        </#list>
                        </div>
                    </div>
                </div>

                <div class="box">
                    <div class="box-header with-border">
                        <h3 class="box-title">3.填写入库单信息</h3>
                    </div>
                    <!-- /.box-header -->
                    <div class="box-body">
                    <#if TYPE == 1>
                        <div class="row">
                            <div class="col-xs-12 col-md-6">
                                <div class="form-group">
                                    <label for="contact">收件人<span class="text-danger"></span>：</label>

                                    <div class="row">
                                        <div class="col-xs-4">
                                            <input class="form-control" disabled value="${USER.hwcSn}">
                                        </div>
                                        <div class="col-xs-8">
                                            <input class="form-control" id="contact" name="contact">
                                        </div>
                                    </div>

                                </div>
                            </div>

                            <div class="col-md-6">
                                <label class="control-label">
                                    此收件人是包裹退货入库唯一识别标识，请注意在亚马逊移出库存中按照系统入库信息来填写。
                                </label>
                            </div>
                        </div>
                    </#if>

                        <div class="row">
                            <div class="col-xs-12 col-md-6">
                                <div class="form-group">
                                    <label for="referenceNumber">参考号<span class="text-danger"></span>：</label>
                                    <input class="form-control" id="referenceNumber" name="referenceNumber">
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-xs-12 col-md-6">
                                <div class="form-group">
                                    <label for="comment">备注：</label>
                                    <input class="form-control" id="comment" name="comment">
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
<script>
    $(function () {
        $('#mainForm').submit(function () {
            //check
            var weightInput = $('input[name="weight[]"]');
            var qtyInput = $('input[name="qty[]"]');

            if (weightInput.length === 0 || qtyInput.length === 0 || weightInput.length !== qtyInput.length) {
                sweetAlert("哎呦……", "请添加货品至入库清单", "error");
                return false;
            }

            for (var i = 0; i < weightInput.length; i++) {
                var witem = $(weightInput[i]).val();
                var qitem = $(qtyInput[i]).val();

                if (isEmpty(witem)) {
                    sweetAlert("哎呦……", "大约重量 不能为空", "error");
                    return false;
                }

                if (isEmpty(qitem)) {
                    sweetAlert("哎呦……", "数量 不能为空", "error");
                    return false;
                }

                var _w = parseFloat(witem);

                if (isNaN(_w) || _w.toString() !== witem) {
                    sweetAlert("哎呦……", "大约重量 请填写数字", "error");
                    return false;
                }

                var _q = parseInt(qitem);
                if (isNaN(_q) || _q.toString() !== qitem) {
                    sweetAlert("哎呦……", "数量 请填写整数", "error");
                    return false;
                }
            }

            var type = parseInt($('#type').val());

            if (type === 1) {
                var contact = $('#contact').val();

                if (isEmpty(contact)) {
                    $('#contact').parent().parent().addClass("has-error");
                    sweetAlert("哎呦……", "请填写 收件人 信息", "error");
                    return false;
                }
            }

            return true;
        });

        $('.u-remove').click(function (e) {
            e.preventDefault();

            var id = $(this).attr('data-id');
            var self = $(this);

            $.ajax({
                url: '/product/package/' + id + '/remove',
                data: {
                    type: ${TYPE}
                },
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

        var $qtyIpt = $("input[name='qty[]']");
        $qtyIpt.change(function () {
            var q = $(this).val();
            var wapper = $(this).parent().parent().parent();
            var w = $(wapper.find('input[name="x-input-weight"]')[0]).val();
            var t = accMul(parseFloat(q), parseFloat(w));
            if (isEmpty(t) || isNaN(t)) {
                t = 0;
            }
            $(wapper.find('.x-weight')[0]).html(t);
            $(wapper.find('input[name="weight[]"]')[0]).val(t);
        });

        $qtyIpt.each(function () {
            var q = $(this).val();
            var wapper = $(this).parent().parent().parent();
            var w = $(wapper.find('input[name="x-input-weight"]')[0]).val();
            var t = accMul(parseFloat(q), parseFloat(w));
            if (isEmpty(t) || isNaN(t)) {
                t = 0;
            }
            $(wapper.find('.x-weight')[0]).html(t);
            $(wapper.find('input[name="weight[]"]')[0]).val(t);
        })
    });
</script>
</body>
</html>
