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

            <form role="form" method="post" action="/order/${order.id}/recheck">
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
                    <label>备注</label>
                    <input class="form-control" name="comment" value="${order.comment!}">
                </div>

                <button type="submit" id="btn" class="btn btn-default">提交</button>
            </form>
        </div>
    </div>
</div>

<#include "/_layout/script.ftl"/>
</body>
</html>
