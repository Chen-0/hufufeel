<!DOCTYPE html>
<html>
<#assign TITLE="查询库存">
<#include "*/_layout/head.ftl" />

<body class="hold-transition skin-black-light sidebar-mini">
<div class="wrapper">
<#include "*/_layout/aside.ftl" />

    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                查询库存
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="#">Examples</a></li>
                <li class="active">Blank page</li>
            </ol>
        </section>

        <section class="content">
            <div class="box">

                <div class="box-body">
                    <form class=" margin-bottom" role="form" method="get" action="/stock/index">
                        <div class="row">
                            <div class="form-group col-xs-5">
                                <label for="keyword">关键字：</label>
                                <input class="form-control" id="keyword" name="keyword" type="text" value="${keyword!}">
                            </div>
                        </div>

                        <div class="form-group">
                            <#list warehouses as w>
                            <label for="checkbox-id">
                                <#assign cc = false>
                                <#if ws??>
                                <#list ws as cw>
                                    <#if cw == w.id>
                                        <#assign cc = true>
                                    </#if>
                                </#list>
                                </#if>
                                <#if cc >
                                    <input class="form-control x-radio flat-red" type="checkbox" name="w[]" value="${w.id}" checked> ${w.name}
                                <#else>
                                    <input class="form-control x-radio flat-red" type="checkbox" name="w[]" value="${w.id}"> ${w.name}
                                </#if>

                            </label>
                            </#list>
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">搜索</button>
                            <a href="/stock/index" class="btn btn-default">重置</a>
                        </div>
                    </form>


                    <form role="form" method="post" action="/stock/select" id="selectForm">
                        <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>
                    <table class="table table-bordered table-striped table-condensed table-hover">
                        <thead>
                        <tr>
                            <th>编号</th>
                            <th>货品SKU</th>
                            <th>货品名称</th>
                            <th>仓库</th>
                            <th>数量</th>
                            <th>总重量</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list elements.getContent() as e>
                        <tr>
                            <td>${e_index + 1}</td>
                            <td>${e.product.productSku}</td>
                            <td>${e.product.productName}</td>
                            <td>${e.warehouse.name}</td>
                            <td>${e.quantity}</td>
                            <td>${e.quantity * e.product.weight}</td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                    </form>
                </div>

                <div class="box-footer clearfix">
                    <#assign ff="&">
                    <#if ws??>
                        <#list ws as w>
                            <#assign ff="${ff}&w%5B%5D=${w}">
                        </#list>
                    </#if>
                <#assign BASEURL="/stock/index?keyword=${keyword!}${ff}&page="/>
                <#include "*/_layout/v2.0/components/pages.ftl">
                </div>
            </div>
        </section>
    </div>
<#include "*/_layout/footer.ftl"/>
</div>

<#include "*/_layout/script.ftl" />
<script>
    $(function () {
        $('#select_all').click(function () {
            if($(this).is(":checked")) {
                changeSelect(true);
            } else {
                changeSelect(false);
            }
        });

        function changeSelect(value) {
            $('.x-checkbox').prop("checked", value);
        }

        $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
            checkboxClass: 'icheckbox_flat-green',
            radioClass: 'iradio_flat-green'
        });

        $("#addToSend").click(function (e) {
            e.preventDefault();

            if($(".x-checkbox:checked").length === 0) {
                alert("请选择一件或多件货品");
                return;
            }

            $('#selectForm').submit();
        });
    });
</script>
</body>
</html>
