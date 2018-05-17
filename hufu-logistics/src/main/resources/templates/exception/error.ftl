<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>404 - KorvenMauve</title>
    <!-- Styles -->
    <style>
        html, body {
            background-color: #fff;
            color: #636b6f;
            font-weight: 100;
            height: 100vh;
            margin: 0;
        }

        .full-height {
            height: 100vh;
        }

        .flex-center {
            align-items: center;
            justify-content: center;
            flex-flow: column;
            /*flex-wrap: wrap;*/
            display: flex;
        }

        .title {
            font-size: 18px;
            text-align: center;
            margin-bottom: 2rem;
            /*flex: 0 0 100%;*/
        }

        .sub-title {
            font-size: 1.5rem;
            text-align: right;
            display: block;
        }

        .links {
            padding: .5rem 2rem;
            color: #E0E0E0;
            text-decoration: none;
            outline: none;

            background: black;
            display: inline-block;
        }

        .links:active,
        .links:focus,
        .links:visited {
            color: #E0E0E0;
            background: black;
        }
    </style>
</head>
<body>
<div class="flex-center full-height">
<#if message?exists >
    <div class="title">${message!}</div>
<#else >
    <div class="title">发生未知错误，请联系开发人员。</div>
</#if>

    <div class="title"><a id="back" href="javascript:void(0);">返回</a></div>
</div>

</body>
<script src="//cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
<script>
    $(function () {
        $('#back').click(function () {
            window.history.go(-1);
        });
    })
</script>
</html>