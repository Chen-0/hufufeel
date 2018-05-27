<!-- Nav -->
<div class="container-fluid black">
    <div class="container">
        <div class="row" style="padding: 10px 0;">
            <div class="col col-md-offset-1 col-md-5">
                <img class="brand-logo" src="/static/assets/images/logo.png" alt="">
                <div class="brand-name">
                    <h3 class="white-text">HUFU <span class="text-primary">FEEL</span></h3>
                    <p class="white-text">http://www.hufufeel.com</p>
                </div>
            </div>

            <#if USER??>
            <div id="menu-container" class="col col-md-offset-2 col-md-4">
                <div class="row">
                    <div class="col-md-12 white-text margin-bottom" style="margin-top: 1rem;">
                        <span style="position:relative;">您好，<span id="username">${USER.userna!}</span>
                             <a class="new hide" href="/Logistics/index.php/UserService/uMessage">NEWS</a>
                        </span>
                    </div>
                </div>
                <div style="display: inline-block; position: relative;">
                    <img src="/static/assets/images/user-center.png" style="width: 2.5rem;">
                    <a class="white-text brand-anchor" href="/product/index">用户中心</a>
                </div>
                <div style="display: inline-block;margin-left: 2em;">
                    <img src="/static/assets/images/money.png" style="width: 2.5rem;">
                    <a class="white-text brand-anchor" href="#" >余额充值</a>
                </div>
            </div>
            <#else>
            <div id="login-container" class="col col-md-offset-3 col-md-3" style="padding-top: 25px;">
                <div style="display: inline-block;">
                    <a class="white-text brand-anchor" href="/login">登录</a>
                </div>
                <div style="display: inline-block;margin-left: 2em;">
                    <a class="white-text brand-anchor" href="javascript:void(0);">注册</a>
                </div>
                <div style="display: inline-block;margin-left: 2em;">
                    <a class="white-text brand-anchor company_login" href="http://admin.hufufeel.com/">大客户进入通道</a>
                </div>
            </div>
            </#if>
        </div>
    </div>
</div>
<nav class="navbar navbar-default" role="navigation">
    <div class="container">
        <div class="row"></div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li><a href="/index">首页</a></li>
                <li><a href="/about_us">关于我们</a></li>
                <#--<li><a href="{:U('News/index')}">特惠公告</a></li>-->
                <li><a href="">使用教程</a></li>
                <li><a href="/cost">服务费用</a></li>
                <li><a href="/qa">常见问题</a></li>
                <li><a href="/strategy">海淘攻略</a></li>
                <li><a href="/contact_us">联系我们</a></li>
            </ul>
        </div>
    </div>
</nav>
<!-- Nav -->
