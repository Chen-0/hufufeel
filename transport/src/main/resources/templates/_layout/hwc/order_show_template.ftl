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
                <td>${ele.createdAt?string}</td>
                <td>发货时间</td>
                <td>
                <#if ele.outTime??>
                                            ${ele.outTime?string}
                                        </#if>
                </td>
                <td>快递单号</td>
                <td class="text-primary">
                    <strong>
                        ${ele.express!} ${ele.expressNo!}
                    </strong>
                </td>
            </tr>
        <#if ele.surcharge gt 0>
            <tr class="text-primary">
                <td>额外费用</td>
                <td>${ele.surcharge} USD</td>
                <td>额外费用说明</td>
                <td colspan="3">${ele.surchargeComment}</td>
            </tr>
        </#if>
            <tr>
                <td>备注</td>
                <td colspan="5">${ele.comment!}</td>
            </tr>
        <#if ele.status.ordinal() == 4>
            <tr>
                <td>审核失败原因</td>
                <td colspan="5" class="text-danger">${ele.reason!}</td>
            </tr>
        </#if>


            <tr>
                <td colspan="6"><strong>收货人信息：</strong></td>
            </tr>
        <#if ele.cType == "w">
            <tr>
                <td>姓名</td>
                <td>${ele.orderSnapshotVo.ckf2!}</td>
                <td>
                    电话：${ele.orderSnapshotVo.ckf4!}
                </td>
                <td>
                    Email：${ele.orderSnapshotVo.ckf6!}
                </td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td>国家</td>
                <td>${ele.orderSnapshotVo.ckf1!} <#if ele.orderSnapshotVo.ckf12?exists && ele.orderSnapshotVo.ckf12?length gt 0>
                    - <strong class="text-danger">${ele.orderSnapshotVo.ckf12}</strong></#if></td>
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
                <td></td>
                <td></td>
                <td>街道</td>
                <td>${ele.orderSnapshotVo.ckf10!}</td>
                <td>门牌号</td>
                <td>${ele.orderSnapshotVo.ckf11!}</td>
            </tr>
        <#else>
            <tr>
                <td colspan="6"><span>${ele.doc.originalFilename}</span> <a href="/file/${ele.doc.name}"
                                                                            download="${ele.doc.originalFilename}">点击下载</a>
                </td>
            </tr>
        </#if>
        <#if lg?exists && lg.comment?length gt 0 >
            <tr>
                <td>物流信息：</td>
                <td colspan="5">
                    <pre>${lg.comment}</pre>
                </td>
            </tr>
        </#if>
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
                <td>
                    <#if AFLAG>
                        <a href="/admin/product/${pp.productId}/show">${pp.productSnapshotVo.productName}</a>
                    <#else>
                        <a href="/product/${pp.productId}/show">${pp.productSnapshotVo.productName}</a>
                    </#if>
                </td>
                <td>${pp.productSnapshotVo.productSku}</td>
                <td>${pp.quantity}</td>
                <td>${pp.productSnapshotVo.weight} KG</td>
                <td>${pp.productSnapshotVo.weight * pp.quantity} KG</td>
                <td>
                    <#if AFLAG>
                        <a href="/admin/product/${pp.productId}/show">货品详情</a>
                    <#else>
                        <a href="/product/${pp.productId}/show">货品详情</a>
                    </#if>
                </td>
            </tr>
            </#list>
            </tbody>
        </table>

    <#if statements?exists && statements?size gt 0 >
        <table class="table table-bordered" style="margin-top: 25px;">
            <thead>
            <tr>
                <th>序号</th>
                <th>费用类型</th>
                <th>费用说明</th>
                <th>总额</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>交易时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <#list statements as e>
                <tr>
                    <td>${e_index + 1}</td>
                    <td>${e.type.getValue()}</td>
                    <td>${e.comment!}</td>
                    <td>${e.total} USD</td>
                    <td>${e.status.getValue()}</td>
                    <td>${e.createdAt?string}</td>
                    <td>
                        <#if e.payAt??>
                                        ${e.payAt?string}
                                    </#if>
                    </td>
                    <td>
                        <#if e.status.ordinal() == 0 && !AFLAG>
                            <a href="/user/statements/${e.id}/pay">立即支付</a>
                        </#if>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>
    </#if>
    </div>
</div>