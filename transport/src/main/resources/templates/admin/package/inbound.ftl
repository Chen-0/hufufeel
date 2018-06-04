<!DOCTYPE html>
<html lang="en">
<#assign title="货品管理" />
<#include "*/admin/_layout/head.ftl" />

<body>
<#include "*/admin/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
                入库 <small>${o.referenceNumber}</small>
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
            <form action="/admin/package/${o.id}/inbound" method="post">
                <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>
            <div class="table-responsive">
                <table class="bordered">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>参考号</th>
                        <th>客户</th>
                        <th>仓库</th>
                        <th>派送方式</th>
                        <th>状态</th>
                        <th>提交时间</th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr>
                        <td>${o.id}</td>
                        <td>${o.referenceNumber}</td>
                        <td>${o.nickname}</td>
                        <td>${o.warehouseName}</td>
                        <td>${o.distributionChannelName}</td>
                        <td>${o.status.getValue()}</td>
                        <td>${o.createdAt?string}</td>
                    </tr>
                    <#list o.packageProducts as p>
                    <tr class="table-sub-item">
                        <td>商品<input type="hidden" name="p[]" value="${p.product.id}"></td>
                        <td>${p.product.productName}</td>
                        <td>${p.product.productSku}</td>
                        <td>${p.qty} 件</td>
                        <td>约 ${p.weight} KG</td>
                        <td>实际数量（件）：<input type="text" class="form-control" placeholder="" name="qty[]"></td>
                        <td>实际重量（KG）：<input type="text" class="form-control" placeholder="" name="weight[]"></td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
                    <div class="text-center">
                        <button class="btn btn-primary" type="submit">提交</button>
                    </div>
            </form>
            </div>
        </div>
    </div>
</div>


<#-- 入库的弹框 -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form id="commentForm" action="/product/{id}/change_status" method="post">
                <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">货品入库</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="qty">实际数量（件）：</label>
                        <input type="text" class="form-control" id="qty" placeholder="" name="qty">
                    </div>
                    <div class="form-group">
                        <label for="weight">实际重量（KG）：</label>
                        <input type="text" class="form-control" id="weight" placeholder="" name="weight">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
                    <button type="submit" class="btn btn-primary">提交</button>
                </div>
            </form>

        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
<script>
    $(function () {
        $('.x-inbound').click(function () {
            var id = $(this).attr('data-id');

            $('#commentForm').attr('action', '/admin/package/' + id + '/change_status')
        });
    })
</script>
</body>
</html>