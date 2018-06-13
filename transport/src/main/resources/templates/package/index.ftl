<!DOCTYPE html>
<html>
<#assign TITLE="入库单管理">
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
                    <form class="form-inline margin-bottom" role="form" method="get" action="/package/index">
                        <div class="form-group">
                            <label for="keyword">关键字：</label>
                            <input class="form-control" id="keyword" name="keyword" type="text" value="${keyword!}">
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">搜索</button>
                            <a href="/package/index?status=${_STATUS!}" class="btn btn-default">重置</a>
                        </div>
                    </form>


                    <table class="table table-bordered table-striped table-condensed table-hover">
                        <thead>
                        <tr>
                            <th>入库单号</th>
                            <th>参考号</th>
                            <th>仓库</th>
                            <th>SKU数</th>
                            <th>总件数</th>
                            <th>实际总件数</th>
                            <th>总重量</th>
                            <th>状态</th>
                            <th>创建时间</th>
                            <th>备注</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list elements.getContent() as e>
                        <#assign tw = 0>
                        <#list e.packageProducts as pp>
                            <#assign tw = tw + pp.quantity * pp.product.weight>
                        </#list>
                        <tr>
                            <td>${e.sn}</td>
                            <td>${e.referenceNumber}</td>
                            <td>${e.warehouseName}</td>
                            <td>${e.packageProducts?size}</td>
                            <td>${e.expectQuantity}</td>
                            <td>${e.quantity}</td>
                            <td>${tw}</td>
                            <td>${e.status.getValue()}</td>
                            <td>${e.createdAt?string}</td>
                            <td>${e.comment}</td>
                            <td>
                                <a href="#">查看详情</a>
                                <#if e.status.ordinal() == 0>
                                <a href="/package/${e.id}/cancel">取消入库单</a>
                                </#if>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>

                <div class="box-footer clearfix">
                <#assign BASEURL="/package/index?keyword=${keyword}&status=${_STATUS!}&page="/>
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
