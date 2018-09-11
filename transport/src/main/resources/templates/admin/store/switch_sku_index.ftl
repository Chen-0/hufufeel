<!DOCTYPE html>
<html lang="en">
<#assign title="换标记录" />
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

    <div class="row">
        <div class="col-xs-8 col-xs-offset-2">
            <table class="table table-bordered table-striped table-condensed table-hover">
                <thead>
                <tr>
                    <th>编号</th>
                    <th>客户名称</th>
                    <th>SKU</th>
                    <th>件数</th>
                    <th>装箱尺寸</th>
                    <th>状态</th>
                    <th>提交时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <#list elements as e>
                <tr>
                    <td>${e_index + 1}</td>
                    <td>${e.user.name} （NO.${e.user.hwcSn}）</td>
                    <td>${e.sku}</td>
                    <td>${e.qty}</td>
                    <td>${e.size}</td>
                    <td>${e.status.value}</td>
                    <td>${e.createdAt?string}</td>
                    <td>
                        <a href="/file/${e.doc.name}" download="${e.user.name}-NO.${e.user.hwcSn}换标文件.pdf">文件下载</a>
                        <a href="/admin/switch_sku/${e.id}/update">修改信息</a>

                        <a href="/admin/switch_sku/${e.id}/change_status?type=1">正在换标</a>
                        <#if e.qty?length gt 0>
                            <a href="/admin/switch_sku/${e.id}/change_status?type=2">换标成功</a>
                        </#if>
                    </td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
</body>
</html>
