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
</div>

<#include "*/admin/_layout/script.ftl"/>
</body>
</html>