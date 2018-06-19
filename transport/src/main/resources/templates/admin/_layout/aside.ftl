<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/admin/product/index">海外仓后台管理系统</a>

            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">
                        货品管理<span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="/admin/product/index">所有货品</a></li>
                        <li><a href="/admin/product/index?status=0">待审核</a></li>
                        <li><a href="/admin/product/index?status=1">已审核</a></li>
                        <li><a href="/admin/product/index?status=2">已拒绝</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">
                        入库管理<span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="/admin/package/index">查看所有</a></li>
                        <li><a href="/admin/package/index?status=0">待入库</a></li>
                        <li><a href="/admin/package/index?status=1">待上架</a></li>
                    </ul>
                </li>

                <li><a href="/admin/stock/index">库存管理</a></li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">
                        出库单管理<span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="/admin/order/index">查看所有</a></li>
                        <li><a href="/admin/order/index?status=0">待审核</a></li>
                        <li><a href="/admin/order/index?status=1">待发货</a></li>
                        <li><a href="/admin/order/index?status=2">已发货</a></li>
                        <li><a href="/admin/order/index?status=3">已冻结</a></li>
                        <li><a href="/admin/order/index?status=4">审核失败</a></li>
                        <li><a href="/admin/order/index?status=5">已取消</a></li>
                    </ul>
                </li>

                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">
                        用户管理<span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="/admin/user/index">查看所有</a></li>
                    </ul>
                </li>
            </ul>

            <form action="/logout" method="post" id="logoutForm">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                <ul class="nav navbar-nav navbar-right">
                    <li><a class="active" href="javascript:void(0);">管理员：${USER.username}</a></li>
                    <li><a id="logout" href="javascript:void(0);">退出登录</a></li>
                </ul>
            </form>
        </div>
    </div>
</nav>

<#if SUCCESS?? >
<div class="message-container">
    <div class="alert alert-success alert-dismissible">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
        <h4><i class="icon fa fa-check"></i> 操作成功</h4>
    ${SUCCESS!}
    </div>
</div>
</#if>

<#if ERROR?? >
<div class="message-container">
    <div class="alert alert-success alert-dismissible">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
        <h4><i class="icon fa fa-check"></i> 操作失败</h4>
    ${ERROR!}
    </div>
</div>
</#if>