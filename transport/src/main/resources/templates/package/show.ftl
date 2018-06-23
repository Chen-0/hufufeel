<!DOCTYPE html>
<html>
<#assign TITLE="入库单详情">
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

        <section class="content">
            <div class="col-md-12 col-xs-12">
                <div class="box">
                    <div class="box-body">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th>入库单号</th>
                                <th>参考号</th>
                                <th>仓库</th>
                                <th>SKU数</th>
                                <th>总件数</th>
                                <th>实际总件数</th>
                                <th>总重量</th>
                                <th>状态</th>
                                <th>创建时间</th>
                                <th>备注</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#assign tw = 0>
                            <#list ele.packageProducts as pp>
                                <#assign tw = tw + pp.quantity * pp.product.weight>
                            </#list>
                            <tr>
                                <td>${ele.sn}</td>
                                <td>${ele.referenceNumber}</td>
                                <td>${ele.warehouseName}</td>
                                <td>${ele.packageProducts?size}</td>
                                <td>${ele.expectQuantity}</td>
                                <td>${ele.quantity}</td>
                                <td>${tw}</td>
                                <td>${ele.status.getValue()}</td>
                                <td>${ele.createdAt?string}</td>
                                <td>${ele.comment!}</td>
                            </tr>
                            </tbody>
                        </table>


                        <table class="table table-bordered" style="margin-top: 25px;">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>货品名称</th>
                                <th>货品SKU</th>
                                <th>预估数量</th>
                                <th>实际数量</th>
                                <th>单件重量</th>
                                <th>总重量</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#list ele.packageProducts as pp>
                            <tr>
                                <td>${pp_index + 1}</td>
                                <td><a href="/product/${pp.productId}/show">${pp.product.productName}</a></td>
                                <td>${pp.product.productSku}</td>
                                <td>${pp.expectQuantity}</td>
                                <td>${pp.quantity}</td>
                                <td>${pp.product.weight}</td>
                                <td>${pp.product.weight * pp.quantity}</td>
                                <td><a href="/product/${pp.productId}/show">货品详情</a></td>
                            </tr>
                            </#list>
                            </tbody>
                        </table>

                    <#if statements?exists && statements?size gt 0 >
                        <table class="table table-bordered" style="margin-top: 25px;">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>费用类型</th>
                                <th>费用说明</th>
                                <th>总额</th>
                                <th>状态</th>
                                <th>创建时间</th>
                                <th>交易时间</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                                <#list statements as e>
                                <tr>
                                    <td>${e_index + 1}</td>
                                    <td>${e.type.getValue()}</td>
                                    <td>${e.comment!}</td>
                                    <td>${e.total} USD</td>
                                    <td>${e.status.getValue()}</td>
                                    <td>${e.createdAt?string}</td>
                                    <td>
                                        <#if e.payAt??>
                                        ${e.payAt?string}
                                    </#if>
                                    </td>
                                    <td>
                                        <a href="/user/statements/${e.id}/pay">立即支付</a>
                                    </td>
                                </tr>
                                </#list>
                            </tbody>
                        </table>
                    </#if>
                    </div>

                    <div class="box-footer clearfix">
                        <a class="btn btn-primary" href="/package/${ele.id}/print?type=sku" target="_blank">打印SKU</a>
                    </div>
                </div>
            </div>
        </section>
    </div>

<#include "*/_layout/footer.ftl"/>
</div>

<#include "*/_layout/script.ftl" />
</body>
</html>
