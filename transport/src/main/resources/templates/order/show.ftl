<!DOCTYPE html>
<html>
<#assign TITLE="订单详情">
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

        <section class="content" style="padding-top: 55px; ">
            <div class="row">
                <div class="col-md-12 col-xs-12">
                    <div class="box">
                        <div class="box-body" style="padding: 25px 15px;">
                            <table class="table table-bordered">
                                <tr>
                                    <td colspan="6">
                                        <strong>基本信息：</strong>
                                        <strong class="pull-right">状态：${ele.status.getValue()}</strong>
                                    </td>
                                </tr>
                                <tr>
                                    <td>发货单号</td>
                                    <td>${ele.sn}</td>
                                    <td>交易号</td>
                                    <td>${ele.tn}</td>
                                    <td>参考号</td>
                                    <td>${ele.referenceNumber}</td>
                                </tr>
                                <tr>
                                    <td>派送方式</td>
                                    <td>${ele.orderSnapshotVo.ckt1!}</td>
                                    <td>保险类型</td>
                                    <td>${ele.orderSnapshotVo.ckt2!}</td>
                                    <td>销售平台</td>
                                    <td>${ele.orderSnapshotVo.ckt3!}</td>
                                </tr>
                                <tr>
                                    <td>仓库</td>
                                    <td>${ele.warehouseName!}</td>
                                    <td>总费用</td>
                                    <td>${ele.total!} USD</td>
                                    <td>总重量</td>
                                    <td>${ele.weight!} KG</td>
                                </tr>
                                <tr>
                                    <td>创建时间</td>
                                    <td></td>
                                    <td>发货时间</td>
                                    <td></td>
                                    <td>支付时间</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>备注</td>
                                    <td colspan="5">${ele.comment!}</td>
                                </tr>
                                <tr>
                                    <td colspan="6"><strong>收货人信息：</strong></td>
                                </tr>
                                <tr>
                                    <td>姓名</td>
                                    <td>${ele.orderSnapshotVo.ckf2!}</td>
                                    <td colspan="2">
                                        电话：${ele.orderSnapshotVo.ckf4!} Email：${ele.orderSnapshotVo.ckf6!}
                                    </td>
                                    <td>身份证</td>
                                    <td>${ele.orderSnapshotVo.ckf8!}</td>
                                </tr>
                                <tr>
                                    <td>国家</td>
                                    <td>${ele.orderSnapshotVo.ckf1!}</td>
                                    <td>州/省</td>
                                    <td>
                                    ${ele.orderSnapshotVo.ckf3!}

                                    <#if ele.orderSnapshotVo.ckf7??>
                                        （邮编：${ele.orderSnapshotVo.ckf7!}）
                                    </#if>
                                    </td>
                                    <td>城市</td>
                                    <td>${ele.orderSnapshotVo.ckf5!}</td>
                                </tr>
                                <tr>
                                    <td>公司</td>
                                    <td>${ele.orderSnapshotVo.ckf9!}</td>
                                    <td>街道</td>
                                    <td>${ele.orderSnapshotVo.ckf10!}</td>
                                    <td>门牌号</td>
                                    <td>${ele.orderSnapshotVo.ckf11!}</td>
                                </tr>
                            </table>

                            <table class="table table-bordered" style="margin-top: 25px;">
                                <thead>
                                <tr>
                                    <td colspan="7"><strong>货品信息：</strong></td>
                                </tr>

                                <tr>
                                    <th>序号</th>
                                    <th>货品名称</th>
                                    <th>货品SKU</th>
                                    <th>实际数量</th>
                                    <th>单件重量</th>
                                    <th>总重量</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <#list ele.orderItems as pp>
                                <tr>
                                    <td>${pp_index + 1}</td>
                                    <td><a href="/product/${pp.productId}/show">${pp.productSnapshotVo.productName}</a>
                                    </td>
                                    <td>${pp.productSnapshotVo.productSku}</td>
                                    <td>${pp.quantity}</td>
                                    <td>${pp.productSnapshotVo.weight} KG</td>
                                    <td>${pp.productSnapshotVo.weight * pp.quantity} KG</td>
                                    <td><a href="/product/${pp.productId}/show">货品详情</a></td>
                                </tr>
                                </#list>
                                </tbody>
                            </table>

                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>

<#include "*/_layout/footer.ftl"/>
</div>

<#include "*/_layout/script.ftl" />
</body>
</html>