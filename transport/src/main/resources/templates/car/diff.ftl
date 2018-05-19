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
            <li><a href="/car/serial/${carA.carSerialId}"><i class="material-icons">library_books</i> ${carA.carSerial.name}</a></li>
            <li class="active"><i class="material-icons">archive</i>车型对比</li>
        </ol>


        <div class="card">
            <div class="body">
                <table class="table table-bordered">
                    <tbody>

                    <tr>
                        <th scope="row">车型：</th>
                        <td colspan="2">${carA.name}</td>
                        <td colspan="2">${carB.name}</td>
                    </tr>

                    <#list carA.attributes as attribute>
                    <tr>
                        <th scope="row">${attribute_index}</th>
                        <td>${attribute.attribute.name}</td>
                        <td>${attribute.value}</td>

                        <#if carB.attributes[attribute_index].value == attribute.value>
                            <td class="success">${carB.attributes[attribute_index].attribute.name}</td>
                            <td class="success">${carB.attributes[attribute_index].value}</td>
                        <#else>
                            <td class="danger">${carB.attributes[attribute_index].attribute.name}</td>
                            <td class="danger">${carB.attributes[attribute_index].value}</td>
                        </#if>

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