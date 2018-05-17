<!DOCTYPE html>
<html>

<#include "*/admin/_layout/head.ftl" />

<body class="theme-red">
<#include "*/admin/_layout/aside.ftl" />

<section class="content">
    <div class="container-fluid">
        <div class="block-header">
            <h2>
                创建车系
                <small>创建品牌下的车系</small>
            </h2>
        </div>
    </div>

    <div class="card">

        <div class="body">
            <form method="post" action="/admin/car/serial/create" enctype="multipart/form-data">
                <label for="name">车系名称：</label>
                <div class="form-group">
                    <div class="form-line">
                        <input type="text" id="name" class="form-control" placeholder="冠道，飞度，奥迪Q3" name="name">
                    </div>
                </div>

                <label for="carBrandId">所属品牌：</label>
                <div class="form-group">
                    <div class="form-line">
                        <select name="carBrandId" id="carBrandId">
                        <#list carBrands as carBrand >
                            <option value="${carBrand.id}">${carBrand.name}</option>
                        </#list>
                        </select>
                    </div>
                </div>


                <label for="image">车系的图片：</label>
                <div class="form-group" id="imageBody">
                    <div class="form-line">
                        <div class="row">
                            <div class="col col-md-10">
                                <input type="file" class="form-control" name="image[]">
                            </div>
                            <div class="col col-md-2">
                                <button type="button" class="btn btn-danger m-t-15 waves-effect removeImg">
                                    删除
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary m-t-15 waves-effect">提交</button>
                <button type="button" class="btn btn-default m-t-15 waves-effect" id="addImage">添加图片</button>
            </form>
        </div>
    </div>
</section>

<#include "*/admin/_layout/script.ftl" />
<script>
    $(function () {
        $("#addImage").click(function () {


            var item = '<div class="form-line">\n' +
                    '                        <div class="row">\n' +
                    '                            <div class="col col-md-10">\n' +
                    '                                <input type="file" class="form-control" name="image[]">\n' +
                    '                            </div>\n' +
                    '                            <div class="col col-md-2">\n' +
                    '                                <button type="button" class="btn btn-danger m-t-15 waves-effect removeImg">\n' +
                    '                                    删除\n' +
                    '                                </button>\n' +
                    '                            </div>\n' +
                    '                        </div>\n' +
                    '                    </div>';




            var content = $(item);
            var ele = content.find('.removeImg')[0];
            console.log(ele);
            console.log($(ele));
            $(ele).click(function () {
                console.log(11111);
                removeImg($(this));
            });

            $('#imageBody').append(content);
        });

        $('.removeImg').click(function () {
            removeImg($(this));
        });


        function removeImg($imgBtn) {
            $($imgBtn).parent().parent().parent().remove();
        }
    })
</script>
</body>

</html>