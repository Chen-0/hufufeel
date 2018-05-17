<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <title>登陆</title>
    <link rel="stylesheet" href="/static/plugins/font-awesome-4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="/static/css/login.css">
</head>

<body>
<div class="fixed-background">
    <div class="login">
        <h1>欢迎，登陆虎芙货运管家</h1>
        <form method="post" action="/perform_login">
            <input name="remember_me" value="true" type="hidden">

            <div class="element-group">
                <label class="label-element" for=""><i class="fa fa-user fa-2x"></i></label>
                <input class="input-element" name="username" placeholder="账号" type="text">
            </div>
            <div class="element-group">
                <label class="label-element" for=""><i class="fa fa-lock fa-2x"></i></label>
                <input class="input-element" name="password" placeholder="密码" type="password">
            </div>

            <p class="text-danger">用户名或密码错误</p>

            <div style="text-align: center; margin-bottom: 5px;">
                <input value="登陆" type="submit">
            </div>

            <div>
                <a href="">我要注册</a>
                <a href="" style="float: right;">忘记密码</a>
            </div>

        </form>
    </div>
</div>

<#--<div class="login-container">-->

    <#--<div style="text-align: center;">HUFU FEEL</div>-->

    <#--<form action="/">-->

        <#--<label for="username">用户名：</label>-->
        <#--<input type="email" name="username" id="username">-->

        <#--<label for="password">密码：</label>-->
        <#--<input type="password" name="password" id="password">-->
    <#--</form>-->
<#--</div>-->


</body>

</html>
