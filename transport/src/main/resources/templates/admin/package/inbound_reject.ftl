<!DOCTYPE html>
<html lang="en">
<#assign title="退货入库" />
<#include "*/admin/_layout/head.ftl" />

<body>
<#include "*/admin/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
            ${title}
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
            <form action="/admin/package/${o.id}/inbound_reject" method="post" enctype="multipart/form-data">
                <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>

                <div class="panel panel-info">
                    <div class="panel-heading">
                    ${title} 参考号：${o.referenceNumber!}
                    </div>
                    <div class="panel-body">
                        <div class="table-responsive">
                            <table class="bordered">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>参考号</th>
                                    <th>收件人</th>
                                    <th>客户</th>
                                    <th>仓库</th>
                                    <th>状态</th>
                                    <th>提交时间</th>
                                </tr>
                                </thead>

                                <tbody>
                                <tr>
                                    <td>${o.id}</td>
                                    <td>${o.referenceNumber}</td>
                                    <td>${o.contact}</td>
                                    <td>${o.nickname}</td>
                                    <td>${o.warehouseName}</td>
                                    <td>${o.status.getValue()}</td>
                                    <td>${o.createdAt?string}</td>
                                </tr>
                                <#list o.packageProducts as p>
                                <tr class="table-sub-item">
                                    <td>商品<input type="hidden" name="p[]" value="${p.product.id}"></td>
                                    <td>${p.product.productName}</td>
                                    <td>${p.product.productSku}</td>
                                    <td>预计：${p.expectQuantity} 件</td>
                                    <td>已入库数量：${p.quantity} 件</td>

                                    <#if p.quantity lt p.expectQuantity>
                                        <td colspan="2">本次入库件数（件）：<input type="text" class="form-control" placeholder=""
                                                                         name="qty[]">
                                        <#else>
                                            <td colspan="2"></td>
                                    </#if>

                                    </td>
                                </tr>
                                </#list>
                                </tbody>
                            </table>
                        </div>

                        <div class="row" style="margin-top: 25px;">
                            <div class="col-xs-6">
                                <div class="form-group">
                                    <label for="total_fee">退货入库费用</label>
                                    <input type="text" class="form-control" id="total_fee" name="total_fee"
                                           value="${s.total}">
                                </div>
                            </div>
                        </div>

                        <div class="row" style="margin-top: 25px;">
                            <div class="col-xs-6">
                                <label for="file">导入箱号（可不上传）</label>
                                <input type="file" name="file">
                                <a href="/static/hwc/import_reject_no.xlsx" download="导入箱号模板.xlsx">导入模板下载</a>
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer clearfix">
                        <button class="pull-right btn btn-default" type="submit">提交</button>
                        <button class="pull-right btn btn-danger" id="Fbtn" type="button" style="margin-right: 15px;">结束入库</button>
                    </div>
                </div>
            </form>

            <#if pb?exists && pb?size gt 0 >
            <div class="panel panel-info">
                <div class="panel-heading">
                    箱号
                </div>
                <div class="panel-body">
                    <div class="table-responsive">
                        <table table class="bordered">
                            <thead>
                            <tr>
                                <td>箱号</td>
                            </tr>
                            </thead>
                            <tbody>
                                    <#list pb as p>
                                    <tr>
                                        <td>${p.boxNo}</td>
                                    </tr>
                                    </#list>
                            </tbody>
                        </table>
                    </div>

                </div>
            </div>
            </#if>

        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
<script>
    $(function () {
        $('#Fbtn').click(function (e) {
            e.preventDefault();

            if(confirm("结束入库将无法再次操作入库，确认继续？")) {

                var total = $("#total_fee").val();

                $.ajax({
                    url: '/admin/package/${o.id}/finish_inbound_reject',
                    data: {
                        total: total
                    },
                    success: function (e) {
                        if (e.success === true) {
                            window.location.href = "/admin/package/index";
                        } else {
                            alert(e.message);
                        }
                    }
                });
            }
        })
    })
</script>
</body>
</html>