<!DOCTYPE html>
<html lang="en">
<#assign title="创建运单"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">创建运单</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">

            <form role="form" method="post" action="/order/create" id="main">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <input type="hidden" name="companyId" value="${USER.id}">

                <div class="form-group">
                    <label>运单号：</label>
                    <input class="form-control" name="trackingNumber" value="">
                    <p class="text-danger hide">运单号不能为空</p>
                </div>

                <div class="form-group">
                    <label>发件人</label>
                    <input class="form-control" name="sender" value="">
                    <p class="text-danger hide">发件人不能为空</p>
                </div>

                <div class="form-group">
                    <label>收件人</label>
                    <input class="form-control" name="contact" value="">
                    <p class="text-danger hide">收件人不能为空</p>
                </div>

                <div class="form-group">
                    <label>收件人身份证号</label>
                    <input class="form-control" name="identity" value="">
                    <p class="text-danger hide">收件人身份证号不能为空</p>
                </div>


                <div class="form-group">
                    <label>收件人手机</label>
                    <input id="phone" class="form-control" name="phone" value="">
                    <p class="text-danger hide">收件人手机为11位的数字</p>
                </div>


                <div class="form-group">
                    <label>收件人地址</label>
                    <input class="form-control" name="address" value="">
                    <p class="text-danger hide">收件人地址不能为空</p>
                </div>

                <div class="form-group">
                    <label>邮编</label>
                    <input class="form-control" name="zipCode" value="">
                    <p class="text-danger hide">邮编不能为空</p>
                </div>

                <div class="form-group">
                    <label>商品名称</label>
                    <input class="form-control" name="goodsName" value="">
                    <p class="text-danger hide">商品名称不能为空</p>
                </div>
                <div class="form-group">
                    <label>数量</label>
                    <input class="form-control" name="quantity" value="">
                    <p class="text-danger hide">数量不能为空</p>
                </div>
                <div class="form-group">
                    <label>入库重量</label>
                    <input class="form-control" name="inWeight" value="">
                    <p class="text-danger hide">入库重量不能为空</p>
                </div>
                <div class="form-group">
                    <label>申报价格</label>
                    <input class="form-control" name="declared" value="">
                    <p class="text-danger hide">申报价格不能为空</p>
                </div>

                <div class="form-group">
                    <label>保额</label>
                    <input class="form-control" name="insurance">
                </div>

                <button type="submit" class="btn btn-default" id="btn">提交</button>
            </form>


        </div>
    </div>
</div>

<#include "/_layout/script.ftl"/>
<#--<script>-->
    <#--$(function () {-->
        <#--$('#btn').click(function (event) {-->
            <#--event.preventDefault();-->

            <#--var flag = true;-->
            <#--$('input').each(function () {-->
                <#--var v = $(this).val();-->
                <#--if (v == null || v == '') {-->
                    <#--flag = false;-->
                    <#--$(this).next().removeClass('hide');-->
                <#--} else {-->
                    <#--$(this).next().addClass('hide');-->
                <#--}-->
            <#--});-->
            <#--var phone = $('#phone').val();-->
            <#--if (phone.length != 11 || isNaN(Number(phone))) {-->
                <#--flag = false;-->
                <#--$('#phone').next().removeClass('hide');-->
            <#--} else {-->
                <#--$('#phone').next().addClass('hide');-->
            <#--}-->

            <#--if (flag) {-->
                <#--$('#main').submit();-->
            <#--}-->
        <#--});-->
    <#--})-->

<#--</script>-->
</body>
</html>
