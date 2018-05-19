<!DOCTYPE html>
<html>

<#include "*/admin/_layout/head.ftl" />

<body class="theme-red">
<#include "*/admin/_layout/aside.ftl" />

<section class="content">
    <div class="container-fluid">
        <div class="block-header">
            <h2>
                创建文章标签
                <small>爬虫自动获取文章，会根据文章的标题和文章的内容自动匹配标签</small>
            </h2>
        </div>
    </div>

    <div class="card">

        <div class="body">
            <form method="post" action="/admin/tag/create">
                <label for="name">标签名称：（一次可填写多个标签用空格分割）</label>
                <div class="form-group">
                    <div class="form-line">
                        <input type="text" id="name" class="form-control" placeholder="标签名称，例如：东风日产，超跑" name="name">
                    </div>
                </div>
                <button type="submit" class="btn btn-primary m-t-15 waves-effect">提交</button>
            </form>
        </div>
    </div>
</section>

<#include "*/admin/_layout/script.ftl" />
</body>

</html>