<!DOCTYPE html>
<html>

<#include "*/admin/_layout/head.ftl" />

<body class="theme-red">
<#include "*/admin/_layout/aside.ftl" />

<section class="content">
    <div class="container-fluid">
        <div class="block-header">
            <h2>
                创建车型
                <small>创建品牌下的车型</small>
            </h2>
        </div>
    </div>

    <div class="card">

        <div class="body">
            <form method="post" action="/admin/car/model/create">
                <label for="name">车型名称：</label>
                <div class="form-group">
                    <div class="form-line">
                        <input type="text" id="name" class="form-control" placeholder="华晨宝马-宝马5系 2018款 528Li 上市特别版" name="name">
                    </div>
                </div>

                <label for="carSerial">所属车系：</label>
                <div class="form-group">
                    <div class="form-line">
                        <select name="carSerialId" id="carSerial">
                        <#list carSerials as carSerial >
                            <option value="${carSerial.id}">${carSerial.name}</option>
                        </#list>
                        </select>
                    </div>
                </div>

                <label for="price">厂家指导价：（请输入数字，单位是元）</label>
                <div class="form-group">
                    <div class="form-line">
                        <input type="text" id="price" class="form-control" placeholder="1000000" name="price">
                    </div>
                </div>

                <#--编辑车型的属性-->
                <#list attributes as attribute>
                <div class="row clearfix">
                    <div class="col-sm-6">
                        <div class="form-group">
                            <div class="form-line">
                                <input type="hidden" name="id[]" value="${attribute.id}">
                                <input type="text" class="form-control" value="${attribute.name}" disabled>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-6">
                        <div class="form-group">
                            <div class="form-line">
                                <input type="text" class="form-control" name="value[]">
                            </div>
                        </div>
                    </div>
                </div>
                </#list>

                <button type="submit" class="btn btn-primary m-t-15 waves-effect">提交</button>
            </form>
        </div>
    </div>
</section>

<#include "*/admin/_layout/script.ftl" />
</body>

</html>