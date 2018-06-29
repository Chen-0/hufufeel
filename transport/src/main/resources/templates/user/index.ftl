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
                <div class="col-xs-3">
                    <div class="box box-widget widget-user">
                        <div class="widget-user-header bg-aqua-active">
                            <h3 class="widget-user-username">${user.name} <small>No.${user.hwcSn}</small></h3>
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
                                </div>
                                <div class="col-sm-6">
                                    <div class="description-block">
                                        <h5 class="description-header">状态</h5>
                                        <span class="description-text">${user.arrearage?string("冻结", "正常")}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-xs-3">
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
                <div class="col-xs-3">
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
                <div class="col-xs-3">
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

            <div class="row">
                <div class="col-xs-12">
                    <div class="box">
                        <div class="box-header">
                            <h3 class="box-title">收费规则</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <table class="table">
                                <tr>
                                    <td>入库费用</td>
                                    <td>
                                        <#switch cc.rkt>
                                            <#case "RK-AZ">
                                                按重收费
                                            <#break>
                                            <#case "RK-AX">
                                                按单收费
                                                <#break>
                                            <#case "RK-AF">
                                                免费
                                                <#break>
                                        </#switch>
                                    </td>
                                    <td>
                                    <#switch cc.rkt>
                                        <#case "RK-AZ">
                                            每公斤 ${cc.rkv} USD
                                            <#break>
                                        <#case "RK-AX">
                                            每单 ${cc.rkv} USD
                                            <#break>
                                        <#case "RK-AF">
                                            0
                                            <#break>
                                    </#switch>
                                    </td>
                                </tr>

                                <tr>
                                    <td>上架费用</td>
                                    <td>
                                    <#switch cc.sjt>
                                        <#case "SJ-AS">
                                            按体积收费
                                            <#break>
                                        <#case "SJ-AJ">
                                            按件收费
                                            <#break>
                                        <#case "SJ-AF">
                                            免费
                                            <#break>
                                    </#switch>
                                    </td>
                                    <td>
                                    <#switch cc.sjt>
                                        <#case "SJ-AS">
                                            每立方米 ${cc.sjv} USD
                                            <#break>
                                        <#case "SJ-AJ">
                                            每件 ${cc.sjv} USD
                                            <#break>
                                        <#case "SJ-AF">
                                            0
                                            <#break>
                                    </#switch>
                                    </td>
                                </tr>

                                <tr>
                                    <td>出库费用</td>
                                    <td>
                                    <#switch cc.ddt>
                                        <#case "DD-AZ">
                                            按重量收费
                                            <#break>
                                        <#case "DD-AJ">
                                            按件收费
                                            <#break>
                                    </#switch>
                                    </td>
                                    <td>
                                    <#switch cc.ddt>
                                        <#case "DD-AZ">
                                            <p>出库总重在1公斤内：${cc.ddv[0]}USD + 每件 * ${cc.ddv[1]} USD（第1件不计算，10件封顶）</p>
                                            <p>出库总重超过1公斤：总重（公斤） * ${cc.ddv[2]} USD + 每件 * ${cc.ddv[3]} USD （第1件不计算，10件封顶）</p>
                                            <#break>
                                        <#case "DD-AJ">
                                            每件 ${cc.ddv[0]} USD
                                            <#break>
                                    </#switch>
                                    </td>
                                </tr>

                                <tr>
                                    <td>退货入库费</td>
                                    <td>
                                    <#switch cc.thrkt>
                                        <#case "TH_RK_AD">
                                            按箱收费
                                            <#break>
                                    </#switch>
                                    </td>
                                    <td>
                                    <#switch cc.thrkt>
                                        <#case "TH_RK_AD">
                                            每箱 ${cc.thrkv} USD
                                            <#break>
                                    </#switch>
                                    </td>
                                </tr>

                                <tr>
                                    <td>退货上架费</td>
                                    <td>
                                        <#switch cc.thsjt>
                                            <#case "TH_SJ_SL">
                                                按件收费
                                                <#break>
                                        </#switch>
                                    </td>
                                    <td>
                                    <#switch cc.thsjt>
                                        <#case "TH_SJ_SL">
                                            每单的总件数 * ${cc.thsjv} USD （件数无封顶）
                                            <#break>
                                    </#switch>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <!-- /.box-body -->
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
