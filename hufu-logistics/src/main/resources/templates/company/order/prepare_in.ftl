<!DOCTYPE html>
<html lang="en">
<#assign title="运单入库"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">运单入库</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">
            <form role="form" method="post" action="/order/prepare/${order.id}" id="main">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                <table class="table table-striped table-bordered table-hover">
                    <tr>
                        <td>批次</td>
                        <td>${order.batch!}</td>
                    </tr>

                    <tr>
                        <td>大客户名称</td>
                        <td>${order.company.name!}</td>
                    </tr>

                    <tr>
                        <td>运单号</td>
                        <td>${order.trackingNumber!}</td>
                    </tr>

                    <tr>
                        <td>收件人</td>
                        <td>${order.contact!}</td>
                    </tr>
                    <tr>
                        <td>收件人电话</td>
                        <td>${order.phone!}</td>
                    </tr>
                    <tr>
                        <td>收件人地址</td>
                        <td>${order.address!}</td>
                    </tr>
                    <tr>
                        <td>邮编：</td>
                        <td>${order.zipCode!}</td>
                    </tr>
                    <tr>
                        <td>商品信息：</td>
                        <td>${order.goodsName!}</td>
                    </tr>
                    <tr>
                        <td>商品数量：</td>
                        <td>${order.quantity!}</td>
                    </tr>
                    <tr>
                        <td>申报价格：</td>
                        <td>${order.declared!}</td>
                    </tr>

                    <tr>
                        <td>入库重量：</td>
                        <td>
                            <input class="form-control" type="text" name="in_weight" id="out_weight">
                            <p class="text-danger hide" id="out_error">出库重必须是一个正整数</p>
                        </td>
                    </tr>

                    <tr>
                        <td>选择发货渠道：</td>
                        <td>
                            <select class="form-control" name="express_id" id="companyExpress">
                            <#list expresses as companyExpress>
                                <option value="${companyExpress.id}">${companyExpress.name}</option>
                            </#list>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>单价（元/磅）：</td>
                        <td id="price">0</td>
                    </tr>

                    <tr>
                        <td>费用：</td>
                        <td id="total">0</td>
                    </tr>
                </table>

                <button type="button" class="btn btn-default" id="btn">入库</button>
            </form>
        </div>
    </div>
</div>


<#include "/_layout/script.ftl"/>
<script>
    var data = {
    <#list order.company.companyRExpressList as companyExpress>
    ${companyExpress.companyExpress.id} : ${companyExpress.price},
    </#list>
    };



    var express_id = $('#companyExpress').val();
    $('#price').html(data[express_id]);

    $('#out_weight').change(function () {
        var out_wight = $('#out_weight').val();
        var express_id = $('#companyExpress').val();
        console.log(out_wight);
        console.log(express_id);

        $('#total').html((out_wight * data[express_id]).toFixed(2));
    });

    $('#companyExpress').change(function () {
        var out_wight = $('#out_weight').val();
        var express_id = $('#companyExpress').val();

        $('#price').html(data[express_id]);
        $('#total').html((out_wight * data[express_id]).toFixed(2));
    });

    $('#btn').click(function (event) {
        event.preventDefault();

        var out_weight = $('#out_weight').val();
        if (!empty(out_weight) && !isNaN(out_weight) && out_weight >= 0) {
//            $('#main').submit();
            var url = $('#main').attr('action');
            var data = $('#main').serialize();

            $.ajax({
                url: url,
                data: data,
                type: 'post',
                dataType: 'json',
                success: function (data) {
                    console.log(data);

                    if (data.code == 0) {
                        alert(data.info);

                        window.location.href = data.data.url;
                    }
                }
            })
        } else {
            $('#out_error').removeClass('hide');
        }
    });

    function empty(va) {
        return va == '' || va == null;
    }

</script>
</body>
</html>