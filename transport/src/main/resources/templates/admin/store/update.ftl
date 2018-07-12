<!DOCTYPE html>
<html lang="en">
<#assign title="修改库存"/>
<#include "*/admin/_layout/head.ftl" />

<body>
<#include "*/admin/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">${title}</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">
            <form role="form" method="post" action="/admin/stock/${ele.id}/update">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">${title}</h3>
                    </div>
                    <div class="panel-body">
                        <h3>商品：${ele.product.productName}， 仓库：${ele.warehouse.name}</h3>
                        <@formGroup label="库存" name="quantity"></@formGroup>
                        <@formGroup label="位置" name="location"></@formGroup>
                    </div>

                    <div class="panel-footer clearfix">
                        <button type="submit" class="btn btn-default" id="btn">提交</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>


<#include "*/admin/_layout/script.ftl"/>
</body>
</html>