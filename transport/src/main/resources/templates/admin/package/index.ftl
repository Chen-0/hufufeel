<!DOCTYPE html>
<html lang="en">
<#assign title="入库单 & 退货单管理" />
<#include "*/admin/_layout/head.ftl" />

<body>
<#include "*/admin/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
            ${title} （
            <#if status??>
                <#switch status>
                    <#case 0> 待入库 <#break>
                    <#case 1> 已收货 <#break>
                    <#case 2> 已上架 <#break>
                    <#case 3> 已取消 <#break>
                    <#case 4> 已冻结 <#break>
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
        <div class="col-xs-8 col-xs-offset-2">
            <form class=" margin-bottom" role="form" method="get" action="/admin/package/index">
                <div class="row">
                    <div class="form-group col-xs-5">
                        <label for="keyword">模糊搜索：（用户名，参考单号，入库单号，客户编号）</label>
                        <input class="form-control" id="keyword" name="keyword" type="text" value="${keyword!}">
                    </div>
                </div>

                <div class="form-group">
                    <button type="submit" class="btn btn-primary">搜索</button>
                    <a href="/admin/package/index" class="btn btn-default">重置</a>
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
                        <th>#</th>
                        <th>参考号</th>
                        <th>收件人</th>
                        <th>客户</th>
                        <th>仓库</th>
                        <th>类型</th>
                        <th>状态</th>
                        <th>提交时间</th>
                        <th>备注</th>
                        <th>操作</th>
                    </tr>
                    </thead>

                    <tbody>
                    <#list elements.getContent() as o>
                    <tr>
                        <td><a href="/admin/package/${o.id}/show">${o.cn}</a></td>
                        <td>${o.referenceNumber}</td>
                        <td>${o.contact}</td>
                        <td>${o.nickname} NO.${o.user.hwcSn}</td>
                        <td>${o.warehouseName}</td>
                        <td>${o.type.value}</td>
                        <td>${o.status.value}</td>
                        <td>${o.createdAt?string}</td>
                        <td>${o.comment!}</td>
                        <td>
                            <a href="/admin/package/${o.id}/show">详情</a>
                            <#switch o.status.ordinal()>
                                <#case 0>
                                    <a href="/admin/package/${o.id}/inbound">入库</a>
                                    <#break>
                                <#case 1>
                                    <a href="/admin/package/${o.id}/publish">上架</a>
                                    <#break >
                            </#switch>
                            <a class="x-remove" href="/admin/package/${o.id}/remove">删除</a>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>

                <nav aria-label="Page navigation" style="text-align: center;">
                    <ul class="pagination">
                    <#if elements.isFirst() != true >
                        <li>
                            <a href="/admin/package/index?page=${elements.previousPageable().pageNumber}&status=${status!}&keyword=${keyword!}"
                               aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </#if>


                    <#if elements.isLast() != true>
                        <li>
                            <a href="/admin/package/index?page=${elements.nextPageable().pageNumber}&status=${status!}&keyword=${keyword!}"
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

<#include "*/admin/_layout/script.ftl"/>
<script>
    $('.x-remove').click(function (e) {
        e.preventDefault();

        var url = $(this).attr("href");

        if (confirm("确实删除该条目")) {
            window.location.href = url;
        }
    });
</script>
</body>
</html>