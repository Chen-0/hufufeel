<!DOCTYPE html>
<html lang="en">
<#assign title="货物入库"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">货物入库</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">
            <form role="form" method="post" action="/waybill/${waybill.id}/in" id="main">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">美国快递公司快递单信息</h3>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th>入库仓库</th>
                                <th>快递公司</th>
                                <th>快递单号</th>
                                <th>客户标示</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>${waybill.agea}</td>
                                <td>${waybill.express}</td>
                                <td>${waybill.expressnum}</td>
                                <td>${waybill.customer}</td>
                            </tr>
                            </tbody>
                        </table>

                        <div class="form-group">
                            <label for="weight">入库重（必填）：</label>
                            <input type="text" class="form-control" id="weight" name="weight">
                        </div>
                        <div class="form-group">
                            <label for="storageno">仓储位（必填）：</label>
                            <input type="text" class="form-control" id="storageno" name="storageNo">
                        </div>
                        <div class="form-group">
                            <label for="extraCost">额外收费：</label>
                            <input type="text" class="form-control" id="extraCost" name="extraCost" value="0">
                        </div>
                        <div class="form-group">
                            <label for="extraCostReason">额外收费原因：</label>
                            <input type="text" class="form-control" id="extraCostReason" name="extraCostReason">
                        </div>
                    </div>
                </div>

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">货物信息</h3>
                    </div>
                    <div class="panel-body">
                        <table class="table">
                            <thead>
                            <tr>
                                <td width="18%">货物分类（必填）：</td>
                                <td width="18%">货物子分类（必填）：</td>
                                <td width="18%">英文品牌名：</td>
                                <td width="18%">货物明细：</td>
                                <td width="18%">数 量：</td>
                            </tr>
                            </thead>
                            <tbody id="container">
                            <#list waybill.goods as goods>
                            <tr>
                                <td>${goods.gooda}</td>
                                <td>${goods.goodb}</td>
                                <td>${goods.brandname}</td>
                                <td>${goods.goodname}</td>
                                <td>${goods.contnum}</td>
                            </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                </div>

                <button type="submit" class="btn btn-success" id="btn">入库</button>
            </form>
        </div>
    </div>
</div>


<#include "/_layout/script.ftl"/>
</body>
</html>