<!DOCTYPE html>
<html lang="en">
<#if ele.type.ordinal() == 0>
    <#assign title="入库单详情" />
<#else>
    <#assign title="退货单详情" />
</#if>

<#include "*/admin/_layout/head.ftl" />

<body>
<#include "*/admin/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
            ${title} <small>${ele.sn}</small>
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
        <div class="col-xs-10 col-xs-offset-1">
        <#include "*/_layout/hwc/package_show.ftl">

            <div class="row">
                <div class="col-xs-6 col-xs-offset-3">
                    <form action="/admin/package/${ele.id}/update_search_no" method="post">
                        <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>

                        <div class="row" style="margin-top: 25px;">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label for="searchNo">修改快递单号：</label>
                                    <input type="text" class="form-control" id="searchNo" name="searchNo" value="${ele.searchNo!}">
                                </div>

                                <button style="margin-top: 15px;" class="btn btn-primary btn-block" type="submit">提交</button>
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