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
                仓库管理 （
                <#if status??>
                    <#switch status>
                        <#case 0> 待审核 <#break>
                        <#case 1> 已审核 <#break>
                        <#case 2> 已拒绝 <#break>
                    </#switch>
                <#else >
                查看所有
                </#if>
                ）
            <#--<small><a href="/company/create">新增货品</a></small>-->
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

            <div class="table-responsive">
                <table class="bordered">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>参考号</th>
                        <th>客户</th>
                        <th>仓库</th>
                        <th>状态</th>
                        <th>提交时间</th>
                        <th>备注</th>
                        <th>操作</th>
                    </tr>
                    </thead>

                    <tbody>
                    <#list elements.getContent() as o>
                    <tr>
                        <td>${o.id}</td>
                        <td>${o.referenceNumber}</td>
                        <td>${o.nickname}</td>
                        <td>${o.warehouseName}</td>
                        <td>${o.status.getValue()}</td>
                        <td>${o.createdAt?string}</td>
                        <td>${o.comment!}</td>
                        <td>
                            <#switch o.status.ordinal()>
                                <#case 0>
                                    <a href="/admin/package/${o.id}/inbound">入库</a>
                                <#break>
                            </#switch>
                        </td>
                    </tr>
                    <#list o.packageProducts as p>
                    <tr class="table-sub-item">
                        <td>商品</td>
                        <td>${p.product.productName}</td>
                        <td>${p.product.productSku}</td>
                        <td>预计：${p.expectQuantity} 件</td>
                        <#if o.status.ordinal() != 0 ||o.status.ordinal() != 3  >
                        <td>实际：${p.quantity} 件</td>
                        <td colspan="3"></td>
                        <#else>
                            <td colspan="4"></td>
                        </#if>
                    </tr>
                    </#list>
                    </#list>
                    </tbody>
                </table>

                <nav aria-label="Page navigation" style="text-align: center;">
                    <ul class="pagination">
                    <#if elements.isFirst() != true >
                        <li>
                            <a href="/admin/package/index?page=${elements.previousPageable().pageNumber}&status=${status!}"
                               aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </#if>


                    <#if elements.isLast() != true>
                        <li>
                            <a href="/admin/package/index?page=${elements.nextPageable().pageNumber}&status=${status!}"
                               aria-label="Previous">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </#if>
                    </ul>

                    <p>
                        当前第${elements.getNumber() + 1}页，一共${elements.getTotalPages()}页，一共${elements.getTotalElements()}条数据</p>
                </nav>

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