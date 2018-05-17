<!DOCTYPE html>
<html lang="en">
<#assign title="更改运单信息"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">更改运单信息</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-3 col-lg-6">
            <form role="form" method="post" action="/waybill/${waybill.id}/update" id="main">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <input type="hidden" name="waybillId" value="${waybill.id}">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">Tracking Number：${waybill.trackingNumber}</h3>
                    </div>
                    <div class="panel-body">
                        <div class="text-center">
                            <img src="/barcode/${waybill.trackingNumber}" alt="">
                        </div>
                        <table class="table table-bordered">
                            <tr>
                                <td colspan="3">收货信息 ：<#if waybill.userAddress?exists>${waybill.userAddress.addressa}</#if></td>
                            </tr>
                            <tr>
                                <td>用户邮箱：${waybill.user.username}</td>
                                <td>客户标识 ：${waybill.customer!}</td>
                                <td>身份证号码 ：<#if waybill.userAddress?exists>${waybill.userAddress.identity}</#if></td>
                            </tr>

                            <tr>
                                <td>仓 库 ：${waybill.storageNo}</td>
                                <td>运输渠道 ：${waybill.expressName!}</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td colspan="3">合箱订单号 ：${waybill.old_tracking_number!}</td>
                            </tr>
                            <tr>
                                <td>选择的操作要求 ：${waybill.operation!}</td>
                                <td>备注 ：${waybill.beizhu!}</td>
                            </tr>
                            <tr>

                            </tr>
                            <tr>
                                <td colspan="3">申报价格 ：${waybill.declareCost!}</td>
                            </tr>
                            <tr>
                                <td>额外收费 ：${waybill.extra_cost!}</td>
                                <td colspan="2">额外收费原因 ：${waybill.extra_cost_reason!}</td>
                            </tr>
                            <tr>
                                <td>补差价 ：${waybill.mendCost!}</td>
                                <td colspan="2">补差原因 ：${waybill.mend_reason!}</td>
                            </tr>
                        </table>
                    </div>
                </div>

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">货物信息</h3>
                    </div>
                    <div class="panel-body">
                        <table class="table">
                            <thead>
                            <tr>
                                <td width="18%">货物分类（必填）：</td>
                                <td width="18%">货物子分类（必填）：</td>
                                <td width="18%">英文品牌名：</td>
                                <td width="18%">货物明细：</td>
                                <td width="18%">数 量：</td>
                                <td width="10%">删 除：</td>
                            </tr>
                            </thead>
                            <tbody id="container">
                            <#list waybill.goods as goods>
                            <tr>
                                <td>
                                    <input class="form-control" type="text" name="gooda[]" value="${goods.gooda}">
                                    <input type="hidden" name="id[]" value="${goods.id}">
                                </td>
                                <td><input class="form-control" type="text" name="goodb[]" value="${goods.goodb}"></td>
                                <td><input class="form-control" type="text" name="brandname[]"
                                           value="${goods.brandname}"></td>
                                <td><input class="form-control" type="text" name="goodname[]" value="${goods.goodname}">
                                </td>
                                <td><input class="form-control" type="text" name="countnum[]" value="${goods.contnum}">
                                </td>
                                <td><a href="#">删除</a></td>
                            </tr>
                            </#list>

                            <#--<tr>-->
                            <#--<td>-->
                            <#--<select class="form-control" name="gooda[]" data-id="">-->
                            <#--<option value="0">--请选择--</option>-->
                            <#--<#list brands as brand>-->
                            <#--<option value="${brand.brand}"-->
                            <#--data-value="${brand.id}">${brand.brand}</option>-->
                            <#--</#list>-->
                            <#--</select>-->
                            <#--</td>-->
                            <#--<td>-->
                            <#--<select class="form-control" name="goodb[]" id="">-->
                            <#--<option value="0">--请选择--</option>-->
                            <#--</select>-->
                            <#--</td>-->
                            <#--<td><input class="form-control" type="text" name="brandname[]"></td>-->
                            <#--<td><input class="form-control" type="text" name="goodname[]"></td>-->
                            <#--<td><input class="form-control" type="text" name="countnum[]"></td>-->

                            <#--</tr>-->
                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <button class="btn btn-primary" id="add" type="button">添加货物</button>
                    </div>
                </div>

                <button class="btn btn-success" type="submit">更新信息</button>
            </form>
        </div>
    </div>
</div>


<#include "/_layout/script.ftl"/>
<script>
    var data = {<#list brands as brand><#if (brand_index>0) >,</#if>${brand.id} : [<#assign chilrend=brand.children><#list chilrend as b><#if (b_index>0) >,</#if>{id: '${b.id}',
        name: '${b.brand}'}</#list>]</#list>}
    ;

    $(function () {
        var $container = $('#container');

        $('#add').click(function () {
            var $box = $('<tr> <td> <select class="form-control" name="gooda[]" data-id=""> <option value="0">--请选择--</option><#list brands as brand> <option value="${brand.brand}" data-value="${brand.id}">${brand.brand}</option></#list> </select> </td> <td> <select class="form-control" name="goodb[]" id=""> <option value="0">--请选择--</option> </select> </td> <td><input class="form-control" type="text" name="brandname[]"></td> <td><input class="form-control" type="text" name="goodname[]"></td> <td><input class="form-control" type="text" name="countnum[]"></td> <td><a href="#">删除</a></td> </tr>');

            $($box.find('select[name="gooda[]"]')[0]).change(event);
            $($box.find('a')[0]).click(remove);

            $container.append($box);

//            console.log(a);
        });

        $('select[name="gooda[]"]').change(event);
        $container.find('a').click(remove);

        function event() {
            var id = $(this).find("option:selected").attr('data-value');
            console.log(data[id]);

            var str = '';

            for (var i = 0; i < data[id].length; i++) {
                str += '<option value="' + data[id][i].name + '">' + data[id][i].name + '</option>\n';
            }

            $(this).parent().next().children('select').html(str);
        }

        function remove(event) {
            event.preventDefault();
            $(this).parent().parent().remove();
        }
    });
</script>
</body>
</html>
