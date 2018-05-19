<!DOCTYPE html>
<html>

<#include "*/_layout/head.ftl" />

<body class="theme-blue">
<#include "*/_layout/aside.ftl" />

<section style="margin-top: 80px;">
    <div class="container">
        <ol class="breadcrumb breadcrumb-col-cyan">
            <li><a href="/"><i class="material-icons">home</i> 首页</a></li>
            <li><a href="/article/"><i class="material-icons">library_books</i> 文章列表</a></li>
            <li class="active"><i class="material-icons">archive</i> ${article.title}</li>
        </ol>

        <div class="card">
            <div class="header">
                <h2 style="text-align: center;">
                    ${article.title}
                </h2>

            </div>
            <div class="body">
                <div style="text-align: center;">
                <span>标签分类：</span>
                <#list article.tags as tag >
                <span class="label label-warning">${tag.name}</span>
                </#list>
                <hr>
                创建时间：${article.createdAt}，作者：${article.author}
                <hr>
                </div>
                ${article.content}
            </div>
        </div>
    </div>
</section>

<#include "*/_layout/script.ftl" />
</body>

</html>