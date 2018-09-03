<header class="main-header">
    <a href="/" class="logo">
        <span class="logo-lg"><img src="/static/favicon.ico" alt=""><b>HUFU</b>FEEL</span>
    </a>
    <nav class="navbar navbar-static-top">

        <ul class="nav navbar-nav">
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">货品管理 <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="/product/index">查看货品</a></li>
                    <li><a href="/product/create">添加货品</a></li>
                    <li><a href="/product/import">导入货品</a></li>
                </ul>
            </li>

            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">入库管理<span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="/product/ready_to_send">新建入库单</a></li>
                    <li><a href="/package/import">导入入库单</a></li>
                    <li><a href="/package/index">入库单管理</a></li>
                </ul>
            </li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">库存管理<span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="/stock/index">库存管理</a></li>
                    <li><a href="/switch_sku/index">换标记录 & 提交</a></li>
                </ul>
            </li>

            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">发货单管理<span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="/order/create">新建发货单</a></li>
                    <li><a href="/order/import">导入发货单</a></li>
                    <li><a href="/order/index">发货单管理</a></li>
                </ul>
            </li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">退货单管理<span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="/product/index?type=1">退货入库</a></li>
                    <li><a href="/package/index?type=1">退货单管理</a></li>
                </ul>
            </li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">个人中心<span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="/user/index">我的账户</a></li>
                    <li><a href="/user/statements/index">费用流水</a></li>
                </ul>
            </li>
            <li><a href="/user/charge_account">充值中心</a></li>
        </ul>


        <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">

                <li class="dropdown notifications-menu">
                    <#if MSG_COUNT?exists && MSG_COUNT gt 0>
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        消息中心<i class="fa fa-bell-o"></i>
                        <span class="label label-warning">${MSG_COUNT}</span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="header">有 ${MSG_COUNT} 条未读信息</li>
                        <li>
                            <ul class="menu">
                                <#list MSG_ELEMENTS as e>
                                <li>
                                    <a href="/m/r?id=${e.id}"><i class="fa fa-users text-aqua"></i> ${e.context}</a>
                                </li>
                                </#list>
                            </ul>
                        </li>
                        <li class="footer"><a href="/message/index">查看全部</a></li>
                    </ul>
                    <#else>
                        <a href="/message/index">
                            <i class="fa fa-bell-o"></i>
                        </a>
                    </#if>

                </li>

                <li class="user user-menu">
                    <a href="/user/index">
                        <img src="/static/LTE/user.jpg" class="user-image" alt="User Image">
                        <span class="hidden-xs">${USER.name} <small>No.${USER.hwcSn}</small></span>
                    </a>
                </li>
                <li>
                    <a id="logoutBtn" href="javascript:void(0)">注销</a>
                    <form id="logoutForm" action="/logout" method="post">
                        <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>
                    </form>
                </li>
            </ul>
        </div>
    </nav>
</header>


<aside class="main-sidebar">
    <section class="sidebar">
        <div class="user-panel">
            <div class="pull-left image">
                <img src="/static/LTE/user.jpg" class="img-circle" alt="User Image">
            </div>
            <div class="pull-left info">
                <p>${USER.name} <small>No.${USER.hwcSn}</small></p>
                <a href="#"><i class="fa fa-circle text-success"></i> 在线</a>
            </div>
        </div>

        <form action="#" method="get" class="sidebar-form">
            <div class="input-group">
                <input type="text" name="q" class="form-control" placeholder="Search...">
                <span class="input-group-btn">
                <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
                </button>
              </span>
            </div>
        </form>

        <ul class="sidebar-menu" data-widget="tree">
        <#if MENU??>
            <li class="header">LABELS</li>
            <#list MENU as m>
                <li><a href="${m.url}"><i class="fa fa-circle-o text-red"></i> <span>${m.name}</span></a></li>
            </#list>
        </#if>
        </ul>
    </section>
</aside>

<#if SUCCESS?? >
<div class="message-container">
    <div class="alert alert-success alert-dismissible" style="margin: 0;">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
        <h4><i class="icon fa fa-check"></i> 操作成功</h4>
    ${SUCCESS!}
    </div>
</div>
</#if>

<#if ERROR?? >
<div class="message-container">
    <div class="alert alert-danger alert-dismissible" style="margin: 0;">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
        <h4><i class="icon fa fa-ban"></i> 操作失败</h4>
    ${ERROR!}
    </div>
</div>
</#if>