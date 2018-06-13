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
                                    <#assign bar= (cs.rkt == "RK-AZ")>
                                    <input type="radio" name="rkt" value="RK-AZ" ${bar?string("checked", "")}>按重收费（USD）
                                </label>
                            </div>
                            <div class="col-sm-6">
                            <select class="form-control" id="INP-RK-AZ">
                                <#list INP_RK_AZ as v>
                                    <#assign foo=false>
                                    <#if bar>
                                    <#assign foo=(cs.rkv?string == v)>
                                    </#if>
                                        <option value="${v}" ${foo?string("selected", "")}>${v}</option>
                                </#list>
                            </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-4 radio">
                                <label>
                                    <#assign bar= (cs.rkt == "RK-AX")>
                                    <input type="radio" name="rkt" value="RK-AX" ${bar?string("checked", "")}>按箱收费（USD）
                                </label>
                            </div>
                            <div class="col-sm-6">
                            <select class="form-control" id="INP-RK-AX">
                            <#list INP_RK_AX as v>
                                <#assign foo=false>
                                <#if bar>
                                    <#assign foo=(cs.rkv?string == v)>
                                </#if>
                                    <option value="${v}" ${foo?string("selected", "")}>${v}</option>
                            </#list>
                            </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-4 radio">
                                <label>
                                <#assign bar= (cs.rkt == "RK-AF")>
                                    <input type="radio" name="rkt" value="RK-AF" ${bar?string("checked", "")}>免费
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
                                    <#assign bar= (cs.sjt == "SJ-AS")>
                                    <input type="radio" name="sjt" value="SJ-AS" ${bar?string("checked", "")}>按体积收费（USD）
                                </label>
                            </div>
                            <div class="col-sm-6">
                            <select class="form-control" id="INP-SJ-AS">
                            <#list INP_SJ_AS as v>
                                <#assign foo=false>
                                <#if bar>
                                    <#assign foo=(cs.sjv?string == v)>
                                </#if>


                                    <option value="${v}" ${foo?string("selected", "")}>${v}</option>

                            </#list>
                            </select>
                                <#--<input type="text" class="form-control" id="INP-SJ-AS">-->
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-4 radio">
                                <label>
                                    <#assign bar= (cs.sjt == "SJ-AJ")>
                                    <input type="radio" name="sjt" value="SJ-AJ" ${bar?string("checked", "")}>按件收费（USD）
                                </label>
                            </div>
                            <div class="col-sm-6">
                            <select class="form-control" id="INP-SJ-AJ">
                            <#list INP_SJ_AJ as v>
                                <#assign foo=false>
                                <#if bar>
                                    <#assign foo=(cs.sjv?string == v)>
                                </#if>


                                    <option value="${v}" ${foo?string("selected", "")}>${v}</option>

                            </#list>
                            </select>
                                <#--<input type="text" class="form-control" id="INP-SJ-AJ">-->
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-4 radio">
                                <label>
                                    <#assign bar= (cs.sjt == "SJ-AF")>
                                    <input type="radio" name="sjt" value="SJ-AF" ${bar?string("checked", "")}>免费
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
                                    <#assign bar= (cs.ddt == "DD-AZ")>
                                    <input type="radio" name="ddt" value="DD-AZ" ${bar?string("checked", "")}>按重量收费（USD）
                                </label>
                            </div>
                            <div class="col-sm-2">1公斤内</div>
                            <div class="col-sm-2">
                            <select class="form-control" id="INP_DD_AZ_1">
                            <#list INP_DD_AZ_1 as v>
                                <#assign foo=false>
                                <#if bar && cs.ddv?exists && cs.ddv?size == 4>
                                    <#assign foo=(cs.ddv[0]?string == v)>
                                </#if>


                                    <option value="${v}" ${foo?string("selected", "")}>${v}</option>

                            </#list>
                            </select>
                            </div>
                            <div class="col-sm-2">超件费</div>
                            <div class="col-sm-2">
                            <select class="form-control" id="INP_DD_AZ_2">
                            <#list INP_DD_AZ_2 as v>
                                <#assign foo=false>
                                <#if bar && cs.ddv?exists && cs.ddv?size == 4>
                                    <#assign foo=(cs.ddv[1]?string == v)>
                                </#if>


                                    <option value="${v}" ${foo?string("selected", "")}>${v}</option>

                            </#list>
                            </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-2 col-sm-offset-4">超1公斤=0.3USD+</div>
                            <div class="col-sm-2">
                            <select class="form-control" id="INP_DD_AZ_3">
                            <#list INP_DD_AZ_3 as v>
                                <#assign foo=false>
                                <#if bar && cs.ddv?exists && cs.ddv?size == 4>
                                    <#assign foo=(cs.ddv[2]?string == v)>
                                </#if>


                                    <option value="${v}" ${foo?string("selected", "")}>${v}</option>

                            </#list>
                            </select>
                            </div>
                            <div class="col-sm-2">超件费</div>
                            <div class="col-sm-2">
                            <select class="form-control" id="INP_DD_AZ_4">
                            <#list INP_DD_AZ_4 as v>
                                <#assign foo=false>
                                <#if bar && cs.ddv?exists && cs.ddv?size == 4>
                                    <#assign foo=(cs.ddv[3]?string == v)>
                                </#if>


                                    <option value="${v}" ${foo?string("selected", "")}>${v}</option>

                            </#list>
                            </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-4 radio">
                                <label>
                                    <#assign bar= (cs.ddt == "DD-AJ")>
                                    <input type="radio" name="ddt" value="DD-AJ" ${bar?string("checked", "")}>按件收费（USD）
                                </label>
                            </div>
                            <div class="col-sm-6">
                            <select class="form-control" id="INP-DD-AJ">
                            <#list INP_DD_AJ as v>
                                <#assign foo=false>
                                <#if bar && cs.ddv?exists && cs.ddv?size == 1>
                                    <#assign foo=(cs.ddv[0]?string == v)>
                                </#if>


                                    <option value="${v}" ${foo?string("selected", "")}>${v}</option>

                            </#list>
                            </select>
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