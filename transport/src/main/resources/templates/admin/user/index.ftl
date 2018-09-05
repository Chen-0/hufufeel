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
                用户管理
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

            <div class="row">
                <div class="col-xs-6 col-xs-offset-3">
                    <form class=" margin-bottom" role="form" method="get" action="/admin/user/index">
                        <div class="form-group">
                            <label for="keyword">关键字（用户名称，编号模糊搜索）：</label>
                            <input class="form-control" id="keyword" name="keyword" type="text" value="${keyword!}">
                        </div>

                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">搜索</button>
                            <a href="/admin/user/index" class="btn btn-default">重置</a>
                        </div>
                    </form>
                </div>
            </div>


            <div class="table-responsive" style="margin-top: 25px;">
                <form method="get" action="/admin/user/store_cost" id="main">
                    <table class="bordered">
                        <thead>
                        <tr>
                            <th width="1px"><input type="checkbox" id="select_all"></th>
                            <th>#</th>
                            <th>账号</th>
                            <th>用户名</th>
                            <th>客户编号</th>
                            <th>余额</th>
                            <th>状态</th>
                            <th>操作</th>
                        </tr>
                        </thead>

                        <tbody>
                        <#list elements as o>
                        <tr>
                            <th><input class="x-checkbox" type="checkbox" name="ids[]" value="${o.id}"></th>
                            <td>${o.id}</td>
                            <td>${o.username}</td>
                            <td>${o.name}</td>
                            <td>${o.hwcSn}</td>
                            <td>${o.usd} usd</td>
                            <td>${o.arrearage?string("冻结", "正常")}</td>
                            <td>
                                <a href="/admin/user/${o.id}/cost_subject">用户设置</a>
                                <a href="/admin/user/${o.id}/password">修改密码</a>
                                <a href="/admin/user/${o.id}/charge">修改余额</a>
                                <a href="/admin/user/${o.id}/recharge">补收费</a>
                            </td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>

                    <div style="margin-top: 20px">
                        <button class="btn btn-primary margin" id="submit">收仓租费</button>
                        <a class="btn btn-primary margin" href="/admin/user/create">新建用户</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
<script>
    $(function () {
        $('#select_all').click(function () {
            if ($(this).is(":checked")) {
                changeSelect(true);
            } else {
                changeSelect(false);
            }
        });

        function changeSelect(value) {
            $('.x-checkbox').prop("checked", value);
        }

        $("#main").submit(function () {

            if ($(".x-checkbox:checked").length === 0) {
                alert("请选择用户");
                return false;
            }

            return true;
        });
    });
</script>
</body>
</html>