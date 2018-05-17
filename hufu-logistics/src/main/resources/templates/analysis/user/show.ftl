<!DOCTYPE html>
<html lang="en">
<#assign title="充值支出明细" />
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
                充值支出明细
            </h1>
        </div>
    </div>

<#if success?exists && (success?length > 0) >
    <div class="alert alert-success alert-dismissable alert-message">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
    ${success}
    </div>
</#if>
<#assign sum1=0>
<#assign sum2=0>
    <div class="row">
        <div class="col-md-12" style="padding: 25px 50px;">

            <h3>充值</h3>
            <div class="table-responsive">
                <table class="table table-bordered table-hover table-condensed">
                    <thead>
                    <tr>
                        <td>#</td>
                        <td>用户名</td>
                        <td>等级</td>
                        <td>充值金额</td>
                        <td>时间</td>
                    </tr>
                    </thead>

                    <tbody>
                    <#list paymonies as o>
                    <tr>
                        <td>${o.id}</td>
                        <td>${o.user.userna}</td>
                        <td>${o.user.role.name}</td>
                        <td>${o.money}</td>
                        <td>${o.time?string}</td>
                    </tr>
                        <#assign sum1 = sum1 + o.money/>
                    </#list>
                    </tbody>
                </table>
                总计：${sum1}
            </div>


            <h3>支出</h3>
            <div class="table-responsive">
                <table class="table table-bordered table-hover table-condensed">
                    <thead>
                    <tr>
                        <td>#</td>
                        <td>用户名</td>
                        <td>等级</td>
                        <td>HUFU 单号</td>
                        <td>支付类型</td>
                        <td>金额</td>
                        <td>时间</td>
                    </tr>
                    </thead>

                    <tbody>
                    <#list finances as o>
                        <#if (o.cost > 0) >
                        <tr>
                            <td>${o.id}</td>
                            <td>${o.user.userna}</td>
                            <td>${o.user.role.name}</td>
                            <td>${o.trackingNumber}</td>
                            <td>${o.type}</td>
                            <td>${o.cost}</td>
                            <td>${o.time?string}</td>
                        </tr>
                            <#assign sum2 = sum2 + o.cost />
                        </#if>
                    </#list>
                    </tbody>
                </table>
                总计：${sum2}
            </div>
        </div>
    </div>
</div>

<#include "/_layout/script.ftl"/>
</body>
</html>