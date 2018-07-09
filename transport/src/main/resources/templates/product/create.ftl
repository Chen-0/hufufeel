<!DOCTYPE html>
<html>
<#assign TITLE = "创建货品">
<#include "*/_layout/head.ftl" />

<body class="hold-transition skin-black-light sidebar-mini">
<!-- Site wrapper -->
<div class="wrapper">
<#include "*/_layout/aside.ftl" />

    <div class="content-wrapper">
        <section class="content-header">
            <h1>${TITLE}</h1>
        </section>


        <section class="content">
            <div class="row">
                <div class="col-md-10 col-md-offset-1 col-xs-12">
                    <div class="box box-primary">
                        <form class="xn-form" role="form" method="post" action="/product/post_create" enctype="multipart/form-data">
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
                            <@fileFormGroup label="上传货品图片" name="image"></@fileFormGroup>
                                <div class="box-footer">
                                    <button type="submit" class="btn btn-primary">提交</button>
                                </div>
                        </form>
                    </div>
                </div>
            </div>

        </section>
    </div>

<#include "*/_layout/footer.ftl"/>
</div>

<#include "*/_layout/script.ftl" />
<script src="https://cdn.bootcss.com/blueimp-file-upload/9.21.0/js/vendor/jquery.ui.widget.min.js"></script>
<script src="https://cdn.bootcss.com/blueimp-file-upload/9.21.0/js/jquery.iframe-transport.min.js"></script>
<script src="https://cdn.bootcss.com/blueimp-file-upload/9.21.0/js/jquery.fileupload.min.js"></script>
<script>
    $(document).ready(function () {
        $('#deadline').datepicker({
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
                $('#size').val(accDiv(accMul(accMul(a, b), c), 1000000.0));
            }
        }

        $('#image').fileupload({
            url: '/product/image/upload',
            dataType: 'json',
            add: function (e, data) {
                $('#image').next().html('文件正在上传！');
                data.submit();
            },
            done: function (e, data) {
                var resp = data.result;
                var error = '文件上传失败，请刷新页面重试。';

                if (resp.success) {
                    $('#image').next().html('上传文件成功！ ' + resp.data.originalFilename);
                    $('#imageId').val(resp.data.id);
                }else {
                    $('#image').next().html(error);
                }
            }
        });
    });
</script>
</body>
</html>
