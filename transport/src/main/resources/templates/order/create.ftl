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

        <#if USER.arrearage == true >
            <div class="callout callout-danger">
                <h4>禁止操作！</h4>

                <p>您的账号已经被冻结，您的账号存在欠费行为：</p>
                <p>1、仓存费欠费 <a href="/user/statements/index">点击查看</a></p>
            </div>
        </#if>

            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">1.选择发货仓库</h3>
                </div>
                <div class="box-body">
                    <form id="mainForm" role="form">
                        <div class="form-group">
                        <#list warehouses as w>
                            <label class="select-label" for="wid_${w.id}">
                                <input id="wid_${w.id}" type="radio" name="wid" class="x-radio flat-red"
                                       value="${w.id}"
                                    <#if wid??>
                                       <#if w.id==wid>checked</#if>
                                    <#else>
                                       <#if w_index==0>checked</#if>
                                    </#if>
                                >
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
                <div class="box-body" style="padding-bottom: 25px;">
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

                <#if error?exists && error.product?exists>
                    <p class="margin text-danger"><strong>${error.product}</strong></p>
                </#if>
                </div>

                <div class="box-footer clearfix">
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
                            <button class="btn btn-default" type="button" data-toggle="modal"
                                    data-target="#show-stock-modal">
                                选择库存
                            </button>
                            <button class="btn btn-primary" type="button" id="addProduct">添加货品</button>
                        </div>
                    </form>
                </div>
            </div>

            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">3.基本服务</h3>
                </div>
                <div class="box-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="CKT-1" class="col-xs-1 control-label">派送方式*</label>
                            <div class="col-xs-5">
                                <select class="form-control" id="CKT-1" name="CKT-1">
                                <#list CKT_1 as c>
                                    <#if sp?exists && sp.ckt1 == c>
                                        <option value="${c}" selected>${c}</option>
                                    <#else>
                                        <option value="${c}">${c}</option>
                                    </#if>
                                </#list>
                                </select>
                            </div>

                            <label for="CKT-2" class="col-xs-1 control-label">保险类型*</label>
                            <div class="col-xs-5">
                                <select class="form-control" id="CKT-2" name="CKT-2">
                                <#list CKT_2 as c>
                                    <#if sp?exists && sp.ckt2 == c>
                                        <option value="${c}" selected>${c}</option>
                                    <#else>
                                        <option value="${c}">${c}</option>
                                    </#if>
                                </#list>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="CKT-3" class="col-xs-1 control-label">销售平台*</label>
                            <div class="col-xs-5">
                                <select class="form-control" id="CKT-3" name="CKT-3">
                                <#list CKT_3 as c>
                                    <#if sp?exists && sp.ckt3 == c>
                                        <option value="${c}" selected>${c}</option>
                                    <#else>
                                        <option value="${c}">${c}</option>
                                    </#if>
                                </#list>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="CKT-4" class="col-xs-1 control-label">平台单号</label>
                        <#if error?exists && error.ckt4?exists>
                            <div class="col-xs-5 has-error">
                                <input class="form-control" id="CKT-4" name="CKT-4" value="${sp.ckt4!}">
                                <span class="help-block">${error.ckt4!}</span>
                            </div>
                        <#else>
                            <div class="col-xs-5">
                                <input class="form-control" id="CKT-4" name="CKT-4" value="${sp.ckt4!}">
                            </div>
                        </#if>


                            <label for="CKT-5" class="col-xs-1 control-label">交易号</label>
                        <#if error?exists && error.ckt5?exists>
                            <div class="col-xs-5 has-error">
                                <input class="form-control" id="CKT-5" name="CKT-5" value="${sp.ckt5!}"
                                       placeholder="可空">
                                <span class="help-block">${error.ckt5!}</span>
                            </div>
                        <#else>
                            <div class="col-xs-5">
                                <input class="form-control" id="CKT-5" name="CKT-5" value="${sp.ckt5!}"
                                       placeholder="可空">

                            </div>
                        </#if>
                        </div>

                        <div class="form-group">
                            <label for="CKT-6" class="col-xs-1 control-label">备注</label>
                            <div class="col-xs-11">
                                <input class="form-control" placeholder="可空" id="CKT-6" name="CKT-6"
                                       value="${sp.ckt6!}">
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

                <#assign foo=true>
                <#if cType?exists && cType=="w">
                    <#assign foo=false>
                </#if>

                    <div class="form-group">
                        <p class="charge-font inline-block">选择填写信息方式：</p>
                        <label class="select-label">
                            <input type="radio" name="c_type" value="w" class="x-radio flat-red"
                                   data-id="x-tab-2" ${foo?string("", "checked")}> 填写
                        </label>
                        <label class="select-label">
                            <input type="radio" name="c_type" value="u" class="x-radio flat-red"
                                   data-id="x-tab-1" ${foo?string("checked", "")}> 上传
                            <input type="hidden" name="did" value="-1" id="did">
                        </label>
                    </div>

                    <div>
                        <ul class="nav nav-tabs" id="tabContainer" style="display: none; visibility: hidden;">
                            <li><a class="x-tab-1" href="#tab_1" data-toggle="tab" aria-expanded="true"></a></li>
                            <li><a class="x-tab-2" href="#tab_2" data-toggle="tab" aria-expanded="false"></a></li>
                        </ul>
                        <div class="tab-content">
                            <div class="tab-pane active" id="tab_1">
                            <#--文件上传-->
                            <#if error?exists && error.did?exists>
                                <form id="upload-file-form">
                                    <div class="form-group has-error">
                                        <label for="upload-file-input">请上传文件：</label>
                                        <input id="upload-file-input" type="file" name="uploadfile"
                                               accept="application/pdf"/>
                                        <p class="text-danger">${error.did!}</p>
                                        <p id="s-msg"></p>
                                    </div>
                                </form>
                            <#else>
                                <form id="upload-file-form">
                                    <div class="form-group">
                                        <label for="upload-file-input">请上传文件：</label>
                                        <input id="upload-file-input" type="file" name="uploadfile"
                                               accept="application/pdf"/>
                                        <p class="help-block">请上传PDF格式的文件</p>
                                        <p id="s-msg"></p>
                                    </div>
                                </form>
                            </#if>
                            </div>
                            <div class="tab-pane" id="tab_2">
                                <form class="form-horizontal" role="form">
                                    <div class="form-group">
                                        <label for="CKF-1" class="col-xs-1 control-label">国家*</label>
                                        <div class="col-xs-5">
                                            <select class="form-control" id="CKF-1" name="CKF-1">
                                            <#list CKF_1 as c>
                                                <#if sp?exists && sp.ck1?exists && sp.ckf1 == c>
                                                    <option value="${c}" selected>${c}</option>
                                                <#else>
                                                    <option value="${c}">${c}</option>
                                                </#if>

                                            </#list>
                                            </select>
                                        </div>

                                        <label for="CKF-2" class="col-xs-1 control-label ">姓名*</label>
                                    <#if error?exists && error.ckf2?exists>
                                        <div class="col-xs-5 has-error">
                                            <input class="form-control" id="CKF-2" name="CKF-2" value="${sp.ckf2!}">
                                            <span class="help-block">${error.ckf2!}</span>
                                        </div>
                                    <#else>
                                        <div class="col-xs-5">
                                            <input class="form-control" id="CKF-2" name="CKF-2" value="${sp.ckf2!}">
                                        </div>
                                    </#if>
                                    </div>

                                    <div class="form-group">
                                        <label for="CKF-10" class="col-xs-1 control-label ">街道*</label>
                                    <#if error?exists && error.ckf10?exists>
                                        <div class="col-xs-5 has-error">
                                            <input class="form-control" id="CKF-10" name="CKF-10" value="${sp.ckf10!}">
                                            <span class="help-block">${error.ckf10!}</span>
                                        </div>
                                    <#else>
                                        <div class="col-xs-5">
                                            <input class="form-control" id="CKF-10" name="CKF-10" value="${sp.ckf10!}">
                                        </div>
                                    </#if>

                                        <label for="CKF-5" class="col-xs-1 control-label ">城市*</label>
                                    <#if error?exists && error.ckf5?exists>
                                        <div class="col-xs-5 has-error">
                                            <input class="form-control" id="CKF-5" name="CKF-5" value="${sp.ckf5!}">
                                            <span class="help-block">${error.ckf5!}</span>
                                        </div>
                                    <#else>
                                        <div class="col-xs-5">
                                            <input class="form-control" id="CKF-5" name="CKF-5" value="${sp.ckf5!}">
                                        </div>
                                    </#if>
                                    </div>

                                    <div class="form-group">
                                        <label for="CKF-3" class="col-xs-1 control-label ">省份*</label>
                                    <#if error?exists && error.ckf3?exists>
                                        <div class="col-xs-5 has-error">
                                            <input class="form-control" id="CKF-3" name="CKF-3" value="${sp.ckf3!}">
                                            <span class="help-block">${error.ckf3!}</span>
                                        </div>
                                    <#else>
                                        <div class="col-xs-5">
                                            <input class="form-control" id="CKF-3" name="CKF-3" value="${sp.ckf3!}">
                                        </div>
                                    </#if>

                                        <label for="CKF-7" class="col-xs-1 control-label ">邮编*</label>
                                    <#if error?exists && error.ckf7?exists>
                                        <div class="col-xs-5 has-error">
                                            <input class="form-control" id="CKF-7" name="CKF-7" value="${sp.ckf7!}">
                                            <span class="help-block">${error.ckf7!}</span>
                                        </div>
                                    <#else>
                                        <div class="col-xs-5">
                                            <input class="form-control" id="CKF-7" name="CKF-7" value="${sp.ckf7!}">
                                        </div>
                                    </#if>
                                    </div>

                                    <div class="form-group">
                                        <label for="CKF-6" class="col-xs-1 control-label ">Email</label>
                                        <div class="col-xs-5">
                                            <input class="form-control" id="CKF-6" name="CKF-6" value="${sp.ckf6!}">
                                        </div>

                                        <label for="CKF-11" class="col-xs-1 control-label ">门牌号</label>
                                        <div class="col-xs-5">
                                            <input class="form-control" id="CKF-11" name="CKF-11" value="${sp.ckf11!}">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="CKF-4" class="col-xs-1 control-label ">电话</label>
                                    <#if error?exists && error.ckf4?exists>
                                        <div class="col-xs-5 has-error">
                                            <input class="form-control" id="CKF-4" name="CKF-4" value="${sp.ckf4!}">
                                            <span class="help-block">${error.ckf4!}</span>
                                        </div>
                                    <#else>
                                        <div class="col-xs-5">
                                            <input class="form-control" id="CKF-4" name="CKF-4" value="${sp.ckf4!}">
                                        </div>
                                    </#if>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            <#if USER.arrearage != true >
                <div class="box-footer clearfix">
                    <button id="submit" class="btn btn-primary btn-lg" type="button">提交</button>
                </div>
            </#if>
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
                        <th>操作</th>
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
        var $msg = $('#s-msg');
        $msg.hide();


        $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
            checkboxClass: 'icheckbox_flat-green',
            radioClass: 'iradio_flat-green'
        });

        var $stockContent = $('#stock-content');

        $('input[name=wid]').on('ifChecked', function (event) { //ifCreated 事件应该在插件初始化之前绑定
            showProduct($(this));
        });

        showProduct($('input[name=wid]:checked'));

        function showProduct($wInput) {
            var id = $wInput.val();

            $.ajax({
                url: '/ajax/stock/get_available',
                data: {
                    wid: id
                },
                success: function (data) {
                    var items = data.data;

                    var options = '<a href="javascript:void(0);">选择</a>';

                    $stockContent.empty();
                    for (var i = 0; i < items.length; i++) {
                        var box = "<tr>\n" +
                                "                        <td>" + items[i].id + "</td>\n" +
                                "                        <td>" + items[i].productName + "</td>\n" +
                                "                        <td>" + items[i].productSku + "</td>\n" +
                                "                        <td>" + items[i].quantity + "</td>\n" +
                                "                        <td>" + options + "</td>\n" +
                                "                    </tr>";
                        var $box = $(box);
                        $($box.find('a')[0]).click(function () {
                            var container = $(this).parent().parent();
                            var val = $($(container).find('td')[2]).html();
                            $('#productSKU').val(val);
                            $('#show-stock-modal').modal('hide');
                        });
                        $stockContent.append($box);
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

            addProduct(productSKU, qty);
        });

        function addProduct(sku, qty) {
            var wiid = $('input[name=wid]:checked').val();

            $.ajax({
                url: '/ajax/stock/get_stock',
                data: {
                    productSku: sku,
                    qty: qty,
                    wid: wiid
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

                        $('#productSKU').val('');
                        $('#qty').val('');
                    }
                }
            });
        }


        ////////////////////
    <#if products??>
        <#list products as p>
            addProduct("${p.productSku}", ${qty[p_index]})
        </#list>
    </#if>

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

            var cType = $('input[name=c_type]:checked').val();
            data['c_type'] = cType;
            if (cType === 'u') {
                data.did = getValue('#did');
            } else if (cType === 'w') {
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
                data.ckf11 = getValue('#CKF-11');
                data.ckf12 = getValue('#CKF-12');
            }



        <#if _csrf??>
            data.${_csrf.parameterName} = "${_csrf.token}";
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
                result.push($(ele[i]).val());
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

        function changeTab($activeEle) {
            var target = $activeEle.attr('data-id');

            $('#tabContainer .' + target).tab('show');
        }

        $('input[name=c_type]').on('ifChecked', function () {
            changeTab($(this));
        });

        changeTab($('input[name=c_type]:checked'));

        $("#upload-file-input").on("change", uploadFile);

        function uploadFile() {
            var data = new FormData($("#upload-file-form")[0]);

            console.log(data);
            $.ajax({
                url: "/order/pdf/upload",
                type: "POST",
                data: data,
                enctype: 'multipart/form-data',
                processData: false,
                contentType: false,
                cache: false,
                success: function (resp) {
                    console.log(resp);

                    if (resp.success) {
                        $('#did').val(resp.data.id);

                        $msg.html('文件已经上传成功！');
                        $msg.fadeIn();

                    }
                },
                error: function (resp) {
                    console.log(resp);
                }
            });
        }

        $('#CKF-1').change(function () {
            setCon($(this));
        });

        setCon($('#CKF-1'));

        function setCon(ele) {
            var v = $(ele).val();
            console.log(v);

            if (v === '其它') {

                $(ele).parent().attr("class", "col-xs-2");

                var box = '<div class="col-xs-3"><input class="form-control" id="CKF-12" name="CKF-12" value="${sp.ckf12!}"></div>';

                $(ele).parent().after(box);
            } else {
                $('#CKF-12').parent().remove();
                $(ele).parent().attr("class", "col-xs-5");
            }
        }
    });
</script>
</body>
</html>
