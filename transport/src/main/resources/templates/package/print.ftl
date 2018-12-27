<!DOCTYPE html>
<html>
<head>
    <meta content="text/html;charset=UTF-8" http-equiv="Content-Type"/>
    <title>批量打印条形码</title>
    <meta content="none" name="msapplication-config"/>
    <meta content="authenticity_token" name="csrf-param"/>
    <link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/metisMenu/2.7.0/metisMenu.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/startbootstrap-sb-admin-2/3.3.7+1/css/sb-admin-2.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap-datepicker/1.6.4/css/bootstrap-datepicker3.min.css">

<#--<link rel="shortcut icon" href="/Logistics/Public/static/favicon.ico" type="image/vnd.microsoft.icon">-->
<#--<link rel="icon" href="/Logistics/Public/static/favicon.ico" type="image/vnd.microsoft.icon">-->
    <!--<script src="/Logistics/Public/lodop/LodopFuncs.js"></script>-->
    <script src="/static/js/lodop.js"></script>
    <object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0 >
        <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="install_lodop32.exe"></embed>
    </object>
</head>

<body>



<div class="container-fluid">

    <!--<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>-->
    <!--<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>-->
    <!--</object>-->
    <div class="row">
        <div class="col-lg-offset-2 col-lg-8">
            <div class="alert alert-warning" style="margin-top: 1rem;">
                <strong>提示：</strong><br>
                打印完毕后请关闭此页面
            </div>
        </div>
    </div>

    <div class="row" style="margin-top: 25px;">
        <div class="col-lg-offset-2 col-lg-8">
            <div class="alert alert-warning" style="margin-top: 1rem;">
                <strong>提示：</strong><br>
                若无法打印 <a href="/file/CLodop_Setup_for_Win32NT.exe">点击这里</a> 下载打印插件。 <br>
                请使用 360 极速浏览器 （模式设置为极速模式）、Firefox 火狐浏览器、Chrome 谷歌浏览器。
            </div>
        </div>
    </div>
</div>

<!--<button class="btn btn-default" onclick="prn_Preview()">打印 </button>-->


<script type="text/javascript">
    var LODOP; //声明为全局变量
    var title = '虎芙导出条形码';

    var data = [
        <#list items as p>
            {"tracking_number": "${p.product.productSku}", "name": "${p.product.productName}", "sku": "${p.product.productSku}"},
        </#list>
    ];
    //    var data = [{"id":"7860","tracking_number":"JINGMIN-20170315A","batch":"HF201703220015","contact":"\u8c22\u82b3\u82b3"},{"id":"7861","tracking_number":"JINGMIN-20170315AB","batch":"HF201703220016","contact":"\u5434\u9e4f"},{"id":"7858","tracking_number":"YU20170321F","batch":"HF201703220013","contact":"\u6885\u7eee\u5a77"},{"id":"7859","tracking_number":"YU20170321G","batch":"HF201703220014","contact":"\u6885\u7eee\u5a77"}];
    var print_page_size = 1;
    function prn_Preview() {
        CreatePrintPage();
        LODOP.SET_PRINT_MODE("AUTO_CLOSE_PREWINDOW", 1);//打印后自动关闭预览窗口
        LODOP.PREVIEW();
    }
    function CreatePrintPage() {
        LODOP = getLodop(document.getElementById('LODOP_OB'), document.getElementById('LODOP_EM'));
        console.log(LODOP);
        if (print_page_size == 2) {
            LODOP.PRINT_INIT(title);
            LODOP.SET_PRINT_PAGESIZE(1, 0, 0, "Letter");
            LODOP.NEWPAGE();
            var page_max_count = 6;
            var base_width_offset = 20;
            var base_height_offset = 60;
            var height_offset = 0;
            for (var n = 0; n < data.length; n++) {
                var item = data[n];
                var width_offset = base_width_offset;
                if (n % 2 == 1) {
                    width_offset = width_offset + 400;
                }

                var height_remainder = n % page_max_count;

                if (height_remainder == 0) {
                    height_offset = base_height_offset
                }

                LODOP.ADD_PRINT_BARCODE(height_offset, width_offset, 270, 60, "128A", item.tracking_number);
                LODOP.ADD_PRINT_TEXT(height_offset + 80, width_offset, 270, 40, '货品名称:' + item.name);
                LODOP.ADD_PRINT_TEXT(height_offset + 120, width_offset, 200, 40, '货品SKU:' + item.sku);

                if (height_remainder % 2 == 1) {
                    height_offset = height_offset + 300;
                }

                if (height_remainder == (page_max_count - 1)) {
                    LODOP.NEWPAGE();
                }

            }
        } else {

            LODOP.PRINT_INITA(4, 4, 900, 500, title);
//            LODOP.PRINT_INIT(title);
            LODOP.SET_PRINT_PAGESIZE(1, 900, 500, "");
            LODOP.NEWPAGE();

            for (var i = 0; i < data.length; i++) {
                var item = data[i];
                LODOP.ADD_PRINT_BARCODE(10, 20, 270, 65, "128A", item.tracking_number);
                LODOP.ADD_PRINT_TEXT(100, 20, 70, 60, '名称：');
                LODOP.ADD_PRINT_TEXT(100, 50, 200, 60, item.name);
                LODOP.ADD_PRINT_TEXT(125, 20, 200, 30, 'SKU:' + item.sku);

                LODOP.NEWPAGE();
            }
        }
    }
    window.onload = function () {
        setTimeout(function () {
            prn_Preview();
        }, 1000);

    };
</script>
</body>
</html>