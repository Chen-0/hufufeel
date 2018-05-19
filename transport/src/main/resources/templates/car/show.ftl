<!DOCTYPE html>
<html>

<#include "*/_layout/head.ftl" />

<body class="theme-blue">
<#include "*/_layout/aside.ftl" />

<section style="margin-top: 80px;">
    <div class="container">
        <ol class="breadcrumb breadcrumb-col-cyan">
            <li><a href="/"><i class="material-icons">home</i> 首页</a></li>
            <li><a href="/car/index"><i class="material-icons">library_books</i> 热门车型</a></li>
            <li class="active"><i class="material-icons">archive</i> ${carSerial.name}</li>
        </ol>

        <div class="card">
            <div class="header">
                <h2 style="text-align: center;">
                ${carSerial.name} 的热门车型：
                </h2>
            </div>

            <div class="body">
                <div class="list-group">
                <#list carSerial.carModels as carModel>
                    <div class="list-group-item">
                        <a href="/car/model/${carModel.id}">${carModel.name}</a>
                        <a href="/car/model/${carModel.id}/addDiff" style="float: right; margin-left: 10px;">加入对比</a>
                        <span style="float: right;" class="text-danger">厂家指导价：${carModel.price} ￥</span>
                    </div>
                </#list>
                </div>
            </div>
        </div>

    <#if (container?size > 0) >
        <div class="card">
            <div class="header">
                <h2 style="text-align: center;">
                    车型对比
                </h2>
            </div>

            <div class="body">
                <div class="list-group">
                    <#list container as carModel>
                        <div class="list-group-item">
                            <a href="/car/model/${carModel.id}">${carModel.name}</a>
                            <a href="/car/model/${carModel.id}/removeDiff"
                               style="float: right; margin-left: 10px;">去除</a>
                            <span style="float: right;" class="text-danger">厂家指导价：${carModel.price} ￥</span>
                        </div>
                    </#list>
                </div>

                <#if (container?size == 2) >
                <div class="text-center">
                    <a href="/car/diff" class="btn btn-primary">去对比</a>
                </div>
                </#if>
            </div>
        </div>
    </#if>
    </div>
</section>

<#include "*/_layout/script.ftl" />
</body>

</html>