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
        </ol>

        <div class="card">
            <div class="header">
                <h2>筛选</h2>
            </div>
            <div class="body">
                <form action="/article/">
                    <label for="keyword">根据标题搜索：</label>
                    <div class="form-group">
                        <div class="form-line">
                            <input type="text" id="keyword" class="form-control" placeholder="比亚迪" name="keyword" value="${keyword}">
                        </div>
                    </div>

                    <div class="demo-checkbox">
                    <#list tags as tag>
                        <#assign flag = false />

                        <#list ids as id>
                            <#if tag.id == id>
                                <#assign flag = true />
                            </#if>
                        </#list>

                        <#if flag>
                            <input type="checkbox" id="tag_${tag.id}" class="filled-in" name="tags[]" value="${tag.id}" checked>
                            <label for="tag_${tag.id}">${tag.name}</label>
                        <#else>
                            <input type="checkbox" id="tag_${tag.id}" class="filled-in" name="tags[]" value="${tag.id}">
                            <label for="tag_${tag.id}">${tag.name}</label>
                        </#if>

                    </#list>
                    </div>

                    <button type="submit" class="btn btn-primary m-t-15 waves-effect">搜索</button>
                    <a href="/article/" class="btn btn-primary m-t-15 waves-effect">重置</a>
                </form>
            </div>
        </div>

        <div class="card">
            <div class="header">
                <h2 style="text-align: center;">
                    热门文章
                </h2>

            </div>
            <div class="body table-responsive">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>文章标题</th>
                        <th>文章作者</th>
                        <th>标签分类</th>
                        <td>发布时间</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#list articles.getContent() as article>
                    <tr>
                        <th scope="row">${article.id}</th>
                        <td><a href="/article/${article.id}">${article.title}</a></td>
                        <td>${article.author}</td>
                        <td>
                            <#list article.tags as tag >
                                <span class="label label-warning">${tag.name}</span>
                            </#list>
                        </td>
                        <td>${article.createdAt}</td>
                    </tr>
                    </#list>
                    </tbody>
                </table>

            <#if articles.hasPrevious() >
                <a href="/article/?page=${articles.number - 1}" class="btn btn-default waves-effect">上一页</a>
            </#if>
            <#if articles.hasNext() >
                <a href="/article/?page=${articles.number + 1}" class="btn btn-default waves-effect">下一页</a>
            </#if>
            </div>
        </div>
    </div>
</section>

<#include "*/_layout/script.ftl" />
</body>
</html>
