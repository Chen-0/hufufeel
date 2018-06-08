<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="UTF-8">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <title>登陆 - 虎芙货运管家</title>
    <link href="https://cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/static/css/login.css">
</head>

<body>
<div class="fixed-background">
    <div class="login">
        <h1>欢迎，登陆虎芙货运管家</h1>
        <form method="post" action="/perform_login">
            <input name="remember_me" value="true" type="hidden">
            <input type="hidden" name="${_csrf.parameterName!}" value="${_csrf.token!}"/>

            <div class="element-group">
                <label class="label-element" for=""><i class="fa fa-user fa-2x"></i></label>
                <input class="input-element" name="username" placeholder="账号" type="text">
            </div>
            <div class="element-group">
                <label class="label-element" for=""><i class="fa fa-lock fa-2x"></i></label>
                <input class="input-element" name="password" placeholder="密码" type="password">
            </div>

        <#if SPRING_SECURITY_LAST_EXCEPTION??>
            <p class="text-danger">账号或密码错误</p>
        </#if>

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
</body>
</html>
