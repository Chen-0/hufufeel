<!DOCTYPE html>
<html>
<#assign TITLE="充值记录">
<#include "*/_layout/head.ftl" />

<body class="hold-transition skin-black-light sidebar-mini">
<div class="wrapper">
<#include "*/_layout/aside.ftl" />

    <div class="content-wrapper">
        <section class="content-header">
            <h1>
            ${TITLE}
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="#">Examples</a></li>
                <li class="active">Blank page</li>
            </ol>
        </section>

        <section class="content">
            <div class="box">

                <div class="box-body">
                    <table class="table table-bordered table-striped table-condensed table-hover">
                        <thead>
                        <tr>
                            <th>编号</th>
                            <th>金额</th>
                            <th>支付渠道</th>
                            <th>支付状态</th>

                            <th>创建时间</th>
                            <th>支付时间</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list elements.getContent() as e>
                        <tr>
                            <td>${e_index + 1}</td>
                            <td>${e.totalFee}</td>
                            <td>${e.type.value}</td>
                            <td>${e.status.getValue()}</td>
                            <td>${e.createdAt?string}</td>
                            <td>${e.successAt?string}</td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>

                <div class="box-footer clearfix">
                <#assign BASEURL="/user/statements/index?page="/>
                <#include "*/_layout/v2.0/components/pages.ftl">
                </div>
            </div>
        </section>
    </div>
<#include "*/_layout/footer.ftl"/>
</div>

<#include "*/_layout/script.ftl" />
</body>
</html>
