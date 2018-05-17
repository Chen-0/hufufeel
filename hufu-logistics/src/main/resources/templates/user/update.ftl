<!DOCTYPE html>
<html lang="en">
<#assign title="更新用户信息"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">更新用户信息</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">

            <form role="form" method="post" action="/user/${user.id}/update">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                <div class="form-group">
                    <label>用户名</label>
                    <input class="form-control" name="userna" value="${user.userna!}">
                </div>

                <div class="form-group">
                    <label>电子邮箱</label>
                    <input class="form-control" name="username" value="${user.username!}">
                </div>

                <div class="form-group">
                    <label>用户等级：</label>
                    <select class="form-control" name="role_id">
                    <#if (user.role_id == 1) >
                        <option value="1" selected>普通用户</option>
                    <#else >
                        <option value="1">普通用户</option>
                    </#if>

                    <#if (user.role_id == 2) >
                        <option value="2" selected>铜虎</option>
                    <#else >
                        <option value="2">铜虎</option>
                    </#if>

                    <#if (user.role_id == 3) >
                        <option value="3" selected>银虎</option>
                    <#else >
                        <option value="3">银虎</option>
                    </#if>

                    <#if (user.role_id == 4) >
                        <option value="4" selected>金虎</option>
                    <#else >
                        <option value="4">金虎</option>
                    </#if>

                    </select>
                </div>
                <div class="form-group">
                    <label>累计充值：</label>
                    <p>${user.total}</p>
                </div>
                <div class="form-group">
                    <label>余额：</label>
                    <p>${user.money}</p>
                </div>


                <div class="form-group">
                    <label>积分</label>
                    <input class="form-control" name="integral" value="${user.integral!}">
                </div>
                <div class="form-group">
                    <label>名（拼音）</label>
                    <input class="form-control" name="ming" value="${user.ming!}">
                </div>
                <div class="form-group">
                    <label>姓（拼音）</label>
                    <input class="form-control" name="xing" value="${user.xing!}">
                </div>
                <div class="form-group">
                    <label>联系地址</label>
                    <input class="form-control" name="address" value="${user.address!}">
                </div>
                <div class="form-group">
                    <label>邮政编码</label>
                    <input class="form-control" name="mail" value="${user.mail!}">
                </div>
                <div class="form-group">
                    <label>手机号码</label>
                    <input class="form-control" name="phone" value="${user.phone!}">
                </div>
                <div class="form-group">
                    <label>腾讯QQ</label>
                    <input class="form-control" name="qq" value="${user.qq!}">
                </div>
                <div class="form-group">
                    <label>备注</label>
                    <input class="form-control" name="beizhu" value="${user.beizhu!}">
                </div>

                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title">联系信息：</h3>
                    </div>
                    <div class="panel-body">
                    <#list user.addresses as o>
                        <#if o.verify == 'Ok'>
                        <table class="table table-bordered">
                            <tr>
                                <td>联系人：${o.userName}</td>
                                <td>联系电话：${o.mobile}</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td>身份证：${o.identity}</td>
                                <td>邮政：${o.zip}</td>
                                <td></td>
                            </tr>
                            <tr>
                                <td colspan="3">收货信息 ：${o.address}</td>
                            </tr>
                        </table>
                        </#if>
                    </#list>
                    </div>
                </div>


                <button type="submit" id="btn" class="btn btn-default">提交</button>
            </form>
        </div>
    </div>
</div>

<#include "/_layout/script.ftl"/>
</body>
</html>
