<!DOCTYPE html>
<html lang="en">
<#assign title="修改货品" />
<#include "*/admin/_layout/head.ftl" />

<body>
<#include "*/admin/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
            ${title}
            </h1>
        </div>
    </div>

<#if success?? >
    <div class="alert alert-success alert-dismissable alert-message">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
    ${success}
    </div>
</#if>

    <div class="row">
        <div class="col-xs-8 col-xs-offset-2">
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
                        <#if ele.status.ordinal() == 2>
                            <td>原因</td>
                            <td colspan="3">${ele.reason!}</td>
                        <#else>
                            <td colspan="4"></td>
                        </#if>

                        </tr>

                        <tr>
                            <td>备注</td>
                            <td colspan="5">${ele.comment!}</td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>



<#include "*/admin/_layout/script.ftl"/>
</body>
</html>