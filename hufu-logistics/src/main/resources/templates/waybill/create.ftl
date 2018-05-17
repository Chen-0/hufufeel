<!DOCTYPE html>
<html lang="en">
<#assign title="货物入库"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">货物入库</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">
            <form role="form" method="post" action="/waybill/create" id="main">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">美国快递公司快递单信息</h3>
                    </div>
                    <div class="panel-body">
                        <div class="form-group">
                            <label for="agea">入库仓库（必填）：</label>
                            <input type="text" class="form-control" id="agea" name="agea">
                        </div>
                        <div class="form-group">
                            <label for="express">快递公司（必填）：</label>
                            <select class="form-control" id="express" name="express">
                                <option value="0">--select--</option>
                                <option value="UPS">UPS</option>
                                <option value="Fedex">Fedex</option>
                                <option value="USPS">USPS</option>
                                <option value="DHL">DHL</option>
                                <option value="TNT">TNT</option>
                                <option value="ONTRAC">ONTRAC</option>
                                <option value="Others">Others</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="expressnum">快递单号（必填）：</label>
                            <input type="text" class="form-control" id="expressnum" name="expressnum">
                        </div>
                        <div class="form-group">
                            <label for="customer">客户标示（必填）：</label>
                            <input type="text" class="form-control" id="customer" name="customer">
                        </div>
                        <div class="form-group">
                            <label for="weight">入库重（必填）：</label>
                            <input type="text" class="form-control" id="weight" name="weight">
                        </div>
                        <div class="form-group">
                            <label for="storageno">仓储位（必填）：</label>
                            <input type="text" class="form-control" id="storageno" name="storageNo">
                        </div>
                        <div class="form-group">
                            <label for="extraCost">额外收费：</label>
                            <input type="text" class="form-control" id="extraCost" name="extraCost" value="0">
                        </div>
                        <div class="form-group">
                            <label for="extraCostReason">额外收费原因：</label>
                            <input type="text" class="form-control" id="extraCostReason" name="extraCostReason">
                        </div>
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

                            <tr>
                                <td>
                                    <select class="form-control" name="gooda[]" data-id="">
                                        <option value="0">--请选择--</option>
                                        <#list brands as brand>
                                            <option value="${brand.brand}"
                                                    data-value="${brand.id}">${brand.brand}</option>
                                        </#list>
                                    </select>
                                </td>
                                <td>
                                    <select class="form-control" name="goodb[]" id="">
                                        <option value="0">--请选择--</option>
                                    </select>
                                </td>
                                <td><input class="form-control" type="text" name="brandname[]"></td>
                                <td><input class="form-control" type="text" name="goodname[]"></td>
                                <td><input class="form-control" type="text" name="countnum[]"></td>
                                <td><a href="#">删除</a></td>
                            </tr>

                            </tbody>
                        </table>
                    </div>
                    <div class="panel-footer">
                        <button class="btn btn-primary" id="add" type="button">添加货物</button>
                    </div>
                </div>

                <button type="submit" class="btn btn-default" id="btn">入库</button>
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