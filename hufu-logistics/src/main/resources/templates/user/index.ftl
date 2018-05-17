<!DOCTYPE html>
<html lang="en">
<#assign title="海淘客运单列表" />
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
                会员中心
            </h1>
        </div>
    </div>

<#if success?exists && (success?length > 0) >
    <div class="alert alert-success alert-dismissable alert-message">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
    ${success}
    </div>
</#if>

    <div class="row">
        <div class="col-md-12">
            <div style="text-align: center; margin-bottom: 1.5rem;">
                <form class="form-horizontal" method="get" action="/user/index">
                <#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">-->
                    <div class="form-group">
                        <label for="search-keyword" class="col-sm-4 control-label">查询：</label>
                        <div class="col-sm-4">
                            <input type="text" id="search-keyword" name="keyword" class="form-control"
                                   placeholder="客户标识、用户名、邮箱" value="${keyword!}">
                        </div>
                    </div>

                    <div>
                        <button type="submit" class="btn btn-default" style="margin-right: 15px;">查询</button>
                        <a href="/user/index" class="btn btn-default">重置</a>
                    </div>
                </form>
            </div>


            <div class="table-responsive">
                <form id="selectForm" action="/waybill/select" method="get">
                    <table class="table table-bordered table-hover table-condensed">
                        <thead>
                        <tr>
                            <td>#</td>
                            <td>用户名</td>
                            <th>邮箱</th>
                            <td>等级</td>
                            <th>余额</th>
                            <td>累计充值</td>
                            <td>客户标示</td>
                            <td>删除</td>
                        </tr>
                        </thead>

                        <tbody>
                        <#list users.getContent() as o>
                        <tr>
                            <td>${o.id}</td>
                            <td>${o.userna}</td>
                            <td><a href="/user/${o.id}/update">${o.username}</a></td>
                            <td>${o.role.name}</td>
                            <td>${o.money}</td>
                            <td>${o.total}</td>
                            <td>${o.customer}</td>
                            <td><a href="#" data-id="destroy" data-value="${o.id}">删除</a></td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </form>

                <nav aria-label="Page navigation" style="text-align: center;">
                    <ul class="pagination">
                    <#if users.isFirst() != true >
                        <li>
                            <a href="/user/index?keyword=${keyword!}&page=${users.previousPageable().pageNumber}"
                               aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </#if>


                    <#if users.isLast() != true>
                        <li>
                            <a href="/user/index?keyword=${keyword!}&page=${users.nextPageable().pageNumber}"
                               aria-label="Previous">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </#if>
                    </ul>

                    <p>
                        当前第${users.getNumber() + 1}页，一共${users.getTotalPages()}页，一共${users.getTotalElements()}条数据</p>
                </nav>
            </div>
        </div>
    </div>
</div>

<form method="post" action="" id="mainForm">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
</form>

<#include "/_layout/script.ftl"/>
<script>
    $(function () {
        $('a[data-id=destroy]').click(function (e) {
            e.preventDefault();

            if (!confirm('确定删除该用户！')) {
                return 0;
            }

            var id = $(this).attr('data-value');

            $('#mainForm').attr('action', '/user/' + id + '/destroy');

            $('#mainForm').submit();

        });
    });
</script>
</body>
</html>