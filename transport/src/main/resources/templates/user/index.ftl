<!DOCTYPE html>
<html>
<#assign TITLE="个人中心">
<#include "*/_layout/head.ftl" />

<body class="hold-transition skin-black-light sidebar-mini">
<div class="wrapper">
<#include "*/_layout/aside.ftl" />

    <div class="content-wrapper">
        <section class="content-header">
            <h1>
            ${TITLE}
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="#">Examples</a></li>
                <li class="active">Blank page</li>
            </ol>
        </section>

        <section class="content">
            <div class="row">
                <div class="col-md-4 col-md-offset-4">
                    <div class="box box-widget widget-user">
                        <!-- Add the bg color to the header using any of the bg-* classes -->
                        <div class="widget-user-header bg-aqua-active">
                            <h3 class="widget-user-username">${user.name}</h3>
                            <h5 class="widget-user-desc">${user.username}</h5>
                        </div>
                        <div class="widget-user-image">
                            <img class="img-circle" src="/static/LTE/user.jpg" alt="User Avatar">
                        </div>
                        <div class="box-footer">
                            <div class="row">
                                <div class="col-sm-6 border-right">
                                    <div class="description-block">
                                        <h5 class="description-header">余额</h5>
                                        <span class="description-text">${user.usd} usd</span>
                                    </div>
                                    <!-- /.description-block -->
                                </div>
                                <!-- /.col -->
                                <div class="col-sm-6">
                                    <div class="description-block">
                                        <h5 class="description-header">状态</h5>
                                        <span class="description-text">${user.arrearage?string("冻结", "正常")}</span>
                                    </div>
                                    <!-- /.description-block -->
                                </div>
                                <!-- /.col -->
                                <#--<div class="col-sm-4">-->
                                    <#--<div class="description-block">-->
                                        <#--<h5 class="description-header"></h5>-->
                                        <#--<span class="description-text">PRODUCTS</span>-->
                                    <#--</div>-->
                                    <#--<!-- /.description-block &ndash;&gt;-->
                                <#--</div>-->
                                <!-- /.col -->
                            </div>
                            <!-- /.row -->
                        </div>
                    </div>
                </div>
            </div>

            <div class="row" style="margin-top: 25px;">
                <div class="col-xs-4">
                    <!-- small box -->
                    <div class="small-box bg-aqua">
                        <div class="inner">
                            <h3>${MSG_COUNT}</h3>

                            <p>新消息</p>
                        </div>
                        <div class="icon">
                            <i class="ion ion-bag"></i>
                        </div>
                        <a href="/message/index" class="small-box-footer">More info <i class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
                <!-- ./col -->
                <div class="col-xs-4">
                    <!-- small box -->
                    <div class="small-box bg-green">
                        <div class="inner">
                            <h3>${pc}</h3>

                            <p>冻结的入库单</p>
                        </div>
                        <div class="icon">
                            <i class="ion ion-stats-bars"></i>
                        </div>
                        <a href="/package/index?status=5" class="small-box-footer">More info <i class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
                <!-- ./col -->
                <div class="col-xs-4">
                    <!-- small box -->
                    <div class="small-box bg-yellow">
                        <div class="inner">
                            <h3>${oc}</h3>

                            <p>冻结的发货单</p>
                        </div>
                        <div class="icon">
                            <i class="ion ion-person-add"></i>
                        </div>
                        <a href="/order/index?status=3" class="small-box-footer">More info <i class="fa fa-arrow-circle-right"></i></a>
                    </div>
                </div>
            </div>
        </section>
    </div>

<#include "*/_layout/footer.ftl"/>
</div>

<#include "*/_layout/script.ftl" />
<script>
    $('input[type="checkbox"].flat-red, input[type="radio"].flat-red').iCheck({
        checkboxClass: 'icheckbox_flat-green',
        radioClass: 'iradio_flat-green'
    });
</script>
</body>
</html>
