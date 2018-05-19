<!DOCTYPE html>
<html>

<#include "*/admin/_layout/head.ftl" />

<body class="theme-red">
<#include "*/admin/_layout/aside.ftl" />

<section class="content">
    <div class="container-fluid">
        <div class="block-header">
            <h2>
                文章列表
                <small>爬虫自动获取的文章将会在这里，爬虫获取的文章状态为未上架，请把合适的文章上架</small>
            </h2>
        </div>
    </div>

    <div class="row">
        <div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">
            <div class="card">
                <div class="header">
                    <h2>
                        汽车文章爬虫
                        <small>文章爬虫已经启动，日志正在输出</small>
                    </h2>
                </div>
                <div class="body">
                    <div class="body">
                        <label for="page">输入要获取的页数：</label>
                        <div class="form-group">
                            <div class="form-line">
                                <input type="text" id="page" class="form-control" value="1" name="page">
                            </div>
                        </div>
                        <button type="button" class="btn btn-primary m-t-15 waves-effect" id="start">启动爬虫</button>
                    </div>

                </div>
                <div class="body">
                    <ul class="list-group" id="logBody">
                    </ul>
                </div>
            </div>
        </div>
    </div>
</section>

<#include "*/admin/_layout/script.ftl" />
<script>
    var sid;
    var length;
    $('#start').click(function () {
        var page = $('#page').val();
        $.ajax({
            url: '/crawler/start',
            data: {
                page: page
            },
            type: 'get',
            success: function (data) {
                console.log(data);

                getLog();
            }
        });
    });

    function getLog() {
        sid = setInterval(function () {
            $.ajax({
                url: "/crawler/log",
                type: 'get',
                dataType: 'json',
                success: function (res) {
                    if (res.isSuccess === 0) {
                        console.log(res);
                        var items = res.data;
                        var len = length;
                        length = items.length;

                        if (items[length - 1] === '#END') {
                            clearInterval(sid);
                        }
                        var content = '';
                        for (var i = 0; i < items.length; i++) {
                            content += '<li class="list-group-item list-group-item-info"><strong style="font-size: 125%;">' + items[i] + '</strong></li>';
                        }

                        $("#logBody").html(content);
                    }
                }
            })
        }, 800);
    }

</script>
</body>

</html>