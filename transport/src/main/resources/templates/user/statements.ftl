<!DOCTYPE html>
<html>
<#assign TITLE="费用流水">
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

                    <form class="form-horizontal" action="/user/statements/index" method="get">
                        <div class="form-group">
                            <label for="" class="col-xs-1 control-label">筛选：</label>
                        </div>
                        <div class="form-group">
                            <label for="startAt" class="col-xs-1 control-label">从：</label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="startAt" name="startAt" value="${startAt!}">
                            </div>

                            <label for="endAt" class="col-xs-1 control-label">至：</label>
                            <div class="col-xs-3">
                                <input type="text" class="form-control" id="endAt" name="endAt" value="${endAt!}">
                            </div>
                        </div>


                        <div class="form-group">
                            <div class="col-xs-6 col-xs-offset-1">
                                <button type="submit" class="btn btn-primary">搜索</button>
                                <a href="/user/statements/index" class="btn btn-default">重置</a>
                            </div>
                        </div>
                    </form>


                    <table class="table table-bordered table-striped table-condensed table-hover">
                        <thead>
                        <tr>
                            <th>编号</th>
                            <th>费用说明</th>
                            <th>费用类型</th>
                            <th>支付状态</th>
                            <th>金额</th>
                            <th>创建时间</th>
                            <th>支付时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list elements.getContent() as e>
                        <tr>
                            <td>${e_index + 1}</td>
                            <td>${e.comment}</td>
                            <td>${e.type.getValue()}</td>
                            <td>${e.status.getValue()}</td>
                            <td>${e.total}</td>
                            <td>${e.createdAt?string}</td>
                            <td>
                                <#if e.status.ordinal() != 0>
                                    ${e.payAt?string}
                                </#if>
                            </td>
                            <td>
                                <#if e.status.ordinal() == 0>
                                    <a href="/user/statements/${e.id}/pay">立即支付</a>
                                </#if>

                                <#switch e.type.ordinal()>
                                    <#case 0>
                                        <a href="/package/${e.target}/show">详情</a>
                                        <#break>
                                    <#case 1>
                                        <a href="/package/${e.target}/show">详情</a>
                                        <#break>
                                    <#case 2>
                                        <a href="/order/${e.target}/show">详情</a>
                                        <#break>
                                </#switch>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>

                <div class="box-footer clearfix">
                    <a href="/user/statements/export?startAt=${startAt!}&endAt=${endAt!}" class="btn btn-primary" target="_blank" download="HUFU${USER.hwcSn}费用明细.xlsx">导出</a>
                <#assign BASEURL="/user/statements/index?page="/>
                <#include "*/_layout/v2.0/components/pages.ftl">
                </div>
            </div>
        </section>
    </div>
<#include "*/_layout/footer.ftl"/>
</div>

<#include "*/_layout/script.ftl" />
<script>
    $(function () {
        $('#startAt, #endAt').datepicker({
            format: 'yyyy-mm-dd',
            autoclose: true
        });
    });
</script>
</body>
</html>
