<!DOCTYPE html>
<html>

<#include "*/admin/_layout/head.ftl" />

<body class="theme-red">
<#include "*/admin/_layout/aside.ftl" />

<section class="content">
    <div class="container-fluid">
        <div class="block-header">
            <h2>
                文章列表
                <small>爬虫自动获取的文章将会在这里，爬虫获取的文章状态为未上架，请把合适的文章上架</small>
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
            </div>
            <div class="body table-responsive">
                <table class="table">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>文章标题</th>
                        <th>作者</th>
                        <th>状态</th>
                        <th>文章创建时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list articles.getContent() as article>
                    <tr>
                        <th scope="row">${article.id}</th>
                        <td>${article.title}</td>
                        <td>${article.author}</td>
                        <td>${(article.status == 1) ? string('已上架', '未上架')}</td>
                        <td>${article.createdAt?string}</td>
                        <td>
                            <#if article.status == 1>
                                <a href="/admin/article/${article.id}/changeStatus?status=0">下架</a>
                            </#if>
                            <#if article.status == 0>
                                <a href="/admin/article/${article.id}/changeStatus?status=1">上架</a>
                            </#if>
                            <a class="text-danger remove" href="/admin/article/${article.id}/delete">删除</a>
                        </td>
                    </tr>
                    </#list>
                    </tbody>
                </table>

            <#if articles.hasPrevious() >
                <a href="/admin/article/index?page=${articles.number - 1}" class="btn btn-default waves-effect">上一页</a>
            </#if>
            <#if articles.hasNext() >
                <a href="/admin/article/index?page=${articles.number + 1}" class="btn btn-default waves-effect">下一页</a>
            </#if>
            </div>
        </div>
    </div>
</section>

<#include "*/admin/_layout/script.ftl" />
</body>

</html>