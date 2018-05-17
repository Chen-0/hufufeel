<!DOCTYPE html>
<html>

<#include "*/_layout/head.ftl" />

<body class="theme-blue">
<#include "*/_layout/aside.ftl" />

<section style="margin-top: 80px;">
    <div class="container">

        <div class="row">
            <div class="col-md-4">
                <div class="card">
                    <div class="header">
                        <h2>
                            热门品牌
                        </h2>
                    </div>
                    <div class="body">
                    <#list carBrands as carBrand>
                        <div class="media" style="margin-bottom: 0;">
                            <div class="media-left">
                                <img class="media-object" src="/file/${carBrand.image.pathName}" width="64" height="64">
                            </div>
                            <div class="media-body" style="line-height: 65px; font-size: 20px; font-weight: bold;">
                            ${carBrand.name}
                            </div>
                        </div>
                    </#list>
                    </div>
                </div>
            </div>
            <div class="col-md-8">
                <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
                    <!-- Indicators -->
                    <ol class="carousel-indicators">
                        <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
                        <li data-target="#carousel-example-generic" data-slide-to="1" class=""></li>
                        <li data-target="#carousel-example-generic" data-slide-to="2" class=""></li>
                    </ol>

                    <!-- Wrapper for slides -->
                    <div class="carousel-inner" role="listbox">
                        <div class="item active">
                            <img src="http://img.xgo-img.com.cn/pics/3480/400/300/3479813.jpg"
                                 style="width: 100%; height: auto;">
                        </div>
                        <div class="item">
                            <img src="http://img.xgo-img.com.cn/pics/3479/400/300/3478365.jpg"
                                 style="width: 100%; height: auto;">
                        </div>
                        <div class="item">
                            <img src=" http://img.xgo-img.com.cn/pics/3480/400/300/3479798.jpg"
                                 style="width: 100%; height: auto;">
                        </div>
                    </div>

                    <!-- Controls -->
                    <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
                        <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                        <span class="sr-only">Previous</span>
                    </a>
                    <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
                        <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                        <span class="sr-only">Next</span>
                    </a>
                </div>
            </div>
        </div>

        <div class="card" style="margin-top: 25px;">
            <div class="header">
                <h2 style="text-align: center;">
                    主推文章
                </h2>
            </div>
            <div class="body">
                <div class="list-group">
                <#list articles as article >
                    <a href="/article/${article.id}" class="list-group-item">
                    ${article.title}
                    </a>
                </#list>
                </div>
            </div>
        </div>


        <div class="card" style="margin-top: 25px;">
            <div class="header">
                <h2 style="text-align: center;">
                    主推车型
                </h2>
            </div>
            <div class="body">
                <div class="list-group">
                    <#list carModels as carModel >
                    <a href="/car/model/${carModel.id}" class="list-group-item">
                        ${carModel.name}
                        <span style="float: right;" class="text-danger">厂家指导价：${carModel.price}￥</span>
                    </a>
                    </#list>
                </div>
            </div>
        </div>


    </div>
</section>

<#include "*/_layout/script.ftl" />
</body>

</html>