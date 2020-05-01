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
                    <form class="form-horizontal" action="/order/index" method="get">
                    <#if _STATUS?? >
                        <input type="hidden" name="status" value="${_STATUS!}">
                    </#if>
                        <div class="form-group">
                            <label for="" class="col-xs-1 control-label">筛选：</label>
                        </div>
                        <div class="form-group">
                            <label for="keyword" class="col-xs-1 control-label">关键字：</label>
                            <div class="col-xs-3">
                                <input class="form-control" id="keyword" name="keyword" type="text" value="${keyword!}">
                            </div>
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
                                <a href="/order/index?status=${_STATUS!}" class="btn btn-default">重置</a>
                                <a class="btn btn-primary" href="/order/export?keyword=${keyword!}&status=${_STATUS!}&startAt=${startAt!}&endAt=${endAt!}">发货单导出</a>
                            </div>
                        </div>
                    </form>


                    <table class="table table-bordered table-striped table-condensed table-hover">
                        <thead>
                        <tr>
                            <th>出库单号</th>
                            <th>快递单号</th>
                            <th>平台单号</th>
                            <th>SKU数</th>
                            <th>总件数</th>
                            <th>实际总重量</th>
                            <th>状态</th>
                            <th>创建时间</th>
                            <th>收件人</th>
                            <th>备注</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list elements.getContent() as e>
                        <tr>
                            <td>${e.sn}</td>
                            <td>${e.expressNo}</td>
                            <td>${e.referenceNumber}</td>
                            <td>${e.skuQty}</td>
                            <td>${e.quantity}</td>
                            <td>
                                <#if e.weight gt 0 >
                                    ${e.weight}
                                </#if>
                            </td>

                            <td>${e.status.getValue()}</td>
                            <td>${e.createdAt?string}</td>
                            <td>${e.contact}</td>
                            <td>${e.comment!}</td>
                            <td>
                                <a href="/order/${e.id}/show">查看</a>
                                <#if e.status.ordinal() == 0 || e.status.ordinal() == 4>
                                    <a href="/order/${e.id}/update">修改</a>
                                    <a href="/order/${e.id}/cancel" class="x-remove">取消</a>
                                </#if>


                                <#assign  key = e.id?string>
                                <#if smap?exists && smap[key]?exists>
                                    <a href="/user/statements/${smap["${e.id}"].id}/pay">立即支付</a>
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
<script>
    $('.x-remove').click(function (e) {
        e.preventDefault();

        var url = $(this).attr("href");

        if (confirm("确认取消该条目")) {
            window.location.href = url;
        }
    });


    $(function () {
        $('#startAt, #endAt').datepicker({
            format: 'yyyy-mm-dd',
            autoclose: true
        });
    });
</script>
</body>
</html>
