<!DOCTYPE html>
<html lang="en">
<#assign title="待审核的地址" />
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
                待审核的地址
            </h1>
        </div>
    </div>

<#if success?exists && (success?length > 0) >
    <div class="alert alert-success alert-dismissable alert-message">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
    ${success}
    </div>
</#if>

    <div class="row">
        <div class="col-md-12">

            <div class="table-responsive">
                <table class="table table-bordered table-hover table-condensed">
                    <thead>
                    <tr>
                        <th>用户名</th>
                        <th>邮箱</th>
                        <th>客户标识</th>
                        <th>收货人</th>
                        <th>电话</th>
                        <th>收货地址</th>
                        <th>邮编</th>
                        <th>身份证</th>
                        <th>查看照片</th>
                        <th>核审状态</th>
                        <th>核审通过</th>
                    </tr>
                    </thead>

                    <tbody>
                    <#list addresses as o>
                        <#if o.user?exists>
                        <tr>
                            <td>${o.user.userna}</td>
                            <td>${o.user.username}</td>
                            <td>${o.user.customer}</td>
                            <td>${o.userName}</td>
                            <td>${o.mobile}</td>
                            <td>${o.address}</td>
                            <td>${o.zip}</td>
                            <td>${o.identity}</td>
                            <td>
                                <a href="#" data-id="photos" data-img1="${o.identityAmga}"
                                   data-img2="${o.identityAmgb}">查看</a>
                            </td>
                            <td>
                            ${o.verify}
                            </td>
                            <td>
                                <a data-id="pass" href="#" data-value="${o.id}">通过</a>
                            </td>
                        <#--<td>${o.}</td>-->
                        </tr>
                        </#if>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" tabindex="-1" role="dialog" id="myModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">查看照片</h4>
            </div>
            <div class="modal-body" id="main">
                <img src="" class="img-responsive" id="img1">
                <img src="" class="img-responsive" id="img2">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>

<form action="/user/address/pass" method="post" id="mainForm">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="id" value="" id="addressId">
</form>

<#include "/_layout/script.ftl"/>
<script>
    $(function () {
        $('a[data-id=photos]').click(function (e) {
            e.preventDefault();

            var img1 = 'http://www.hufufeel.com' + $(this).attr('data-img1');
            var img2 = 'http://www.hufufeel.com' + $(this).attr('data-img2');

            $('#img1').attr('src', img1);
            $('#img2').attr('src', img2);

            $('#myModal').modal('show');
        });

        $('a[data-id=pass]').click(function (e) {
            e.preventDefault();

            if (!confirm("通过审核？")) {
                return 0;
            }
            var value = $(this).attr('data-value');

            $('#addressId').val(value);

            $('#mainForm').submit();
        });
    })
</script>
</body>
</html>