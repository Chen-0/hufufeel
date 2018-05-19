<!DOCTYPE html>
<html>

<#include "*/admin/_layout/head.ftl" />

<body class="theme-red">
<#include "*/admin/_layout/aside.ftl" />

<section class="content">
    <div class="container-fluid">
        <div class="block-header">
            <h2>
                文章标签列表
                <small>爬虫自动获取文章，会根据文章的标题和文章的内容自动匹配标签</small>
            </h2>
        </div>
    </div>

    <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
        <div class="card">
            <div class="header">
            <#if message??>
                <div class="alert alert-success">
                    <strong>${message}</strong>
                </div>
            </#if>
                <a class="btn btn-primary" href="/admin/tag/create">创建新标签</a>
            </div>
            <div class="body table-responsive">
                <table class="table">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>标签名称</th>
                        <th>标签创建时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list elements as item>
                    <tr>
                        <th scope="row">${item.id}</th>
                        <td>${item.name}</td>
                        <td>${item.createdAt?string}</td>
                        <td>
                            <a class="text-danger remove" href="/admin/tag/${item.id}/delete">删除</a>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</section>

<#include "*/admin/_layout/script.ftl" />
<script>
    $(function () {
        $(".remove").click(function (e) {
            e.preventDefault();

            var url = $(this).attr("href");

            if (confirm("确认删除该标签")) {
                window.location.href = url;
            }
        });
    })
</script>
</body>

</html>