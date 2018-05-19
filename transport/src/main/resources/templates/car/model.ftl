<!DOCTYPE html>
<html>

<#include "*/_layout/head.ftl" />

<body class="theme-blue">
<#include "*/_layout/aside.ftl" />

<section style="margin-top: 80px;">
    <div class="container">
        <ol class="breadcrumb breadcrumb-col-cyan">
            <li><a href="/"><i class="material-icons">home</i> 首页</a></li>
            <li><a href="/car/index"><i class="material-icons">library_books</i> 热门车型</a></li>
            <li><a href="/car/serial/${carSerial.id}"><i class="material-icons">library_books</i> ${carSerial.name}</a>
            </li>
            <li class="active"><i class="material-icons">archive</i> ${carModel.name}</li>
        </ol>

        <div class="card">
            <div class="header">
                <h2 style="text-align: center;">
                    概况
                </h2>
            </div>

            <div class="body">
                <div class="row">
                    <div class="col-lg-4 col-md-4 col-md-offset-1">
                        <img class="img-responsive thumbnail" src="/file/${carSerial.images[0].pathName}">
                    </div>

                    <div class="col-md-offset-1 col-md-6">
                        <p class="lead">
                            品牌：${carModel.carSerial.carBrand.name}
                        </p>

                        <p class="lead">
                            所属车系：${carModel.carSerial.name}
                        </p>

                        <p class="lead">
                            车型：${carModel.name}
                        </p>

                        <p class="lead">
                            厂家指导价：${carModel.price} ￥
                        </p>

                        <a href="/car/model/${carModel.id}/star" class="btn btn-primary waves-effect">
                            <#if star >
                                取消收藏
                            <#else>
                                收藏
                            </#if>
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="header">
                <h2 style="text-align: center;">
                ${carModel.name} 的相关图片
                </h2>
            </div>

            <div class="body">
                <div id="aniimated-thumbnials" class="list-unstyled row clearfix">
                <#list carSerial.images as image >
                    <div class="col-lg-3 col-md-4 col-sm-6 col-xs-12">
                        <a href="/file/${image.pathName}" data-sub-html="Demo Description">
                            <img class="img-responsive thumbnail" src="/file/${image.pathName}">
                        </a>
                    </div>
                </#list>
                </div>
            </div>
        </div>

        <div class="card">
            <div class="header">
                <h2 style="text-align: center;">
                ${carModel.name} 的详细参数
                </h2>
            </div>

            <div class="body">
                <table class="table table-bordered">
                    <tbody>
                    <#list carModel.attributes as attribute>
                    <tr>
                        <th scope="row">${attribute_index}</th>
                        <td>${attribute.attribute.name}</td>
                        <td>${attribute.value}</td>
                    </tr>
                    </#list>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</section>

<#include "*/_layout/script.ftl" />
</body>

</html>