<!DOCTYPE html>
<html lang="en">
<#assign title="出库单详情" />
<#include "*/admin/_layout/head.ftl" />

<body>
<#include "*/admin/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
                出库单详情
                <small>${ele.sn}</small>
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
        <div class="col-xs-12">
        <#assign sShow=true>
        <#include "*/_layout/hwc/order_show_template.ftl">
        </div>
    </div>

    <div class="row" style="margin-top: 25px;">
        <div class="col-xs-8 col-xs-offset-2">
            <div class="panel panel-info">
                <div class="panel-heading">
                    运单出库
                </div>
                <div class="panel-body">
                    <form class="xn-form" method="post" action="/admin/order/${ele.id}/check_out">
                        <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>

                        <@formGroup label="运费（USD）" name="total" ></@formGroup>
                        <@formGroup label="快递公司" name="express" ></@formGroup>
                        <@formGroup label="快递单号" name="expressNo" ></@formGroup>


                        <div class="form-group">
                            <div class="col-xs-12">
                                <button class="btn btn-primary" type="submit">提交</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
</body>
</html>