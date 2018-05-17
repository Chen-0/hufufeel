<!DOCTYPE html>
<html lang="en">
<#assign title="更新运单信息"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid" style="padding-bottom: 48px;">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">更新运单信息</h1>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">

            <form role="form" method="post" action="/order/${order.id}/update">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                <div class="form-group">
                    <label>批次</label>
                    <input class="form-control" name="batch" value="${order.batch!}">
                </div>
                <div class="form-group">
                    <label>运单号：</label>
                    <input class="form-control" name="tracking_number" value="${order.trackingNumber!}">
                </div>

                <div class="form-group">
                    <label>发件人</label>
                    <input class="form-control" name="sender" value="${order.sender!}">
                </div>

                <div class="form-group">
                    <label>发件人电话</label>
                    <input class="form-control" name="sender_phone" value="${order.senderPhone!}">
                </div>

                <div class="form-group">
                    <label>收件人</label>
                    <input class="form-control" name="contact" value="${order.contact!}">
                </div>

                <div class="form-group">
                    <label>收件人身份证号</label>
                    <input class="form-control" name="identity" value="${order.identity!}">
                </div>

                <div class="form-group">
                    <label>收件人手机</label>
                    <input class="form-control" name="phone" value="${order.phone!}">
                </div>


                <div class="form-group">
                    <label>收件人地址</label>
                    <input class="form-control" name="address" value="${order.address!}">
                </div>

                <div class="form-group">
                    <label>邮编</label>
                    <input class="form-control" name="zip_code" value="${order.zipCode!}">
                </div>

                <div class="form-group">
                    <label>商品名称</label>
                    <input class="form-control" name="goods_name" value="${order.goodsName!}">
                </div>

                <div class="form-group">
                    <label>数量</label>
                    <input class="form-control" name="quantity" value="${order.quantity!}">
                </div>

                <div class="form-group">
                    <label>申报价格</label>
                    <input class="form-control" name="declared" value="${order.declared!}">
                </div>

                <div class="form-group">
                    <label>保额</label>
                    <input class="form-control" name="insurance" value="${order.insurance!}">
                </div>

                <div class="form-group">
                    <label>备注</label>
                    <input class="form-control" name="comment" value="${order.comment!}">
                </div>

            <#if USER.isAdmin()>

                <div class="form-group">
                    <label>物流状态</label>
                    <input class="form-control" name="lg_status" value="${order.lgStatus!}">
                </div>

                <div class="form-group">
                    <label>物流信息</label>
                    <textarea class="form-control" name="lg_info" rows="5">${order.lgInfo!}</textarea>
                </div>
            </#if>

                <button type="submit" id="btn" class="btn btn-default">提交</button>
            </form>
        </div>
    </div>
</div>

<#include "/_layout/script.ftl"/>
<#--<script>-->
    <#--$(function () {-->
        <#--$('#btn').click(function () {-->
            <#--var url = $('form').attr('action');-->
            <#--var data = $('form').serialize();-->

            <#--$.ajax({-->
                <#--url: url,-->
                <#--data: data,-->
                <#--success: function (data) {-->
                    <#--if (data.code == 0) {-->
                        <#--alert(data.info);-->
                        <#--window.history.go(-1);-->
                    <#--} else {-->
                        <#--alert("操作失败");-->
                    <#--}-->
                <#--}-->
            <#--})-->
        <#--});-->
    <#--})-->
<#--</script>-->
</body>
</html>
