<!DOCTYPE html>
<html lang="en">
<#assign title="修改货品" />
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
        <div class="col-xs-12">
            <form role="form" method="post" action="/admin/product/${felements.id}/update">
                <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>

                <div class="box-body">
                    <div class="form-group">
                        <label for="">业务类型*：</label>
                    <#assign foo=felements.businessType>
                        <div class="radio">
                            <label>
                                <input type="radio" name="businessType" value="0" ${foo?string("", "checked=true")}>
                                出口业务
                            </label>

                            <label style="margin-left: 15px;">
                                <input type="radio" name="businessType" value="1" ${foo?string("checked=true", "")}>
                                进口业务
                            </label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="productName">商品名称*：</label>
                        <input type="text" class="form-control" id="productName" name="productName" value="${felements.productName!}">
                    <#if errors??>
                        <p class="text-danger">${errors.productName!}</p>
                    </#if>
                    </div>

                    <div class="form-group">
                        <label for="productSku">商品SKU*：</label>
                        <input type="text" class="form-control" id="productSku" name="productSku" value="${felements.productSku!}">
                    <#if errors??>
                        <p class="text-danger">${errors.productSku!}</p>
                    </#if>
                    </div>

                    <div class="form-group">
                        <label for="">电池类型*：</label>
                    <#assign foo=felements.isBattery>

                        <div class="radio">
                            <label>
                                <input type="radio" name="isBattery" value="0" ${foo?string("", "checked=true")} >
                                非电池类型
                            </label>

                            <label style="margin-left: 15px;">
                                <input type="radio" name="isBattery" value="1" ${foo?string("checked=true", "")}>
                                电池类型
                            </label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="origin">原产地：</label>
                        <input type="text" class="form-control" id="origin" name="origin" value="${felements.origin!}">
                    <#if errors??>
                        <p class="text-danger">${errors.origin!}</p>
                    </#if>
                    </div>

                    <div class="form-group">
                        <label for="weight">重量（KG）*：</label>
                        <input type="text" class="form-control" id="weight" name="weight" value="${felements.weight!}">
                    <#if errors??>
                        <p class="text-danger">${errors.weight!}</p>
                    </#if>
                    </div>


                    <div class="row">
                        <div class="col-xs-4">
                            <div class="form-group">
                                <label for="length">长（厘米）*：</label>
                                <input type="text" class="form-control" id="length" name="length" value="${felements.length!}">
                            <#if errors??>
                                <p class="text-danger">${errors.length!}</p>
                            </#if>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="form-group">
                                <label for="width">宽（厘米）*：</label>
                                <input type="text" class="form-control" id="width" name="width" value="${felements.width!}">
                            <#if errors??>
                                <p class="text-danger">${errors.width!}</p>
                            </#if>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <div class="form-group">
                                <label for="height">高（厘米）*：</label>
                                <input type="text" class="form-control" id="height" name="height" value="${felements.height!}">
                            <#if errors??>
                                <p class="text-danger">${errors.height!}</p>
                            </#if>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="size">体积（立方米）：</label>
                        <input type="text" class="form-control" id="size" disabled>
                    </div>

                    <div class="form-group">
                        <label for="datepicker">货品有效期：</label>

                        <div class="input-group date">
                            <div class="input-group-addon">
                                <i class="fa fa-calendar"></i>
                            </div>
                            <input type="text" class="form-control pull-right" id="datepicker" name="deadline" value="${felements.deadline!}">

                        <#if errors??>
                            <p class="text-danger">${errors.deadline!}</p>
                        </#if>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="">危险货物*：</label>
                    <#assign foo=felements.isDanger>
                        <div class="radio">
                            <label>
                                <input type="radio" name="isDanger" value="0" ${foo?string("", "checked=true")}>
                                否
                            </label>

                            <label style="margin-left: 15px;">
                                <input type="radio" name="isDanger" value="1" ${foo?string("checked=true", "")}>
                                是
                            </label>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="quotedName">申报名称*：</label>
                        <input type="text" class="form-control" id="quotedName" name="quotedName" value="${felements.quotedName!}">
                    <#if errors??>
                        <p class="text-danger">${errors.quotedName!}</p>
                    </#if>
                    </div>

                    <div class="form-group">
                        <label for="quotedPrice">申报价值（USD）*：</label>
                        <input type="text" class="form-control" id="quotedPrice" name="quotedPrice" value="${felements.quotedPrice!}">
                    <#if errors??>
                        <p class="text-danger">${errors.quotedPrice!}</p>
                    </#if>
                    </div>

                    <div class="form-group">
                        <label for="comment">备注：</label>
                        <input type="text" class="form-control" id="comment" name="comment" value="${felements.comment!}">
                    <#if errors??>
                        <p class="text-danger">${errors.comment!}</p>
                    </#if>
                    </div>
                </div>

                <div class="box-footer">
                    <button type="submit" class="btn btn-primary">提交</button>
                </div>
            </form>
        </div>
    </div>
</div>



<#include "*/admin/_layout/script.ftl"/>
<script>
    $(function () {
        $('#datepicker').datepicker({
            format: 'yyyy-mm-dd',
            autoclose: true
        });

        $('#height, #width, #length').change(function () {
            calcSize();
        });

        calcSize();

        function calcSize() {
            var a = $('#height').val();
            var b = $('#width').val();
            var c = $('#length').val();

            if (isEmpty(a) || isEmpty(b) || isEmpty(c)) {
                ;
            } else {
                $('#size').val(accDiv(accMul(accMul(a, b) , c) , 1000000.0));
            }
        }
    });
</script>
</body>
</html>