<nav class="navbar">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="/">汽车信息采集系统</a>
        </div>

        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li>
                    <a href="/" class="js-search" data-close="true">
                        首页
                    </a>
                </li>
                <li>
                    <a href="/article/">热门文章</a>
                </li>
                <li>
                    <a href="/car/index">热门车型</a>
                </li>
            </ul>


            <div class="collapse navbar-collapse" id="navbar-collapse">
                <ul class="nav navbar-nav navbar-right">
                <#if USER??>
                    <li>
                        <a href="javascript:void(0);" class="js-search" data-close="true">
                        ${USER.username}
                        </a>
                    </li>
                    <li>
                        <#if USER.isAdmin()>
                        <a href="/admin/index" class="js-search" data-close="true">
                            管理后台
                        </a>
                        </#if>
                    </li>
                    <li>
                        <a href="/logout">登出</a>
                    </li>
                <#else>
                    <li>
                        <a href="/login">登录</a>
                    </li>
                    <li>
                        <a href="/register">注册</a>
                    </li>
                </#if>
                </ul>
            </div>

        </div>


    </div>
</nav>
