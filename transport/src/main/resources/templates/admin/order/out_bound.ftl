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
                    <form method="post" action="/admin/order/${ele.id}/out_bound" class="form-horizontal">
                        <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>

                        <div class="form-group">
                            <div class="col-sm-4">
                                <label>订单费用（
                                    <#if cost?size == 1>
                                        按件收费:${cost[0]}
                                    <#else>
                                        重量费:${cost[0]} 超件费:${cost[1]}
                                    </#if>
                                    ）</label>
                            </div>
                            <div class="col-sm-6">
                                <input name="order_fee" class="form-control" type="text" value="${ss.total}">
                            </div>
                            <label class="control-label col-sm-2">


                            </label>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-4">
                                <label>打包费</label>
                            </div>
                            <div class="col-sm-6">
                                <select name="package_fee" class="form-control">
                                <#list package_fee_list?keys as i>
                                    <option value="${i}">${package_fee_list[i]}</option>
                                </#list>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-4">
                                <label>物料费</label>
                            </div>
                            <div class="col-sm-6">
                                <select name="material_fee" class="form-control">
                                <#list material_fee_list?keys as i>
                                    <option value="${i}">${material_fee_list[i]}</option>
                                </#list>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-4">
                                <label>额外费用</label>
                            </div>
                            <div class="col-sm-6">
                                <input name="surcharge" class="form-control" type="text" value="0">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-4">
                                <label>额外费用说明</label>
                            </div>
                            <div class="col-sm-6">
                                <input name="surchargeComment" class="form-control" type="text">
                            </div>
                        </div>

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