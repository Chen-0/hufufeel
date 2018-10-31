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
                费用流水
            </h1>
        </div>
    </div>

<#if success?? >
    <div class="alert alert-success alert-dismissable alert-message">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
    ${success}
    </div>
</#if>

    <div class="row" style="padding-bottom: 50px;">
        <div class="col-xs-12">
            <div class="box">

                <div class="box-body">

                    <form class="form-horizontal" action="/admin/user/statements/index" method="get">
                        <div class="form-group">
                            <label for="" class="col-xs-1 control-label">筛选：</label>
                        </div>
                        <div class="form-group">
                            <label for="startAt" class="col-xs-1 control-label">客户</label>
                            <div class="col-xs-3">
                                <select name="userId" id="userId" class="form-control">
                                    <option value="">所有</option>
                                    <#list users as u>
                                        <#if userId?exists && userId == u.id>
                                            <option value="${u.id}" selected>${u.name}</option>
                                        <#else>
                                            <option value="${u.id}">${u.name}</option>
                                        </#if>

                                    </#list>
                                </select>
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
                                <a href="/admin/user/statements/index" class="btn btn-default">重置</a>
                            </div>
                        </div>
                    </form>


                    <table class="table table-bordered table-striped table-condensed table-hover">
                        <thead>
                        <tr>
                            <th>编号</th>
                            <th>客户</th>
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
                            <td>${e.user.name} (NO.${e.user.hwcSn})</td>
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
                                <#switch e.type.ordinal()>
                                    <#case 0>
                                        <a href="/admin/package/${e.target}/show">详情</a>
                                        <#break>
                                    <#case 1>
                                        <a href="/admin/package/${e.target}/show">详情</a>
                                        <#break>
                                    <#case 2>
                                        <a href="/admin/order/${e.target}/show">详情</a>
                                        <#break>
                                </#switch>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>

                <div class="box-footer clearfix">
                    <a href="/admin/user/statements/export?startAt=${startAt!}&endAt=${endAt!}&userId=${userId!}" class="btn btn-primary" target="_blank" download="HUFU-管理后台-费用明细.xlsx">导出</a>
                    <nav aria-label="Page navigation" style="text-align: center;">
                        <ul class="pagination">
                        <#if elements.isFirst() != true >
                            <li>
                                <a href="/admin/user/statements/index?page=${elements.previousPageable().pageNumber}&startAt=${startAt!}&endAt=${endAt!}&userId=${userId!}"
                                   aria-label="Previous">
                                    <span aria-hidden="true">&laquo;</span>
                                </a>
                            </li>
                        </#if>

                        <#if elements.isLast() != true>
                            <li>
                                <a href="/admin/user/statements/index?page=${elements.nextPageable().pageNumber}&startAt=${startAt!}&endAt=${endAt!}&userId=${userId!}"
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
</div>

<#include "*/admin/_layout/script.ftl"/>
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