<!DOCTYPE html>
<html lang="en">
<#assign title="公告管理" />
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

    <div class="row" style="padding-bottom: 50px;">
        <div class="col-xs-12">

            <div style="margin: 15px;">
                <a href="/admin/notice/create" class="btn btn-primary">添加公告</a>
            </div>

            <div class="table-responsive">
                <table class="bordered">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>标题</th>
                        <th>内容</th>
                        <th>时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>

                    <tbody>
                    <#list elements as o>
                    <tr>
                        <td>${o.id}</td>
                        <td>${o.title}</td>
                        <td>
                            <#if o.context?length gt 20>
                                ${o.context?substring(0, 20)}
                                <#else>
                            ${o.context}
                            </#if>
                        </td>
                        <td>${o.createdAt?date}</td>
                        <td>
                            <a class="x-remove" href="/admin/notice/${o.id}/remove">删除</a>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
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