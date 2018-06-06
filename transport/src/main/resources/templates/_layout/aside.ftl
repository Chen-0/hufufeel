<header class="main-header">
    <!-- Logo -->
    <a href="../../index2.html" class="logo">
        <!-- mini logo for sidebar mini 50x50 pixels -->
        <span class="logo-mini"><b>A</b>LT</span>
        <!-- logo for regular state and mobile devices -->
        <span class="logo-lg"><b>HUFU</b>FEEL</span>
    </a>
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top">

        <ul class="nav navbar-nav">
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">货品管理 <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="/product/index">查看货品</a></li>
                    <li><a href="/product/create">添加货品</a></li>
                </ul>
            </li>

            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">入库管理<span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="/product/ready_to_send">新建入库单</a></li>
                    <li><a href="/package/index">入库单管理</a></li>
                </ul>
            </li>
        <#--货品管理-->
        <#--新建货品-->
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">库存管理<span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="/stock/index">查询库存</a></li>
                    <#--<li><a href="/package/index">入库单管理</a></li>-->
                </ul>
            </li>
        <#--查看库存-->
            <li><a href="#">订单管理</a></li>
        <#--创建出库单-->
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">Dropdown <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="#">Action</a></li>
                    <li><a href="#">Another action</a></li>
                    <li><a href="#">Something else here</a></li>
                    <li role="separator" class="divider"></li>
                    <li><a href="#">Separated link</a></li>
                    <li role="separator" class="divider"></li>
                    <li><a href="#">One more separated link</a></li>
                </ul>
            </li>
        </ul>


        <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">


                <li class="dropdown notifications-menu">
                    <#if MSG_COUNT?exists && MSG_COUNT gt 0>
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-bell-o"></i>
                        <span class="label label-warning">${MSG_COUNT}</span>
                    </a>
                    <ul class="dropdown-menu">
                        <li class="header">有 ${MSG_COUNT} 条未读信息</li>
                        <li>
                            <!-- inner menu: contains the actual data -->
                            <ul class="menu">
                                <#list MSG_ELEMENTS as e>
                                <li>
                                    <a href="/m/r?id=${e.id}"><i class="fa fa-users text-aqua"></i> ${e.context}</a>
                                </li>
                                </#list>
                            </ul>
                        </li>
                        <li class="footer"><a href="#">View all</a></li>
                    </ul>
                    <#else>
                        <a href="/message/index">
                            <i class="fa fa-bell-o"></i>
                        </a>
                    </#if>

                </li>

                <li class="user user-menu">
                    <a href="javascript:void(0)">
                        <img src="/static/LTE/user.jpg" class="user-image" alt="User Image">
                        <span class="hidden-xs">${USER.name}</span>
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
                <p>${USER.name}</p>
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
    <div class="alert alert-success alert-dismissible">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
        <h4><i class="icon fa fa-check"></i> 操作成功</h4>
    ${SUCCESS!}
    </div>
</div>
</#if>