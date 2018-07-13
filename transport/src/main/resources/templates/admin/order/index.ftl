<!DOCTYPE html>
<html lang="en">
<#assign title="出库单管理" />
<#include "*/admin/_layout/head.ftl" />

<body>
<#include "*/admin/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
            ${title}（
            <#if status??>
                <#switch status>
                    <#case 0> 待审核 <#break>
                    <#case 1> 待发货 <#break>
                    <#case 2> 已发货 <#break>
                    <#case 3> 已冻结 <#break>
                    <#case 4> 审核失败 <#break>
                    <#case 5> 已取消 <#break>
                </#switch>
            <#else >
                查看所有
            </#if>
                ）
            </h1>
        </div>
    </div>

<#if success?? >
    <div class="alert alert-success alert-dismissable alert-message">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
    ${success}
    </div>
</#if>

    <div class="row margin-bottom">
        <div class="col-xs-8 col-xs-offset-2">
            <form class="form-horizontal" action="/admin/order/index" method="get" role="form">
                <div class="form-group">
                    <label for="keyword" class="col-xs-6 control-label" style="text-align: center;">模糊搜索（出库单号、运单号）：</label>
                    <div class="col-xs-6">
                        <input class="form-control" id="keyword" name="keyword" value="${keyword!}">
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-xs-12">
                        <button class="btn btn-primary" type="submit">搜索</button>
                        <a class="btn btn-default" href="/admin/order/index">重置</a>
                        <a class="btn btn-primary" href="/admin/order/import">导入</a>
                        <a class="btn btn-primary" href="/admin/order/export">导出（导出的订单为待审核状态）</a>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-12">
            <div class="table-responsive">
                <table class="bordered">
                    <thead>
                    <tr>
                        <th>出库单号</th>
                        <th>运单号</th>
                        <th>客户</th>
                        <th>SKU数</th>
                        <th>总件数</th>
                        <th>实际总重量</th>
                        <th>创建时间</th>
                        <th>备注</th>
                    <#if status?exists && status == 4>
                        <th>失败原因</th>
                    </#if>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                    </thead>

                    <tbody>
                    <#list elements.getContent() as e>
                    <tr>
                        <td>${e.sn}</td>
                        <td>${e.expressNo}</td>
                        <td>${e.user.name}</td>
                        <td>${e.skuQty}</td>
                        <td>${e.quantity}</td>
                        <td>
                            <#if e.weight gt 0 >
                                    ${e.weight}
                                </#if>
                        </td>
                        <td>${e.createdAt?string}</td>
                        <td>${e.comment!}</td>
                        <#if status?exists && status == 4>
                            <td>${e.reason}</td>
                        </#if>
                        <td>${e.status.getValue()}</td>
                        <td>
                            <a href="/admin/order/${e.id}/show">查看详情</a>

                            <#switch e.status.ordinal()>
                                <#case 0>
                                    <a href="/admin/order/${e.id}/check_out">通过</a>
                                    <a class="x-fail-check" href="javascript:void(0);" data-id="${e.id}"
                                       data-toggle="modal"
                                       data-target="#myModal">拒绝</a>
                                    <#break>
                                <#case 1>
                                    <a href="/admin/order/${e.id}/out_bound">去发货</a>
                                    <#break >
                                <#case 2>
                                    <a href="/admin/order/${e.id}/logistics">更新物流</a>
                                    <#break>
                            </#switch>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>

                <nav aria-label="Page navigation" style="text-align: center;">
                    <ul class="pagination">
                    <#if elements.isFirst() != true >
                        <li>
                            <a href="/admin/order/index?page=${elements.previousPageable().pageNumber}&status=${status!}&keyword=${keyword!}"
                               aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </#if>

                    <#if elements.isLast() != true>
                        <li>
                            <a href="/admin/order/index?page=${elements.nextPageable().pageNumber}&status=${status!}&keyword=${keyword!}"
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

<#-- 拒绝审核的弹框 -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form id="commentForm" action="/order/{id}/change_status" method="post">
                <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                            aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">拒绝审核</h4>
                </div>
                <div class="modal-body">
                    <div class="form-group">
                        <label for="comment">拒绝审核原因：</label>
                        <input type="text" class="form-control" id="comment" placeholder="拒绝审核原因" name="comment">
                        <input type="hidden" name="name" value="fail">
                    </div
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
        $('.x-fail-check').click(function () {
            var id = $(this).attr('data-id');

            $('#commentForm').attr('action', '/admin/order/' + id + '/change_status')
        });
    })
</script>
</body>
</html>