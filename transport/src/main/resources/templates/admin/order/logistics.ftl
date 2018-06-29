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
        <#include "*/admin/order/order_show_template.ftl">

            <div class="box">
                <div class="box-body" style="padding: 25px 15px;">


                    <div class="row">
                        <div class="col-md-6 col-md-offset-3">

                            <form role="form" action="/admin/order/${ele.id}/logistics" method="post">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                                <@formGroup label="快递公司" name="express"></@formGroup>
                                <@formGroup label="快递号" name="express_no"></@formGroup>

                                <div class="form-group">
                                    <label for="comment">物流信息</label>
                                    <textarea id="comment" class="form-control" rows="3" name="comment">${lg.comment!}</textarea>
                                </div>

                                <div class="text-center">
                                    <button class="btn btn-primary">提交</button>
                                </div>
                            </form>

                        </div>
                    </div>

                </div>
            </div>
        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
</body>
</html>