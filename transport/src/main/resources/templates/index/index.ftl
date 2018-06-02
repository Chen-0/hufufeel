<!DOCTYPE html>
<html lang="zh-cn">
<#assign title="首页"/>
<#include "*/_layout/v1.0/head.ftl"/>
<body>

<#include "*/_layout/v1.0/header.ftl"/>

<div class="container-fluid grey">
    <div class="row">
        <div id="carousel" class="carousel slide" data-ride="carousel">
            <ol class="carousel-indicators">
                <li data-target="#carousel" data-slide-to="0" class="active"></li>
                <li data-target="#carousel" data-slide-to="1"></li>
                <li data-target="#carousel" data-slide-to="2"></li>
                <li data-target="#carousel" data-slide-to="3"></li>
            </ol>
            <div class="carousel-inner" role="listbox">
                <div class="item active">
                    <img src="/static/assets/images/1.jpg">
                </div>
                <div class="item">
                    <img src="/static/assets/images/2.jpg">
                </div>
                <div class="item">
                    <img src="/static/assets/images/3.jpg">
                </div>
                <div class="item">
                    <img src="/static/assets/images/4.jpg">
                </div>
            </div>
            <a class="left carousel-control" href="#carousel" role="button" data-slide="prev">
                <span class="glyphicon glyphicon-chevron-left"></span>
                <span class="sr-only">Previous</span>
            </a>
            <a class="right carousel-control" href="#carousel" role="button" data-slide="next">
                <span class="glyphicon glyphicon-chevron-right"></span>
                <span class="sr-only">Next</span>
            </a>
        </div>
    </div>

    <div class="row" style="text-align: center;">
        <div style="display: inline-block;">
            <form role="form" class="form-inline">
                <div class="form-group" style="border-collapse:separate; border-spacing:10px; ">
                    <div class="input-group">
                        <div class="input-group-search">
                            <i class="fa fa-search fa-fw" aria-hidden="true"></i>
                        </div>
                        <input id="search-text" class="form-control search-input" type="text" placeholder="输入单号查询运单">
                        <input class="search-input-submit" type="button" value="Go" id="search">
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="container-fluid light-grey" style="padding: 15px 0;">
    <div class="container">
        <div class="row" style="text-align: center;">
            <div style="display: inline-block; margin-right: 15px;vertical-align:middle;">服务时间：中国时间上午9:00-下午3:00</div>
            <div style="display: inline-block; margin-right: 15px;vertical-align:center;">
                <img src="/static/assets/images/qq_icon.png">
                <a class="black-text" style="display: inline-block;vertical-align: middle;"
                   href="http://wpa.qq.com/msgrd?v=3&amp;uin=3398425679&amp;site=qq&amp;menu=yes"
                   target="_blank">中国客服</a>
            </div>
            <div style="display: inline-block; margin-right: 15px;vertical-align:middle;">
                <img src="/static/assets/images/qq_icon.png">
                <a class="black-text" style="display: inline-block;vertical-align: middle;"
                   href="http://wpa.qq.com/msgrd?v=3&amp;uin=3398425679&amp;site=qq&amp;menu=yes"
                   target="_blank">美国客服</a>
            </div>
            <div style="display: inline-block; vertical-align:middle;">虎芙QQ群号：595439326</div>
        </div>
        <div class="row">
            <div style="margin: 15px auto; width: 960px">
                <div class="notice-bar">
                    <h3>公告栏</h3>
                    <ul>
                        <volist name="news_list" id="vo">
                            <li><a class="white-text" href="javascript:void(0);" data-id="{$vo['id']}"
                                   data-type="affiche">【公告】&nbsp; {$vo['subject']}</a></li>
                        </volist>
                    </ul>
                </div>

                <div style="display: inline-block;vertical-align: top;">
                    <img src="/static/assets/images/bg1.png">
                </div>
            </div>
        </div>

        <div class="row" style="cursor: pointer;" onclick="showCustom()">
            <div style="width: 960px; margin: 0 auto;">
                <img src="/static/assets/images/bg2.png">
            </div>
        </div>
    </div>
</div>

<div class="show-modal hide" id="affiche-modal">
    <div class="body white-text">
        <div class="content" id="content">
            <div id="affiche-content">
            </div>
        </div>
        <div class="close-modal">
            <img src="/static/assets/images/close.png" alt="">
        </div>
    </div>
</div>


<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">查询运单</h4>
            </div>
            <div class="modal-body" id="search-container">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>


<div class="fix-modal hide">
    <div class="panel panel-user">
        <div class="panel-heading" style="background: #fc6;">
            <h3 class="panel-title" style="display: inline-block">货物订制</h3>
            <button type="button" id="close-tijiao" class="close" data-dismiss="modal"
                    style="color: white; z-index: 999; opacity: 1;display: inline-block;"><span
                    aria-hidden="true">×</span><span class="sr-only">Close</span></button>
        </div>
        <div class="panel-body" style="background: white;">
            <form id="custom" class="form-user" role="form" method="post"
                  action="/Logistics/index.php/UserGoods/diysub">
                <div class="form-group">
                    <label for="brandname">英文品牌名</label>
                    <input type="text" class="form-control" id="brandname" name="brandname">
                </div>

                <div class="form-group">
                    <label for="name">型号</label>
                    <input type="text" class="form-control" id="name" name="name">
                </div>

                <div class="form-group">
                    <label for="color">颜色</label>
                    <input type="text" class="form-control" id="color" name="color">
                </div>

                <div class="form-group">
                    <label for="email">电子邮箱</label>
                    <input type="text" class="form-control" id="email" name="email">
                </div>

                <div class="form-group">
                    <label for="weixin">微信</label>
                    <input type="text" name="weixin" class="form-control" id="weixin" style="width: 50%;">
                    <button id="save" type="button" class="btn btn-primary" style="margin-left: 2em;">确定</button>
                </div>

                <div class="form-group text-center">
                    <p>订制前请联系我们，QQ号：2465957191；3398425679</p>
                </div>

                <div class="text-danger" id="goods_msg"></div>
            </form>
        </div>
    </div>
</div>

<#include "*/_layout/v1.0/footer.ftl"/>
</body>
</html>