<#if USER.isAdmin() >
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
            </ul>


            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">
                        大客户运单管理<span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="/order/index?status=1">待入库运单</a></li>
                        <li><a href="/order/index?status=2">待出库运单</a></li>
                        <li><a href="/order/index?status=3">已出库运单</a></li>
                        <li><a href="/order/index?status=99">含有错误的运单</a></li>
                        <li><a href="/order/index?status=100">待审核的运单</a></li>
                    </ul>
                </li>
            </ul>


            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">
                        海淘客用户管理<span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="/user/index">会员中心</a></li>
                        <li><a href="/user/address/index">待审核的地址</a></li>
                    </ul>
                </li>
            </ul>

            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">
                        大客户管理<span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="/company/index">用户管理</a></li>
                        <li><a href="/company/express/index">大客户发货渠道</a></li>
                    </ul>
                </li>
            </ul>

            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">
                        海淘客数据统计<span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="/analysis/user/paymoney">本月累计充值</a></li>
                    </ul>
                </li>
            </ul>

            <form action="/logout" method="post" id="logoutForm">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">

                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                           aria-expanded="false">${USER.username} <span class="caret"></span></a>
                    <#--<ul class="dropdown-menu">-->
                    <#--<li><a href="#">Action</a></li>-->
                    <#--<li><a href="#">Another action</a></li>-->
                    <#--<li><a href="#">Something else here</a></li>-->
                    <#--<li role="separator" class="divider"></li>-->
                    <#--<li><a href="#">Separated link</a></li>-->
                    <#--</ul>-->
                    </li>

                    <li><a id="logout" href="javascript:void(0);">退出登录</a></li>
                </ul>
            </form>
        </div>
    </div>
</nav>
<#else >

<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <img src="http://www.hufufeel.com/Logistics/public/Images/logo1.png" alt=""
            style="height: 50px; float: left; margin-right: 25px; margin-left:10px;">
            <a class="navbar-brand" href="/">HUFU大客户中心</a>

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
                        运单管理<span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="/order/index?status=1" id="ruku">待入库的运单</a></li>
                        <li><a href="/order/index?status=2" id="fahuo">待发货的运单</a></li>
                        <li><a href="/order/index?status=3" id="song">已发货的运单</a></li>
                        <li><a href="/order/index?status=99">信息有误的运单</a></li>
                    </ul>
                </li>
            </ul>


            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">
                        数据导入<span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="/order/excel/upload">导入运单</a></li>
                    </ul>
                </li>
            </ul>

            <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                       aria-expanded="false">
                        信息<span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="javascript:void(0)">大客户名称：${USER.name}</a></li>
                        <li><a href="javascript:void(0)">余额：${USER.money}</a></li>
                        <#list USER.companyRExpressList as o >
                            <li><a href="javascript:void(0)">${o.companyExpress.name}：${o.price}</a></li>
                        </#list>
                        <li><a href="javascript:void(0);" id="logout">退出</a></li>
                    </ul>
                </li>
            </ul>

            <form action="/logout" method="post" id="logoutForm">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
            </form>
        </div>
    </div>
</nav>

<#--<nav class="navbar navbar-default" role="navigation">-->
    <#--<div class="container-fluid">-->
        <#--<!-- Brand and toggle get grouped for better mobile display &ndash;&gt;-->
        <#--<div class="navbar-header">-->
            <#--<img src="http://www.hufufeel.com/Logistics/public/Images/logo1.png" alt=""-->
                 <#--style="height: 50px; float: left;">-->
            <#--<a class="navbar-brand" href="javascript:void(0)">HUFU</a>-->
        <#--</div>-->

        <#--<!-- Collect the nav links, forms, and other content for toggling &ndash;&gt;-->
        <#--<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">-->
            <#--<ul class="nav navbar-nav">-->
                <#--<!--<li><a href="">导入/</a></li>&ndash;&gt;-->
                <#--<li class="dropdown">-->
                    <#--<a href="#" class="dropdown-toggle" data-toggle="dropdown">运单管理 <span class="caret"></span></a>-->
                    <#--<ul class="dropdown-menu" role="menu">-->
                        <#--<li><a href="/order/index?status=1" id="ruku">待入库的运单</a></li>-->
                        <#--<li><a href="/order/index?status=2" id="fahuo">待发货的运单</a></li>-->
                        <#--<li><a href="/order/index?status=3" id="song">已发货的运单</a></li>-->
                        <#--<li><a href="/order/index?status=99">信息有误的运单</a></li>-->
                    <#--</ul>-->
                <#--</li>-->

                <#--<li class="dropdown">-->
                    <#--<a href="#" class="dropdown-toggle" data-toggle="dropdown">数据处理 <span class="caret"></span></a>-->
                    <#--<ul class="dropdown-menu" role="menu">-->
                        <#--<li><a href="/order/excel/upload">导入运单</a></li>-->
                    <#--</ul>-->
                <#--</li>-->


            <#--</ul>-->

            <#--<form action="/logout" method="post" id="logoutForm">-->
                <#--<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">-->
                <#--<ul class="nav navbar-nav">-->
                    <#--<li class="dropdown">-->
                        <#--<a href="#" class="dropdown-toggle" data-toggle="dropdown">信息 <span class="caret"></span></a>-->

                        <#--<ul class="dropdown-menu" role="menu">-->
                            <#--<li><a href="javascript:void(0)">大客户名称：${USER.name}</a></li>-->
                            <#--<li><a href="javascript:void(0)">余额：${USER.money}</a></li>-->
                            <#--<#list USER.companyRExpressList as o >-->
                                <#--<li><a href="javascript:void(0)">${o.companyExpress.name}：${o.price}</a></li>-->
                            <#--</#list>-->
                            <#--<li><a href="javascript:void(0);" id="logout">退出</a></li>-->
                        <#--</ul>-->

                    <#--</li>-->
                <#--</ul>-->
            <#--</form>-->
        <#--</div><!-- /.navbar-collapse &ndash;&gt;-->
    <#--</div><!-- /.container-fluid &ndash;&gt;-->
<#--</nav>-->

</#if>
