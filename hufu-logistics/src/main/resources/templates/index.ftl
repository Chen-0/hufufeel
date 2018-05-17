<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>后台管理 - 虎芙货运管家</title>

    <link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/metisMenu/2.7.0/metisMenu.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/startbootstrap-sb-admin-2/3.3.7+1/css/sb-admin-2.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker3.min.css">
    <link href="//cdn.bootcss.com/morris.js/0.5.1/morris.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/style.css">

</head>

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">虎芙后台管理</h1>
        </div>
        <!-- /.col-lg-12 -->
    </div>
    <div class="row">
        <div class="col-lg-6 col-lg-offset-3">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <i class="fa fa-bar-chart-o fa-fw"></i> 本月运单总数
                </div>
                <div class="panel-body">
                    <div id="morris-donut-chart"></div>
                    <p class="text-center" style="margin-top: 1rem;">合计：${total1 + total2} （张）</p>
                </div>
                <!-- /.panel-body -->
            </div>
        </div>
        <!-- /.col-lg-4 -->
    </div>
    <!-- /.row -->
</div>
<!-- /#page-wrapper -->

<#include "/_layout/script.ftl"/>
<script src="//cdn.bootcss.com/raphael/2.2.7/raphael.min.js"></script>
<script src="//cdn.bootcss.com/morris.js/0.5.1/morris.min.js"></script>
<script>
    $.ajaxSetup({
        headers: {
            'X-CSRF-TOKEN': '${_csrf.token}'
        },
        dataType: 'json',
        type: 'post'
    });

    Morris.Donut({
        element: 'morris-donut-chart',
        data: [{
            label: "海淘客",
            value: ${total1}
        }, {
            label: "大客户",
            value: ${total2}
        }],
        resize: true
    });
</script>
</body>
</html>
