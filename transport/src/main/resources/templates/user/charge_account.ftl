<!DOCTYPE html>
<html>
<#assign TITLE="充值中心">
<#include "*/_layout/head.ftl" />

<body class="hold-transition skin-black-light sidebar-mini">
<div class="wrapper">
<#include "*/_layout/aside.ftl" />

    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                充值中心
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="#">Examples</a></li>
                <li class="active">Blank page</li>
            </ol>
        </section>

        <section class="content">
            <div class="box">
                <div class="box-body charge-padding">

                    <form id="main" class="form-horizontal" role="form" method="post" action="/user/charge_account">
                        <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>

                        <div class="form-group">
                            <p class="charge-font">账户余额：<strong>${USER.usd} 美元（USD）</strong></p>
                        </div>

                        <div class="form-group">
                            <p class="charge-font">当前汇率：1 美元 = ${U2R} 人民币</p>
                        </div>

                        <div class="form-group">
                            <div class="form-group">
                                <label class="col-xs-2 control-label charge-font" for="total">充值金额(USD)：</label>
                                <div class="col-xs-4">
                                    <input class="form-control" id="total" type="text" placeholder="5">
                                <#if nu?exists && nu?length gt 0>
                                    <p class="text-danger">${nu}</p>
                                </#if>
                                </div>
                                <div class="col-xs-2">
                                    <input type="text" class="form-control" id="u2r" disabled>
                                    <input type="hidden" id="tt" name="total">
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <p class="charge-font inline-block">支付方式：</p>
                            <label class="select-label">
                                <input type="radio" name="pay_method" class="x-radio flat-red" checked value="1"> 支付宝
                            </label>
                            <label class="select-label">
                                <input type="radio" name="pay_method" class="x-radio flat-red" value="2"> 电汇支付
                            </label>
                            <label class="select-label">
                                <input type="radio" name="pay_method" class="x-radio flat-red" disabled> 敬请期待
                            </label>
                        </div>

                        <div class="form-group">
                            <button class="btn btn-default" type="submit" id="submit">提交</button>
                        </div>
                    </form>
                </div>
            </div>

            <div class="box" style="display: none;" id="e2">
                <div class="box-header with-border">
                    <h3 class="box-title">电子汇款信息</h3>
                </div>
                <div class="box-body">
                    <pre style="font-size: 16px;">
（美国银行，BOA）
BANK NAME : BANK OF AMERICA
BANK ADDRESS: 14284 SCHLEISMAN RD, EASTVALE, CA 92880
SWIFT CODE: BOFAUS3N
ACCOUNT NUMBER : 3250 9075 4180
ROUTING NUMBER : 121000358 (PAPER&ELECTRONIC)
                 026009593 (WIRES)

ACCOUNT  NAME : HUFU INC
hufuaccounting@hufufeel.com（PayPal）
hufuaccounting@hufufeel.com（payoneer）
                    </pre>
                    <div class="alert alert-warning alert-dismissible">
                        <h4><i class="icon fa fa-warning"></i> 提醒：</h4>
                        <ul>
                            <li>每笔汇款需在备注中填写您的客户编号，您的客户编号为：${USER.hwcSn}。</li>
                            <li>汇款金额到账时间需1-3个工作日，最终HUFU账号显示金额以实际到款为标准。</li>
                        </ul>
                    </div>
                </div>
            </div>


        </section>
    </div>

<#include "*/_layout/footer.ftl"/>
</div>

<#include "*/_layout/script.ftl" />
<script>
    $(function () {
//        $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
//            checkboxClass: 'icheckbox_flat-green',
//            radioClass: 'iradio_flat-green'
//        });

        $('input[type="radio"].flat-red').on('ifChecked', function(event){
            var v = $(this).val();

            if (v === "2") {
                $('#e2').fadeIn();
                $("#submit").fadeOut();
            } else {
                $('#e2').fadeOut();
                $("#submit").fadeIn();
            }
        });

        var u2r = ${U2R};

        $('#total').change(function () {
            var value = $(this).val();
            if (isEmpty(value) || parseFloat(value) != value) {
                $('#r2u').val("请输入数字")
            } else {
                $.ajax({
                    url: '/api/base/u2r',
                    data: {
                        value: value
                    },
                    success: function (data) {
                        console.log(data);
                        if (data.success) {
                            $('#u2r').val(data.data + " RMB");
                            $('#tt').val(data.data);
                        }
                    }
                })
            }

        });

        $('#main').submit(function () {
            var v = $('#tt').val();
            if (isEmpty(v)) {
                alert("请输入充值金额！");
                return false;
            }
            return true;
        });
    })
</script>
</body>
</html>
