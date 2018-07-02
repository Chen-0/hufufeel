<#--<div class="show-modal hide" id="login-con">-->
<#--<div class="body white-text">-->
<#--<div class="content">-->
<#--<div class="row">-->
<#--<div class="col-md-6">-->
<#--<h3 class="text-primary">注册</h3>-->

<#--<form id="register-form" class="form-inline login-form" role="form"-->
<#--action="/Logistics/index.php/User/add" method="post">-->

<#--<table class="table">-->
<#--<tr>-->
<#--<td><label>用户名</label></td>-->
<#--<td><input type="text" class="form-control" name="userna"></td>-->
<#--</tr>-->
<#--<tr>-->
<#--<td><label>邮箱</label></td>-->
<#--<td><input type="email" class="form-control" name="username"></td>-->
<#--</tr>-->
<#--<tr>-->
<#--<td><label></label>登录密码</td>-->
<#--<td><input type="password" class="form-control" name="password"></td>-->
<#--</tr>-->
<#--<tr>-->
<#--<td><label>确认密码</label></td>-->
<#--<td><input type="password" name="repassword" class="form-control"></td>-->
<#--</tr>-->
<#--<tr>-->
<#--<td><label>姓氏</label></td>-->
<#--<td><input type="text" name="xing" class="form-control"></td>-->
<#--</tr>-->
<#--<tr>-->
<#--<td><label>名字</label></td>-->
<#--<td><input type="text" name="ming" class="form-control"></td>-->
<#--</tr>-->
<#--</table>-->
<#--</form>-->
<#--<div id="register-msg" class="text-primary"></div>-->
<#--</div>-->
<#--<div class="col-md-6">-->
<#--<button type="button" id="close-login" class="close" data-dismiss="modal"-->
<#--style="color: white; z-index: 999; opacity: 1;"><span aria-hidden="true">&times;</span><span-->
<#--class="sr-only">Close</span></button>-->
<#--<h3 class="text-primary">登录</h3>-->

<#--<form id="login-form" class="form login-form" role="form" action="/Logistics/index.php/Index/login"-->
<#--method="post">-->

<#--<table class="table">-->
<#--<tr>-->
<#--<td><label>用户名/邮箱</label></td>-->
<#--<td><input type="text" class="form-control" name="username"></td>-->
<#--</tr>-->
<#--<tr>-->
<#--<td><label>密码</label></td>-->
<#--<td><input type="password" class="form-control" name="password"></td>-->
<#--</tr>-->
<#--<tr>-->
<#--<td>-->
<#--<a href="javascript:void(0)" id="showReset">忘记密码？</a>-->
<#--</td>-->
<#--</tr>-->
<#--</table>-->
<#--</form>-->
<#--<div id="login-msg" class="text-primary"></div>-->
<#--</div>-->
<#--</div>-->

<#--<div class="row text-center">-->
<#--<button id="register-btn" type="button" class="btn btn-primary" style="margin-right: 30px;">注册</button>-->
<#--<button id="login-btn" type="button" class="btn btn-primary">登录</button>-->
<#--</div>-->
<#--</div>-->
<#--</div>-->
<#--</div>-->

<#--<div class="show-modal hide" id="reset">-->
<#--<div class="body white-text" style="width: 40%; height: 25%;">-->
<#--<div class="content">-->
<#--<div class="row" style="margin-bottom: 2.5rem;">-->
<#--<button type="button" id="close-reset" class="close" data-dismiss="modal"-->
<#--style="color: white; z-index: 999; opacity: 1;"><span aria-hidden="true">&times;</span><span-->
<#--class="sr-only">Close</span></button>-->
<#--<div class="col-md-10 col-md-offset-1 col-lg-10 col-lg-offset-1">-->
<#--<h3 class="text-primary" style="margin-bottom: 2.5rem;">找回密码</h3>-->

<#--<form class="form-horizontal" role="form" action="/Logistics/index.php/User/findPass" method="post"-->
<#--id="reset-form">-->
<#--<div class="form-group">-->
<#--<label for="inputEmail3" class="col-sm-2 control-label">电子邮箱：</label>-->
<#--<div class="col-sm-10">-->
<#--<input type="email" class="form-control" id="inputEmail3" placeholder="Email" name="emailf">-->
<#--</div>-->
<#--</div>-->
<#--</form>-->
<#--</div>-->
<#--</div>-->

<#--<div class="row text-center">-->
<#--<button id="reset-btn" type="button" class="btn btn-primary" style="margin-right: 30px;">确定</button>-->
<#--</div>-->
<#--</div>-->
<#--</div>-->
<#--</div>-->

<div class="fix-modal hide">
    <div class="panel panel-user">
        <div class="panel-heading" style="background: #fc6;">
            <h3 class="panel-title" style="display: inline-block">我要注册</h3>
            <button type="button" id="close-tijiao" class="close" data-dismiss="modal"
                    style="color: white; z-index: 999; opacity: 1;display: inline-block;"><span
                    aria-hidden="true">×</span><span class="sr-only">Close</span></button>
        </div>
        <div class="panel-body" style="background: white;">
            <div class="form-group text-center">
                <p>请联系客服进行注册，电话：170-8949-9183</p>
            </div>
        </div>
    </div>
</div>


<footer class="grey">
    <div class="container-fluid" style="border-bottom: 5px solid #fc6;">
        <div class="container">
            <div class="row" style="padding-top: 20px; padding-bottom: 20px; ">
                <div class="col-md-4"><img class="logo" src="/static/assets/images/ems.png"></div>
                <div class="col-md-4"><img class="logo" src="/static/assets/images/dhl.png"></div>
                <div class="col-md-4"><img class="logo" src="/static/assets/images/chinapost.png">
                </div>
            </div>
        </div>
    </div>
    <div class="container" style="padding: 20px 0; position: relative;">
        <p>友情链接：<a href="http://www.amazon.com">Amazon</a></p>
        <p>联系方式：电话：9513148768；客服邮箱：hufuinc@hufufeel.com</p>
        <p>关于我们：虎芙Feel货运管家</p>
    </div>
</footer>

<script src="https://cdn.bootcss.com/jquery/3.3.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script src="https://cdn.bootcss.com/malihu-custom-scrollbar-plugin/3.1.5/jquery.mCustomScrollbar.concat.min.js"></script>
<#--<script src="/static/bower_components/sweetalert-master/dist/sweetalert.min.js"></script>-->
<script src="https://cdn.bootcss.com/sweetalert/2.1.0/sweetalert.min.js"></script>
<script src="/static/assets/js/common.js"></script>
<script>
    function showCustom() {
        $('.fix-modal').removeClass('hide');
    }

    $(function () {
        $('#close-tijiao').click(function () {
            $('.fix-modal').addClass('hide');
        });
    });
</script>
