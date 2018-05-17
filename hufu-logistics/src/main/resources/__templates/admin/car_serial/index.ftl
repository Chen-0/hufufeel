<!DOCTYPE html>
<html>

<#include "*/admin/_layout/head.ftl" />

<body class="theme-red">
<#include "*/admin/_layout/aside.ftl" />

<section class="content">
    <div class="container-fluid">
        <div class="block-header">
            <h2>
                车系列表
                <small>关于车系的列表</small>
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
                <a class="btn btn-primary" href="/admin/car/serial/create">
                    创建车系
                </a>
            </div>
            <div class="body table-responsive">
                <table class="table">
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>车系名称</th>
                        <th>所属品牌</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody>
                    <#list elements as item>
                    <tr>
                        <th scope="row">${item.id}</th>
                        <td>${item.name}</td>
                        <td>${item.carBrand.name}</td>
                        <td>
                            <a class="text-danger remove" href="/admin/car/serial/${item.id}/delete">删除</a>
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