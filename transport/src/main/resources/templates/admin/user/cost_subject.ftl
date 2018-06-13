<!DOCTYPE html>
<html lang="en">
<#assign title="费用设置" />
<#include "*/admin/_layout/head.ftl" />

<body>
<#include "*/admin/_layout/aside.ftl" />

<div class="container-fluid">
    <div class="row">
        <div class="col-lg-12">
            <h1 class="page-header">
            ${title}
                <small>${user.name}</small>
            </h1>
        </div>
    </div>

<#if success?? >
    <div class="alert alert-success alert-dismissable alert-message">
        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
    ${success}
    </div>
</#if>

    <div class="row">
        <div class="col-xs-8 col-xs-offset-2">
            <form class="form-horizontal">

                <div class="panel panel-info">
                    <div class="panel-heading">
                        入库费用设置
                    </div>
                    <div class="panel-body">
                        <div class="form-group">
                            <div class="col-sm-4 radio">
                                <label>
                                    <input type="radio" name="rkt" value="RK-AZ" checked="">按重收费（USD）
                                </label>
                            </div>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="INP-RK-AZ">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-4 radio">
                                <label>
                                    <input type="radio" name="rkt" value="RK-AX">按箱收费（USD）
                                </label>
                            </div>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="INP-RK-AX">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-4 radio">
                                <label>
                                    <input type="radio" name="rkt" value="RK-AF">免费
                                    <input type="hidden" class="form-control" id="INP-RK-AF" value="0">
                                </label>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="panel panel-info">
                    <div class="panel-heading">
                        上架费用设置
                    </div>
                    <div class="panel-body">
                        <div class="form-group">
                            <div class="col-sm-4 radio">
                                <label>
                                    <input type="radio" name="sjt" value="SJ-AS" checked="">按体积收费（USD）
                                </label>
                            </div>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="INP-SJ-AS">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-4 radio">
                                <label>
                                    <input type="radio" name="sjt" value="SJ-AJ">按件收费（USD）
                                </label>
                            </div>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="INP-SJ-AJ">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-4 radio">
                                <label>
                                    <input type="radio" name="sjt" value="SJ-AF">免费
                                    <input type="hidden" class="form-control" id="INP-SJ-AF" value="0">
                                </label>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="panel panel-info">
                    <div class="panel-heading">
                        订单费用设置
                    </div>
                    <div class="panel-body">
                        <div class="form-group">
                            <div class="col-sm-4 radio">
                                <label>
                                    <input type="radio" name="ddt" value="DD-AZ" checked="">按重量收费（USD）
                                </label>
                            </div>
                            <div class="col-sm-2">1公斤内</div>
                            <div class="col-sm-2">
                                <select class="form-control" id="INP_DD_AZ_1">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                </select>
                            </div>
                            <div class="col-sm-2">超件费</div>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="INP_DD_AZ_2">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-2 col-sm-offset-4">超1公斤=0.3USD+</div>
                            <div class="col-sm-2">
                                <select class="form-control" id="INP_DD_AZ_3">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                </select>
                            </div>
                            <div class="col-sm-2">超件费</div>
                            <div class="col-sm-2">
                                <input type="text" class="form-control" id="INP_DD_AZ_4">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-4 radio">
                                <label>
                                    <input type="radio" name="ddt" value="DD-AJ">按件收费（USD）
                                </label>
                            </div>
                            <div class="col-sm-6">
                                <input type="text" class="form-control" id="INP-DD-AJ">
                            </div>
                        </div>
                    </div>
                    <div class="panel-footer">
                        <button id="submit" class="btn btn-primary" type="button">提交</button>
                    </div>
                </div>
            </form>

        </div>
    </div>
</div>

<#include "*/admin/_layout/script.ftl"/>
<script>
    $(function () {
        $('#submit').click(function (e) {
            e.preventDefault();

            var rkt = $('input[name=rkt]:checked').val();
            var rkv = $('#INP-' + rkt).val();

            var sjt = $('input[name=sjt]:checked').val();
            var sjv = $('#INP-' + sjt).val();

            var ddt = $('input[name=ddt]:checked').val();
            var ddv = [];

            if (ddt === 'DD-AZ') {
                ddv.push($('#INP_DD_AZ_1').val());
                ddv.push($('#INP_DD_AZ_2').val());
                ddv.push($('#INP_DD_AZ_3').val());
                ddv.push($('#INP_DD_AZ_4').val());
            } else if(ddt === 'DD-AJ') {
                ddv.push($('#INP-DD-AJ').val());
            }

            var data = {};
            data.rkt = rkt;
            data.rkv = rkv;
            data.sjt = sjt;
            data.sjv = sjv;
            data.ddt = ddt;
            data.ddv = ddv;

        <#if _csrf??>
            data.${_csrf.parameterName}="${_csrf.token}";
        </#if>

            console.log(data);
            post('/admin/user/${user.id}/cost_subject', data)
        });

        function post(url, args) {
            var body = $(document.body),
                    form = $("<form method='post'></form>"),
                    input;
            form.attr({"action": url});
            $.each(args, function (key, value) {
                if (value instanceof Array) {
                    input = $("<input type='hidden'>");
                    input.attr({"name": key + '[]'});
                    input.val(value);
                    form.append(input);
                } else {
                    input = $("<input type='hidden'>");
                    input.attr({"name": key});
                    input.val(value);
                    form.append(input);
                }
            });

            form.appendTo(document.body);
            form.submit();
            document.body.removeChild(form[0]);
        }
    })
</script>
</body>
</html>