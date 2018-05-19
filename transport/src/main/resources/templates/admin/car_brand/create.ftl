<!DOCTYPE html>
<html>

<#include "*/admin/_layout/head.ftl" />

<body class="theme-red">
<#include "*/admin/_layout/aside.ftl" />

<section class="content">
    <div class="container-fluid">
        <div class="block-header">
            <h2>
                创建品牌
                <small>创建关于汽车的品牌</small>
            </h2>
        </div>
    </div>

    <div class="card">

        <div class="body">
            <form method="post" action="/admin/car/brand/create" enctype="multipart/form-data">
                <label for="name">品牌名称：</label>
                <div class="form-group">
                    <div class="form-line">
                        <input type="text" id="name" class="form-control" placeholder="广汽本田，东风日产" name="name">
                    </div>
                </div>

                <label for="image">品牌Logo：</label>
                <div class="form-group">
                    <div class="form-line">
                        <input type="file" id="image" class="form-control" name="image">
                    </div>
                </div>

                <label for="description">品牌描述：</label>
                <div class="form-group">
                    <div class="form-line">
                        <input type="text" id="description" class="form-control" name="description" >
                    </div>
                </div>
                <button type="submit" class="btn btn-primary m-t-15 waves-effect">提交</button>
            </form>
        </div>
    </div>
</section>

<#include "*/admin/_layout/script.ftl" />
<script>
    $(function () {
        $(".remove").click(function (e) {
            e.preventDefault();

            var url = $(this).attr("href");

            if (confirm("确认删除该标签")) {
                window.location.href = url;
            }
        });
    })
</script>
</body>

</html>