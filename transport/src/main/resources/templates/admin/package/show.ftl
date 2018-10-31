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
            ${title}
                <small>${ele.cn}</small>
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
<#if sub?exists && sub?size gt 0 >
            <div class="box">
                <div class="box-header with-border">
                    <h3 class="box-title">子退货单</h3>
                </div>
                <div class="box-body">
                    <ul>
                        <#list sub as s>
                            <li><a target="_blank" href="/admin/package/${s.id}/show">${s.cn}</a></li>
                        </#list>

                    </ul>
                </div>
            </div>
</#if>
        </div>
    </div>

</div>

<#include "*/admin/_layout/script.ftl"/>
</body>
</html>