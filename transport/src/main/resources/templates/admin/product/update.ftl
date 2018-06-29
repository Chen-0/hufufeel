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
        <div class="col-md-10 col-md-offset-1 col-xs-12">

            <div class="box box-primary">
                <form class="xn-form" role="form" method="post" action="/admin/product/${id}/update" enctype="multipart/form-data">
                    <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>

                    <div class="box-body">
                    <@radioFormGroup label="业务类型${MUST}" name="businessType" items=bts></@radioFormGroup>
                    <@formGroup label="商品名称${MUST}" name="productName"></@formGroup>
                    <@formGroup label="商品SKU${MUST}" name="productSku"></@formGroup>
                    <@radioFormGroup label="电池类型${MUST}" name="isBattery" items=ibs></@radioFormGroup>
                    <@formGroup label="原产地" name="origin"></@formGroup>
                    <@formGroup label="重量（KG）${MUST}" name="weight"></@formGroup>
                        <div class="row">
                            <div class="col-xs-4">
                            <@formGroup label="长（厘米）${MUST}" name="length"></@formGroup>
                            </div>
                            <div class="col-xs-4">
                            <@formGroup label="宽（厘米）${MUST}" name="width"></@formGroup>
                            </div>
                            <div class="col-xs-4">
                            <@formGroup label="高（厘米）${MUST}" name="height"></@formGroup>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="size">体积（立方米）：</label>
                            <input type="text" class="form-control" id="size" disabled>
                        </div>
                    <@formGroup label="货品有效期" name="deadline"></@formGroup>
                    <@radioFormGroup label="危险货物${MUST}" name="isDanger" items=ids></@radioFormGroup>
                    <@formGroup label="申报名称${MUST}" name="quotedName"></@formGroup>
                    <@formGroup label="申报价值（USD）${MUST}" name="quotedPrice"></@formGroup>
                    <@formGroup label="备注" name="comment"></@formGroup>
                        <input type="hidden" id="imageId" name="imageId" value="${fele["imageId"]!}">
                        <div class="box-footer">
                            <button type="submit" class="btn btn-primary">提交</button>
                        </div>
                </form>
            </div>
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