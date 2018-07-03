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
        <div class="col-xs-8 col-xs-offset-2">
        <#include "*/_layout/hwc/package_show.ftl">
        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
</body>
</html>