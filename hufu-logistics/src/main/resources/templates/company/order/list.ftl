<!DOCTYPE html>
<html lang="en">
<#assign title="多选运单"/>
<#include "/_layout/head.ftl" />

<body>
<#include "/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">多选运单</h1>
        </div>
    </div>

    <div class="row" style="display: none;">
        <div class="col-lg-offset-2 col-lg-8">
            <form role="form" method="get" action="#" id="main" >
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <textarea class="form-control" rows="8" name="tracking_number"
                          id="items"><#list orders as order>${order.trackingNumber} </#list></textarea>
                <div class="text-center" style="margin-top: 1.5rem;">
                    <button type="submit" class="btn btn-primary" id="btn">提交</button>
                </div>
            </form>
        </div>
    </div>

<#if USER.isAdmin() >
    <div class="row" style="margin-top: 2.5rem;">
        <div style="width: 85%; margin: 0 auto;">
            <table class="table table-condensed table-bordered">
                <tr>
                    <td>
                        <button class="btn btn-default" type="button" id="in">多选入库</button>
                    </td>
                    <td>
                        <button class="btn btn-danger" type="button" id="delete">删除</button>
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td>
                        <a class="btn btn-primary"
                           href="/order/print/brcode?tracking_number=<#list orders as order>${order.trackingNumber} </#list>&flag=1"
                           target="_blank">
                            打印(3in x 2in, Plugin Lodop)
                        </a>
                    </td>
                    <td>
                        <a class="btn btn-primary"
                           href="/order/print/brcode?tracking_number=<#list orders as order>${order.trackingNumber} </#list>&flag=2"
                           target="_blank">
                            打印(Letter, Plugin Lodop)
                        </a>
                    </td>
                    <td></td>
                </tr>
                <tr>
                    <td>
                        <button class="btn btn-default" type="button" id="export_guangzhuo">广州渠道导出</button>
                    </td>
                    <td>
                        <button class="btn btn-default" type="button" id="export_e">E特快渠道导出</button>
                    </td>
                    <td>
                        <button class="btn btn-success" type="button" id="export_lg">物流信息导出</button>
                    </td>
                </tr>
            </table>

        <#--<a class="btn btn-success" href="#">-->
        <#--打印(Letter,网页直接打印)-->
        <#--</a>-->


            <div class="alert alert-warning" style="margin-top: 1rem;">
                <strong>提示：</strong><br>
                <p>1. 打印需要下载插件 <a href="http://www.lodop.net/download/CLodop_Setup_for_Win32NT_2.090.zip">32位C-Lodop独立版(CLodop2.090)</a>
                    并支持 IE 浏览器（极速模式），火狐浏览器， 360 浏览器。</p>
                <p>2. 导出（广州渠道导出和 E 特快渠道导出）只能导出运单状态为已出库的运单，若运单状态不为已出库，那么将会跳过。</p>
                <p>3. 多选入库，如果运单的状态不为待入库，则此运单会被跳过。</p>
            </div>
        </div>
    </div>
<#else >
    <div class="row" style="margin-top: 2.5rem;">
        <div style="width: 85%; margin: 0 auto;">
            <table class="table table-condensed table-bordered">
                <tr>
                    <td>
                        <a class="btn btn-primary"
                           href="/order/print/brcode?tracking_number=<#list orders as order>${order.trackingNumber} </#list>&flag=1"
                           target="_blank">
                            打印(3in x 2in, Plugin Lodop)
                        </a>
                    </td>
                    <td>
                        <a class="btn btn-primary"
                           href="/order/print/brcode?tracking_number=<#list orders as order>${order.trackingNumber} </#list>&flag=2"
                           target="_blank">
                            打印(Letter, Plugin Lodop)
                        </a>
                    </td>
                    <td>
                        <button class="btn btn-danger" type="button" id="delete">删除</button>
                    </td>
                </tr>
                <tr>
                    <td colspan="3">
                        <button class="btn btn-default" type="button" id="export_default">虎芙导出</button>
                    </td>
                </tr>
            </table>

            <div class="alert alert-warning" style="margin-top: 1rem;">
                <strong>提示：</strong><br>
                <p>1. 打印需要下载插件 <a href="http://www.lodop.net/download/CLodop_Setup_for_Win32NT_2.090.zip">32位C-Lodop独立版(CLodop2.090)</a>
                    并支持 IE 浏览器（ie 11以上），火狐浏览器， 360 浏览器（极速模式）。</p>
                <p>2.只能导出运单状态为已出库的运单，若运单状态不为已出库，那么将会跳过。</p>
            </div>
        </div>
    </div>
</#if>

    <div class="row" style="margin: 2.5rem 0;">
        <div style="width: 85%; margin: 0 auto;">
            <table class="table table-striped table-bordered table-hover" id="dataTables">
                <thead>
                <tr>
                    <th>批次</th>
                    <th>发件人</th>
                    <th>运单号</th>
                    <th>收件人</th>
                    <th>收件人电话</th>
                    <th>收件人地址</th>
                    <th>邮编</th>
                    <th>商品信息</th>
                    <th>商品数量</th>
                    <th>运费（元）</th>
                </tr>
                </thead>
                <tbody>
                <#list orders as order>
                <tr>
                    <td>${order.batch!}</td>
                    <td>${order.sender!}</td>
                    <td>${order.trackingNumber!}</td>
                    <td>${order.contact!}</td>
                    <td>${order.phone!}</td>
                    <td>${order.address!}</td>
                    <td>${order.zipCode!}</td>
                    <td>${order.goodsName!}</td>
                    <td>${order.quantity!}</td>
                    <td>${order.total!}</td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>

</div>


<#include "/_layout/script.ftl"/>
<script>
    $(function () {
        var $main = $('#main');
        $('#export_guangzhuo').click(function () {
            $main.attr('action', '/order/export_guangzhuo');
            $main.attr('method', 'get');
            $main.submit();
        });

        $('#export_e').click(function () {
            $main.attr('action', '/order/export_e');
            $main.attr('method', 'get');
            $main.submit();
        });

        $('#export_lg').click(function () {
            $main.attr('action', '/order/export_lg');
            $main.attr('method', 'get');
            $main.submit();
        });

        $('#delete').click(function () {
            if (confirm("确定删除，删除后将无法恢复！")) {
                $main.attr('action', '/order/remove');
                $main.attr('method', 'post');
                $main.submit();
            }
        });

        $('#in').click(function () {
            if (confirm('确认入库？')) {
                $main.attr('action', '/order/in');
                $main.attr('method', 'get');
                $main.submit();
            }
        });

        $('#export_default').click(function () {
            $main.attr('action', '/order/export/default');
            $main.attr('method', 'get');
            $main.submit();
        });
    })
</script>
</body>
</html>