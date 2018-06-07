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

                    <form class="form-horizontal" role="form" method="post" action="/user/charge_account">
                        <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>

                        <div class="form-group">
                            <p class="charge-font">账户余额：<strong>${USER.money}元</strong></p>
                        </div>

                        <div class="form-group inline-block">
                            <p class="charge-font inline-block">充值金额：</p>
                            <label class="select-label">
                                <input type="radio" name="total" class="x-radio flat-red" value="5" checked> 5元
                            </label>
                            <label class="select-label">
                                <input type="radio" name="total" class="x-radio flat-red" value="10"> 10元
                            </label>
                            <label class="select-label">
                                <input type="radio" name="total" class="x-radio flat-red" value="20"> 20元
                            </label>
                            <label class="select-label">
                                <input type="radio" name="total" class="x-radio flat-red" value="50"> 50元
                            </label>
                            <label class="select-label">
                                <input type="radio" name="total" class="x-radio flat-red" value="100"> 100元
                            </label>
                        </div>

                        <div class="form-group">
                            <p class="charge-font inline-block">支付方式：</p>
                            <label class="select-label">
                                <input type="radio" name="pay_method" class="x-radio flat-red" checked> 支付宝
                            </label>
                            <label class="select-label">
                                <input type="radio" name="pay_method" class="x-radio flat-red" disabled> 敬请期待
                            </label>
                        </div>

                        <div class="form-group">
                            <button class="btn btn-default" type="submit">提交</button>
                        </div>
                    </form>
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

//        $()
    })
</script>
</body>
</html>
