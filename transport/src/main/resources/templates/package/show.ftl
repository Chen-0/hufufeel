<!DOCTYPE html>
<html>
<#if ele.type.ordinal() == 0>
    <#assign TITLE="入库单详情">
<#else>
    <#assign TITLE="退货单详情">
</#if>

<#include "*/_layout/head.ftl" />

<body class="hold-transition skin-black-light sidebar-mini">
<div class="wrapper">
<#include "*/_layout/aside.ftl" />

    <div class="content-wrapper">
        <section class="content-header">
            <h1>
            ${TITLE}
                <small>${ele.sn}</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="#">Examples</a></li>
                <li class="active">Blank page</li>
            </ol>
        </section>

        <section class="content">
            <div class="row">
                <div class="col-md-12 col-xs-12">
                    <#include "*/_layout/hwc/package_show.ftl">
                </div>
            </div>
        </section>
    </div>

<#include "*/_layout/footer.ftl"/>
</div>

<#include "*/_layout/script.ftl" />
</body>
</html>
