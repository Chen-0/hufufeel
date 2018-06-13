<!DOCTYPE html>
<html>
<#assign TITLE="发货单管理">
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
                    <form class="form-inline margin-bottom" role="form" method="get" action="/order/index">
                        <#if _STATUS?? >
                            <input type="hidden" name="status" value="${_STATUS}">
                        </#if>
                        <div class="form-group">
                            <label for="keyword">关键字：</label>
                            <input class="form-control" id="keyword" name="keyword" type="text" value="${keyword!}">
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">搜索</button>
                            <a href="/order/index?status=${_STATUS!}" class="btn btn-default">重置</a>
                        </div>
                    </form>


                    <table class="table table-bordered table-striped table-condensed table-hover">
                        <thead>
                        <tr>
                            <th>出库单号</th>
                            <th>参考号</th>
                            <th>销售交易号</th>
                            <th>SKU数</th>
                            <th>总件数</th>
                            <th>实际总重量</th>
                            <th>状态</th>
                            <th>创建时间</th>
                            <th>备注</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list elements.getContent() as e>
                        <tr>
                            <td>${e.sn}</td>
                            <td>${e.referenceNumber}</td>
                            <td>${e.tn}</td>
                            <td>${e.skuQty}</td>
                            <td>${e.quantity}</td>
                            <td>
                                <#if e.weight gt 0 >
                                    ${e.weight}
                                </#if>
                            </td>

                            <td>${e.status.getValue()}</td>
                            <td>${e.createdAt?string}</td>
                            <td>${e.comment!}</td>
                            <td>
                                <a href="#">查看详情</a>
                                <#if e.status.ordinal() == 0 || e.status.ordinal() == 4>
                                    <a href="/order/${e.id}/cancel">取消入库单</a>
                                </#if>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>

                <div class="box-footer clearfix">
                <#assign BASEURL="/order/index?keyword=${keyword}&status=${_STATUS!}&page="/>
                <#include "*/_layout/v2.0/components/pages.ftl">
                </div>
            </div>


        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

<#include "*/_layout/footer.ftl"/>
</div>

<#include "*/_layout/script.ftl" />
</body>
</html>
