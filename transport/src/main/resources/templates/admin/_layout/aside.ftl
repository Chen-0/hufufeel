<nav class="navbar">
    <div class="container-fluid">
        <div class="navbar-header">
            <a href="javascript:void(0);" class="navbar-toggle collapsed" data-toggle="collapse"
               data-target="#navbar-collapse" aria-expanded="false"></a>
            <a href="javascript:void(0);" class="bars"></a>
            <a class="navbar-brand" href="/admin/index">汽车信息采集系统</a>
        </div>
    <#if USER??>
        <div class="collapse navbar-collapse" id="navbar-collapse">
            <ul class="nav navbar-nav navbar-right">
                <li>
                    <a href="javascript:void(0);" class="js-search" data-close="true">
                    ${USER.username}
                    </a>
                </li>
                <li>
                    <a href="/" class="js-search" data-close="true">
                        回到首页
                    </a>
                </li>
                <li>
                    <a href="/logout">登出</a>
                </li>
            </ul>
        </div>
    </#if>
    </div>
</nav>
<!-- #Top Bar -->
<section>
    <!-- Left Sidebar -->
    <aside id="leftsidebar" class="sidebar">
        <!-- User Info -->
        <div class="user-info"></div>
        <!-- #User Info -->
        <!-- Menu -->
        <div class="menu">
            <ul class="list">
                <li class="header">MAIN NAVIGATION</li>
                <li class="active">
                    <a href="javascript:void(0);" class="menu-toggle">
                        <i class="material-icons">content_copy</i>
                        <span>文章管理</span>
                    </a>
                    <ul class="ml-menu">
                        <li>
                            <a href="/admin/article/catch/start">文章爬虫</a>
                        </li>
                        <li>
                            <a href="/admin/tag/index">文章标签管理</a>
                        </li>
                        <li>
                            <a href="/admin/article/index">文章管理</a>
                        </li>
                    </ul>
                </li>
                <li class="active">
                    <a href="javascript:void(0);" class="menu-toggle">
                        <i class="material-icons">pie_chart</i>
                        <span>车型管理</span>
                    </a>
                    <ul class="ml-menu">
                        <li>
                            <a href="/admin/car/brand/index">品牌管理</a>
                        </li>
                        <li>
                            <a href="/admin/car/serial/index">车系管理</a>
                        </li>
                        <li>
                            <a href="/admin/car/model/index">车型管理</a>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
        <!-- #Menu -->
        <!-- Footer -->
        <div class="legal">
            <div class="copyright">
                &copy; 2017 <a href="javascript:void(0);">Power By 王杰</a>.
            </div>
            <div class="version">
                <b>Version: </b> 1.0.0
            </div>
        </div>
    </aside>
</section>