<!DOCTYPE html>
<html>

<#include "*/_layout/head.ftl" />

<body class="theme-blue">
<#include "*/_layout/aside.ftl" />

<section style="margin-top: 80px;">
    <div class="container">
        <ol class="breadcrumb breadcrumb-col-cyan">
            <li><a href="/"><i class="material-icons">home</i> 首页</a></li>
            <li><a href="/article/"><i class="material-icons">library_books</i> 热门车型</a></li>
        </ol>

    <#list brands as brand >
        <div class="card">
            <div class="header">
                <div class="media" style="margin-bottom: 0;">
                    <div class="media-left">
                        <a href="javascript:void(0);">
                            <img class="media-object" src="/file/${brand.image.pathName}" width="64" height="64">
                        </a>
                    </div>
                    <div class="media-body" style="line-height: 65px; font-size: 20px; font-weight: bold;">
                    ${brand.name}
                    </div>
                </div>
            </div>
            <div class="body">
                <div style="margin-left: 25px;">
                    <div class="list-group">
                        <#list brand.carSerials as serial>
                        <a href="/car/serial/${serial.id}" class="list-group-item">
                        ${serial.name}

                        </a>
                        </#list>
                    </div>
                </div>
            </div>
        </div>
    </#list>
    </div>
</section>

<#include "*/_layout/script.ftl" />
</body>
</html>
