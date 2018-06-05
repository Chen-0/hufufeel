<html>
<head>
    <title>支付宝支付 - HUFUFEEL</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
</head>

<body>
<noscript><h1>您的浏览器已经禁用了 Javascript 。</h1></noscript>
<form id="alipaysubmit" name="alipaysubmit" action="https://mapi.alipay.com/gateway.do?_input_charset=UTF-8"
      method="get">
    <#assign keys = params?keys>
        <#list keys as k>
            <input type="hidden" name="${k}" value="${params["${k}"]}">
        </#list>
    <#--<input type="submit" value="submit">-->
    <input type="submit" value="submit" style="display:none;">
</form>
<script>document.forms['alipaysubmit'].submit();</script>
</body>
</html>
