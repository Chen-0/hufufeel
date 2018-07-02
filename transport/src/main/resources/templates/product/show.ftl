<!DOCTYPE html>
<html>
<#assign TITLE = "查看货品">
<#include "*/_layout/head.ftl" />

<body class="hold-transition skin-black-light sidebar-mini">


<div class="wrapper">
<#include "*/_layout/aside.ftl" />

    <div class="content-wrapper">
        <section class="content-header">
            <h1>
            ${TITLE}
                <small>${ele.productName}</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="#">Examples</a></li>
                <li class="active">Blank page</li>
            </ol>
        </section>


        <section class="content" style="padding-top: 55px;">
            <div class="row">
                <div class="col-md-10 col-md-offset-1 col-sm-12 col-xs-12">
                    <div class="box">
                        <div class="box-body">
                            <div class="row">
                                <div class="col-xs-3">
                                    <img src="/file/${ele.image.name}" alt="" style="display: block; margin: 0 auto;">
                                </div>
                                <div class="col-xs-9">
                                    <table class="table table-bordered">
                                        <tr>
                                            <td>货品名称</td>
                                            <td>${ele.productName}</td>
                                            <td>SKU</td>
                                            <td>${ele.productSku}</td>
                                            <td>原产地</td>
                                            <td>${ele.origin!}</td>
                                        </tr>
                                        <tr>
                                            <td>电池类型</td>
                                            <td>${ele.isBattery?string("是", "否")}</td>
                                            <td>是否危险品</td>
                                            <td>${ele.isDanger?string("是", "否")}</td>
                                            <td>业务类型</td>
                                            <td>${ele.businessType?string("进口业务", "出口业务")}</td>
                                        </tr>

                                        <tr>
                                            <td>长（CM）</td>
                                            <td>${ele.length}</td>
                                            <td>宽（CM）</td>
                                            <td>${ele.width}</td>
                                            <td>高（CM）</td>
                                            <td>${ele.height}</td>
                                        </tr>

                                        <tr>
                                            <td>重量（KG）</td>
                                            <td>${ele.weight}</td>
                                            <td>体积（立方米）</td>
                                            <td>${ele.vol?string("0.########")}</td>
                                            <td colspan="2"></td>
                                        </tr>

                                        <tr>
                                            <td>有效期</td>
                                            <td>
                                            <#if ele.deadline??>
                                                    ${ele.deadline?date}
                                                </#if>
                                            </td>
                                            <td>申报名称</td>
                                            <td>${ele.quotedName}</td>
                                            <td>申报价格</td>
                                            <td>${ele.quotedPrice} USD</td>
                                        </tr>
                                        <tr>
                                            <td>状态</td>
                                            <td>${ele.status.getValue()}</td>
                                            <td colspan="4"></td>
                                        </tr>

                                        <tr>
                                            <td>备注</td>
                                            <td colspan="5">${ele.comment!}</td>
                                        </tr>

                                        <#if ele.status.ordinal() == 2>
                                        <tr class="text-danger">
                                            <td>审核失败原因</td>
                                            <td colspan="5">${ele.reason!}</td>
                                        </tr>
                                        </#if>
                                    </table>
                                </div>
                            </div>
                        </div>
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
