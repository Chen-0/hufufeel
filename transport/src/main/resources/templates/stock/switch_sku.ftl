<!DOCTYPE html>
<html>
<#assign TITLE = "提交换标信息">
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
                        <form class="xn-form" role="form" method="post" action="/stock/switch_sku" enctype="multipart/form-data">
                            <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>

                            <div class="box-body">
                                <div class="row margin-bottom">
                                    <div class="col-xs-6">
                                    <@formGroup label="原SKU ${MUST}" name="sku"></@formGroup>
                                    </div>
                                    <div class="col-xs-6" style="padding-top: 15px;">
                                        <p>原SKU是指需被更换的SKU码，并不是填写新的 sku码。</p>
                                    </div>
                                </div>

                                <div class="row margin-bottom">
                                    <div class="col-xs-6">
                                    <@fileFormGroup label="PDF文件上传" name="uploadfile"></@fileFormGroup>
                                    </div>
                                    <div class="col-xs-6" style="padding-top: 15px;">
                                        <p>PDF文件不能出现单面纸张里只有一个SKU标，应是多个。并且仓库使用的标签尺寸是1英寸X2-5/8英寸。</p>
                                    </div>
                                </div>
                                
                                <div class="row margin-bottom">
                                    <div class="col-xs-6">
                                        <img class="img-responsive" src="/static/hwc/th_1.jpg" alt="th_1.jpg">
                                    </div>
                                    <div class="col-xs-6" style="padding-top: 15px;">
                                        打印标签前，需填入打印标签数量（数量以实际需发出货物数量为标准）。
                                    </div>
                                </div>

                                <div class="row margin-bottom">
                                    <div class="col-xs-6">
                                        <img  class="img-responsive" src="/static/hwc/th_2.jpg" alt="th_1.jpg">
                                    </div>
                                    <div class="col-xs-6" style="padding-top: 15px;">
                                        纸张类型要选择1英寸X 2-5/8英寸（HUFU唯一使用的标签尺寸）。
                                    </div>
                                </div>

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
    $(function () {
        $('#uploadfile').fileupload({
            url: '/order/pdf/upload',
            dataType: 'json',
            add: function (e, data) {
                $('#image').next().html('文件正在上传！');
                data.submit();
            },
            done: function (e, data) {
                var resp = data.result;
                var error = '文件上传失败，请刷新页面重试。';

                if (resp.success) {
                    $('#uploadfile').next().html('上传文件成功！ ' + resp.data.originalFilename);
                    $('#uploadfileId').val(resp.data.id);
                }else {
                    $('#uploadfile').next().html(error);
                }
            }
        });
    });

</script>
</body>
</html>
