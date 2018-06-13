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
                            <label class="select-label" for="wid_${w.id}">
                                <input id="wid_${w.id}" type="radio" name="wid" class="x-radio flat-red" value="${w.id}"
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

                    <form role="form" class="form-inline margin-bottom">
                        <div class="form-group">
                            <label for="productSKU">货品SKU</label>
                            <input type="text" class="form-control" id="productSKU">
                        </div>
                        <div class="form-group">
                            <label for="qty">发货数量</label>
                            <input type="text" class="form-control" id="qty">
                        </div>
                        <div class="form-group">
                            <button class="btn btn-primary" type="button" id="addProduct">添加货品</button>
                            <button class="btn btn-default" type="button" data-toggle="modal"
                                    data-target="#show-stock-modal">
                                查看货品
                            </button>
                        </div>
                    </form>
                    <table class="table table-bordered table-striped table-condensed table-hover">
                        <thead>
                        <tr>
                            <th>编号</th>
                            <th>商品名称</th>
                            <th>商品SKU</th>
                            <th>库存数量</th>
                            <th>发货数量</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="send-content">

                        </tbody>
                    </table>
                </div>
            </div>

            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">3.基本服务</h3>
                </div>
                <div class="box-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="CKT-1" class="col-xs-1 control-label text-center">派送方式*</label>
                            <div class="col-xs-5">
                                <select class="form-control" id="CKT-1" name="CKT-1">
                                    <option value="1">测试1</option>
                                    <option value="2">测试2</option>
                                </select>
                            </div>

                            <label for="CKT-2" class="col-xs-1 control-label text-center">保险类型*</label>
                            <div class="col-xs-5">
                                <select class="form-control" id="CKT-2" name="CKT-2">
                                    <option value="HEIGHT">高保</option>
                                    <option value="LOW">低保</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="CKT-3" class="col-xs-1 control-label text-center" style="text-align: center;">销售平台*</label>
                            <div class="col-xs-5">
                                <select class="form-control" id="CKT-3" name="CKT-3">
                                    <option value="1">我母鸡啊1</option>
                                    <option value="2">我母鸡啊2</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="CKT-4" class="col-xs-1 control-label text-center" style="text-align: center;">参考号*</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="CKT-4" name="CKT-4">
                            </div>

                            <label for="CKT-5" class="col-xs-1 control-label text-center" style="text-align: center;">交易号*</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="CKT-5" name="CKT-5">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="CKT-6" class="col-xs-1 control-label text-center"
                                   style="text-align: center;">备注</label>
                            <div class="col-xs-11">
                                <textarea class="form-control" placeholder="（可不填）" id="CKT-6" name="CKT-6"></textarea>
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
                            <label for="CKF-1" class="col-xs-1 control-label text-center" style="text-align: center;">国家*</label>
                            <div class="col-xs-5">
                                <select class="form-control" id="CKF-1" name="CKF-1">
                                    <option value="1">我母鸡啊1</option>
                                    <option value="2">我母鸡啊2</option>
                                </select>
                            </div>

                            <label for="CKF-2" class="col-xs-1 control-label text-center">姓名*</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="CKF-2" name="CKF-2">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="CKF-3" class="col-xs-1 control-label text-center">州/省*</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="CKF-3" name="CKF-3">
                            </div>

                            <label for="CKF-4" class="col-xs-1 control-label text-center">电话*</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="CKF-4" name="CKF-4">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="CKF-5" class="col-xs-1 control-label text-center">城市</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="CKF-5" name="CKF-5">
                            </div>

                            <label for="CKF-6" class="col-xs-1 control-label text-center">Email</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="CKF-6" name="CKF-6">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="CKF-7" class="col-xs-1 control-label text-center">邮编</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="CKF-7" name="CKF-7">
                            </div>

                            <label for="CKF-8" class="col-xs-1 control-label text-center">身份证</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="CKF-8" name="CKF-8">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="CKF-9" class="col-xs-1 control-label text-center">公司</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="CKF-9" name="CKF-9">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="CKF-10" class="col-xs-1 control-label text-center">街道</label>
                            <div class="col-xs-5">
                                <textarea class="form-control" id="CKF-10" name="CKF-10"></textarea>
                            </div>

                            <label for="CKF-11" class="col-xs-1 control-label text-center">门牌号</label>
                            <div class="col-xs-5">
                                <input class="form-control" id="CKF-11" name="CKF-11">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="box-footer clearfix">
                    <button id="submit" class="btn btn-primary btn-lg" type="button">提交</button>
                </div>
            </div>
        </section>
    </div>

<#include "*/_layout/footer.ftl"/>
</div>

<div class="modal fade" id="show-stock-modal">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">查看货品</h4>
            </div>
            <div class="modal-body">
                <table class="table table-bordered table-striped table-condensed table-hover">
                    <thead>
                    <tr>
                        <th>编号</th>
                        <th>商品名称</th>
                        <th>商品SKU</th>
                        <th>库存数量</th>
                    </tr>
                    </thead>
                    <tbody id="stock-content">
                    <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<#include "*/_layout/script.ftl" />
<script>
    $(function () {
        $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
            checkboxClass: 'icheckbox_flat-green',
            radioClass: 'iradio_flat-green'
        });

        var $stockContent = $('#stock-content');

        $('input[name=wid]').on('ifChecked', function (event) { //ifCreated 事件应该在插件初始化之前绑定
            showProduct($(this));
        });

        showProduct($('input[name=wid]'));

        function showProduct($wInput) {
            var id = $wInput.val();

            $.ajax({
                url: '/ajax/stock/get_available',
                data: {
                    wid: id
                },
                success: function (data) {
                    var items = data.data;

                    $stockContent.empty();
                    for (var i = 0; i < items.length; i++) {
                        $stockContent.append("<tr>\n" +
                                "                        <td>" + items[i].id + "</td>\n" +
                                "                        <td>" + items[i].productName + "</td>\n" +
                                "                        <td>" + items[i].productSku + "</td>\n" +
                                "                        <td>" + items[i].quantity + "</td>\n" +
                                "                    </tr>");
                    }
                }
            });
        }

        $('#addProduct').click(function (e) {
            e.preventDefault();

            var productSKU = $('#productSKU').val();
            var qty = $('#qty').val();

            if (isEmpty(productSKU)) {
                sweetAlert("哎呦……", "请填写货品SKU", "error");
                return;
            }

            if (isEmpty(qty)) {
                sweetAlert("哎呦……", "请填写发货数量", "error");
                return;
            }

            $.ajax({
                url: '/ajax/stock/get_stock',
                data: {
                    productSku: productSKU,
                    qty: qty
                },
                success: function (data) {
                    if (isEmpty(data.data)) {
                        sweetAlert("哎呦……", data.message, "error");
                    } else {
                        var item = data.data;
                        var flag = true;
                        $('input[name="S-PW-id[]"]').each(function (t) {
                            console.log($(this).val());
                            if (parseInt($(this).val()) === item.productId) {
                                flag = false;
                            }
                        });

                        if (!flag) {
                            //货品已经存在
                            return;
                        }


                        var input = '<input type="hidden" name="S-PW-id[]" value="' + item.productId + '">';
                        input = input + '<input type="hidden" name="S-PW-qty[]" value="' + qty + '">';

                        var options = '<a href="javascript:void(0)" class="PW-remove">移除</a>';

                        var box = "" +
                                "<tr>\n" +
                                "                            <td>" + item.id + input + "</td>\n" +
                                "                            <td>" + item.productName + "</td>\n" +
                                "                            <td>" + item.productSku + "</td>\n" +
                                "                            <td>" + item.quantity + "</td>\n" +
                                "                            <td>" + qty + "</td>\n" +
                                "                            <td>" + options + "</td>\n" +
                                "                        </tr>";

                        var $box = $(box);
                        $($box.find('.PW-remove')[0]).click(function (e) {
                            e.preventDefault();

                            $(this).parent().parent().remove();
                        });
                        $('#send-content').append($box);
                    }
                }
            });
        });

//      ----------------- SUBMIT -----------------------------
        $('#submit').click(function (e) {
            e.preventDefault();

            var data = {};

            data.wid = getValue($('input[name=wid]:checked'));
            data.pids = getArray($('input[name="S-PW-id[]"]'));
            data.qty = getArray($('input[name="S-PW-qty[]"]'));
            data.ckt1 = getValue('#CKT-1');
            data.ckt2 = getValue('#CKT-2');
            data.ckt3 = getValue('#CKT-3');
            data.ckt4 = getValue('#CKT-4');
            data.ckt5 = getValue('#CKT-5');
            data.ckt6 = getValue('#CKT-6');

            data.ckf1 = getValue('#CKF-1');
            data.ckf2 = getValue('#CKF-2');
            data.ckf3 = getValue('#CKF-3');
            data.ckf4 = getValue('#CKF-4');
            data.ckf5 = getValue('#CKF-5');
            data.ckf6 = getValue('#CKF-6');
            data.ckf7 = getValue('#CKF-7');
            data.ckf8 = getValue('#CKF-8');
            data.ckf9 = getValue('#CKF-9');
            data.ckf10 = getValue('#CKF-10');
            data.ckf10 = getValue('#CKF-11');

            <#if _csrf??>
                data.${_csrf.parameterName}="${_csrf.token}";
            </#if>

            console.log(data);

//            $.ajax({
//                url: '/order/create',
//                data: data,
//                success: function (resp) {
//                    console.log(resp);
//                }
//            })

            post('/order/create', data);
        });

        function getValue(e) {
            return $(e).val();
        }

        function getArray(ele) {
            var result = [];
            for (var i = 0; i < ele.length; i++) {
                result.push(ele.val());
            }
            return result;
        }


        function post(url, args) {
            var body = $(document.body),
                    form = $("<form method='post'></form>"),
                    input;
            form.attr({"action": url});
            $.each(args, function (key, value) {
                if (value instanceof Array) {
                    input = $("<input type='hidden'>");
                    input.attr({"name": key + '[]'});
                    input.val(value);
                    form.append(input);
                } else {
                    input = $("<input type='hidden'>");
                    input.attr({"name": key});
                    input.val(value);
                    form.append(input);
                }
            });

            form.appendTo(document.body);
            form.submit();
            document.body.removeChild(form[0]);
        }
    });
</script>
</body>
</html>
