<div class="box">
    <div class="box-header with-border">
        <h3 class="box-title">入库单（${ele.cn}）</h3>
    </div>
    <div class="box-body">
        <table class="table table-bordered margin-bottom">
            <thead>
            <tr>
                <th>入库单号</th>
                <th>平台单号</th>
            <#if ele.type.ordinal() == 1>
                <th>快递单号</th>
                <th>收件人</th>
            </#if>

                <th>仓库</th>
                <th>SKU数</th>
                <th>总件数</th>
                <th>实际总件数</th>
                <th>总重量</th>
                <th>状态</th>
                <th>创建时间</th>
                <th>备注</th>
            </tr>
            </thead>
            <tbody>
            <#assign tw = 0>

            <#assign qty = ele.quantity>
            <#if ele.status.ordinal() == 0 || ele.status.ordinal() == 3 || ele.status.ordinal() == 5>
                <#assign qty = ele.expectQuantity>
            </#if>

            <#list ele.packageProducts as pp>
                <#assign tw = tw + qty * pp.product.weight>
            </#list>
            <tr>
                <td>${ele.cn}</td>
                <td>${ele.referenceNumber}</td>
            <#if ele.type.ordinal() == 1>
                <td>${ele.searchNo}</td>
                <td>${ele.contact}</td>
            </#if>
                <td>${ele.warehouseName}</td>
                <td>${ele.packageProducts?size}</td>
                <td>${ele.expectQuantity}</td>
                <td>${ele.quantity}</td>
                <td>${tw}</td>
                <td>${ele.status.getValue()}</td>
                <td>${ele.createdAt?string}</td>
                <td>${ele.comment!}</td>
            </tr>
            </tbody>
        </table>
    </div>

    <div class="box-footer clearfix">
        <a class="btn btn-primary" href="/package/${ele.id}/print?type=sku" target="_blank">打印SKU</a>
        <a class="btn btn-primary" href="/package/${ele.id}/print_package" target="_blank">打印运单</a>
    </div>
</div>

<div class="box">
    <div class="box-header with-border">
        <h3 class="box-title">入库单商品列表</h3>
    </div>
    <div class="box-body">
        <table class="table table-bordered margin-bottom">
            <thead>
            <tr>
                <th>序号</th>
                <th>货品名称</th>
                <th>货品SKU</th>
                <th>预估数量</th>
                <th>实际数量</th>
                <th>单件重量</th>
                <th>总重量</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <#list ele.packageProducts as pp>
            <tr>
                <td>${pp_index + 1}</td>
                <td>
                    <#if AFLAG>
                        <a href="/admin/product/${pp.productId}/show">${pp.product.productName}</a>
                    <#else >
                        <a href="/product/${pp.productId}/show">${pp.product.productName}</a>
                    </#if>
                </td>
                <td>${pp.product.productSku}</td>
                <td>${pp.expectQuantity}</td>
                <td>${pp.quantity}</td>
                <td>${pp.product.weight}</td>
                <td>${pp.product.weight * pp.quantity}</td>
                <td>
                    <#if AFLAG>
                        <a href="/admin/product/${pp.productId}/show">货品详情</a>
                    <#else >
                        <a href="/product/${pp.productId}/show">货品详情</a>
                    </#if>
                </td>
            </tr>
            </#list>
            </tbody>
        </table>
    </div>
</div>

<#if statements?exists && statements?size gt 0 >
<div class="box">
    <div class="box-header with-border">
        <h3 class="box-title">费用说明</h3>
    </div>
    <div class="box-body">

        <table class="table table-bordered margin-bottom">
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
                    <td><#if e.payAt??>${e.payAt?string}</#if></td>
                    <td>
                        <#if e.status.ordinal() == 0 && ele.userId == USER.id && !AFLAG>
                            <a href="/user/statements/${e.id}/pay">立即支付</a>
                        </#if>
                    </td>
                </tr>
                </#list>
            </tbody>
        </table>

    </div>
</div>
</#if>
