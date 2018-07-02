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
                货品管理 （
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
                        <th>所属用户</th>
                        <th>类型</th>
                        <th>货品名称</th>
                        <th>货品SKU</th>
                        <th>电池类型</th>
                        <th>原产地</th>
                        <th>重量</th>
                        <th>体积 （立方米）</th>
                        <th>有效期</th>
                        <th>危险品</th>
                        <th>申报价值</th>
                        <th>申报名称</th>
                        <th>状态</th>
                        <#if status??>
                            <#if status == 2>
                                <th width="40">失败原因</th>
                            </#if>
                        </#if>
                        <th width="40">备注</th>
                        <th>操作</th>
                    </tr>
                    </thead>

                    <tbody>
                    <#list elements.getContent() as o>
                    <tr>
                        <td>${o.id}</td>
                        <td>${o.user.name} NO.${o.user.hwcSn}</td>
                        <td>${o.type.value}</td>
                        <td>${o.productName}</td>
                        <td>${o.productSku}</td>
                        <td>${o.isBattery?string("是", "否")}</td>
                        <td>${o.origin}</td>
                        <td>${o.weight}kg</td>
                        <td>${o.vol?string("0.########")} </td>
                        <td>
                            <#if o.deadline??>
                                ${o.deadline?date}
                            </#if>
                        </td>
                        <td>${o.isDanger?string("是", "否")}</td>
                        <td>${o.quotedPrice}</td>
                        <td>${o.quotedName}</td>
                        <td>${o.status.getValue()}</td>
                        <#if status??>
                            <#if status == 2>
                                <td>${o.reason!}</td>
                            </#if>
                        </#if>
                        <td>${o.comment!}</td>
                        <td>
                            <a href="/admin/product/${o.id}/show">查看</a>
                            <a href="/admin/product/${o.id}/update">修改</a>

                            <#if o.status.ordinal() == 0>
                                <a href="/admin/product/${o.id}/change_status?name=success">通过</a>
                                <a class="x-fail-check" href="javascript:void(0);" data-id="${o.id}" data-toggle="modal"
                                   data-target="#myModal">拒绝</a>
                            </#if>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>

                <nav aria-label="Page navigation" style="text-align: center;">
                    <ul class="pagination">
                    <#if elements.isFirst() != true >
                        <li>
                            <a href="/admin/product/index?page=${elements.previousPageable().pageNumber}&status=${status!}"
                               aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </#if>


                    <#if elements.isLast() != true>
                        <li>
                            <a href="/admin/product/index?page=${elements.nextPageable().pageNumber}&status=${status!}"
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
            <form id="commentForm" action="/product/{id}/change_status" method="post">
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

            $('#commentForm').attr('action', '/admin/product/' + id + '/change_status')
        });
    })
</script>
</body>
</html>