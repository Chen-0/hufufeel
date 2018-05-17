<!DOCTYPE html>
<html lang="en">
<#assign title="月客户数据统计表" />
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
                月客户数据统计表（${year!}年${month}月）
            </h1>
        </div>
    </div>

    <div class="row" style="margin-bottom: 48px;">
        <div class="col-md-12" style="padding: 25px 50px;">
            <div style="text-align: center; margin-bottom: 3rem;">
                <form class="form-horizontal" method="get" action="/analysis/user/paymoney">
                <#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">-->

                    <div class="form-group">
                        <label for="time" class="col-sm-4 control-label">时间：</label>
                        <div class="col-sm-4">
                            <input type="text" id="time" name="time" class="form-control" placeholder="时间筛选" value="${time}">
                        </div>
                    </div>

                    <div>
                        <button type="submit" class="btn btn-default" style="margin-right: 15px;">查询</button>
                        <a href="/analysis/user/paymoney" class="btn btn-default">重置</a>
                    </div>
                </form>
            </div>


            <div class="table-responsive">
                <form id="selectForm" action="/waybill/select" method="get">
                    <table class="dataTable display" id="mainTable" cellspacing="0" width="100%" role="grid"
                           aria-describedby="example_info" style="width: 100%;">
                        <thead>
                        <tr>
                            <td>用户名</td>
                            <td>客户标示</td>
                            <td>等级</td>
                            <th>余额</th>
                            <td class="text-danger">本月充值</td>
                            <td class="text-success">本月消费</td>
                            <td>新增运单数</td>
                            <td>查看</td>
                        </tr>
                        </thead>

                        <tbody>
                        <#list users as o>
                            <#if o.user?exists>
                            <tr>
                                <td>${o.user.userna}</td>
                                <td>${o.user.customer}</td>
                                <td>${o.user.role.name}</td>
                                <td>${o.user.money}</td>
                                <td class="text-danger">${o.in}</td>
                                <td class="text-success">${o.out}</td>
                                <td>${o.totalWaybill}</td>
                                <td><a href="/analysis/user/paymoney/${o.user.id}?time=${time}">查看</a></td>
                            </tr>
                            </#if>
                        </#list>
                        </tbody>
                    </table>
                </form>
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
        $('#mainTable').DataTable({
            "order": [[4, "desc"]]
        });

        $('#time').datetimepicker({
            viewMode: 'years',
            format: 'MM/YYYY'
        });

    });
</script>
</body>
</html>