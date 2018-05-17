<!DOCTYPE html>
<html>

<#include "*/admin/_layout/head.ftl" />

<body class="theme-red">
<#include "*/admin/_layout/aside.ftl" />

<section class="content">
    <div class="container-fluid">
        <div class="block-header">
            <h2>
                车型列表
                <small>车型，简单的理解就是具体的车系</small>
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
                <a class="btn btn-primary" href="/admin/car/model/create">创建车型</a>
            </div>
            <div class="body table-responsive">
                <table class="table">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>品牌</th>
                        <th>车系</th>
                        <th>车型</th>
                        <th>厂家指导价 （元）</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list elements as item>
                    <tr>
                        <th scope="row">${item.id}</th>
                        <td>${item.carSerial.carBrand.name}</td>
                        <td>${item.carSerial.name}</td>
                        <td>${item.name}</td>
                        <td>${item.price}</td>
                        <td>
                            <a class="text-danger remove" href="/admin/car/model/${item.id}/delete">删除</a>
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
</body>

</html>