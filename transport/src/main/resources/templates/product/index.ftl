<!DOCTYPE html>
<html>
<#assign TITLE = "货品管理">
<#include "*/_layout/head.ftl" />

<body class="hold-transition skin-black-light sidebar-mini">


<div class="wrapper">
<#include "*/_layout/aside.ftl" />

    <div class="content-wrapper">
        <section class="content-header">
            <h1>
                货品管理
            </h1>
            <ol class="breadcrumb">
                <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                <li><a href="#">Examples</a></li>
                <li class="active">Blank page</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">

            <div class="box">
                <div class="box-body">
                    <form class="form-inline margin-bottom" role="form" method="get" action="/product/index">
                        <div class="form-group">
                            <label for="keyword">关键字：</label>
                            <input class="form-control" id="keyword" name="keyword" type="text" value="${keyword!}">
                        </div>
                        <div class="form-group">
                            <button type="submit" class="btn btn-primary">搜索</button>
                            <a href="/product/index" class="btn btn-default">重置</a>
                        </div>
                    </form>

                    <form id="selectForm" action="/product/select" method="post">
                        <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>
                        <table class="table table-bordered table-striped table-condensed table-hover">
                            <thead>
                            <tr>
                                <th width="1px"><input type="checkbox" id="select_all"></th>
                                <th>编号</th>
                                <th>货品名称</th>
                                <th>货品SKU</th>
                                <th>电池类型</th>
                                <th>原产地</th>
                                <th>重量</th>
                                <th>体积</th>
                                <th>有效期</th>
                                <th>危险品</th>
                                <th>申报价值</th>
                                <th>申报名称</th>
                                <th>状态</th>
                            <#if status??>
                                <#if status == 2>
                                    <th width="40">失败原因</th>
                                </#if>
                            </#if>
                                <th width="40">备注</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <#list elements.getContent() as e>
                            <tr>
                                <th><input class="x-checkbox" type="checkbox" name="trackingNumber[]" value="${e.id}"></th>
                                <td>${e.id}</td>
                                <td>${e.productName}</td>
                                <td>${e.productSku}</td>
                                <td>${e.isBattery?string("是", "否")}</td>
                                <td>${e.origin}</td>
                                <td>${e.weight}kg</td>
                                <td>${e.vol} 立方米</td>
                                <td>
                                    <#if e.deadline??>
                                ${e.deadline?date}
                            </#if>
                                </td>
                                <td>${e.isDanger?string("是", "否")}</td>
                                <td>${e.quotedPrice}</td>
                                <td>${e.quotedName}</td>
                                <td>${e.status.getValue()}</td>
                                <#if status??>
                                    <#if status == 2>
                                        <td>${e.reason!}</td>
                                    </#if>
                                </#if>
                                <td>${e.comment!}</td>
                                <td>
                                    <a href="/product/${e.id}/show">查看</a>
                                    <a href="/product/${e.id}/update">更新</a>
                                    <a class="x-remove" href="/product/${e.id}/remove">删除</a>
                                </td>
                            </tr>
                            </#list>
                            </tbody>
                        </table>
                    </form>
                </div>

                <div class="box-footer clearfix">
                    <#if _STATUS?exists && ( _STATUS == 1) >
                    <button id="addToSend" class="btn btn-primary">添加到入库单</button>
                    </#if>
                <#assign BASEURL="/product/index?keyword=${keyword}&status=${_STATUS!}&page="/>
                <#include "*/_layout/v2.0/components/pages.ftl">
                </div>
            </div>


        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->

<#include "*/_layout/footer.ftl"/>
</div>

<#include "*/_layout/script.ftl" />
<script>
    $(function () {
        $('#select_all').click(function () {
            if($(this).is(":checked")) {
                changeSelect(true);
            } else {
                changeSelect(false);
            }
        });

        function changeSelect(value) {
            $('.x-checkbox').prop("checked", value);
        }


        $("#addToSend").click(function (e) {
            e.preventDefault();

            if($(".x-checkbox:checked").length === 0) {
                alert("请选择一件或多件货品");
                return;
            }

            $('#selectForm').submit();
        });
    })
</script>
</body>
</html>
