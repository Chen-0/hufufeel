<!DOCTYPE html>
<html lang="zh-cn">
<#assign title="费用说明">
<#include "*/_layout/v1.0/head.ftl"/>
<body>

<#include "*/_layout/v1.0/header.ftl"/>


<header>
    <div class="container-fluid grey">
        <div class="container" style="padding: 15px 0;">
            <div class="col-md-offset-1 col-md-5">
                <img class="header-logo" src="/static/assets/images/header_logo/FuWu.png">
                <h3 class="white-text">服务费用</h3>
            </div>
        </div>
    </div>
</header>

<div class="container-fluid black">
    <div class="container img-item white-text" style="padding: 15px 0;">
        <div class="row margin-bottom">
            <div class="col col-md-offset-1 col-md-10">
                <div class="img-container">
                    <img src="/static/assets/images/FeiYong/1.jpg">
                </div>
                <h3 style="margin-top: 0;">{$info[0]['express']}</h3>
                <p class="text-primary">￥{$info[0]['firstcost']}每磅</p>
                <!--<p class="text-primary">{$info[0]['insurance']} %保险费</p>-->
                <p>（如产生关税，由虎芙货运管家承担）</p>
                <p>1：专属于各类奶粉的渠道，如各种品牌的幼儿奶粉、成年保健营养奶粉等</p>
                <p>2：打包要求为成年奶粉6盒一箱，幼儿奶粉3罐一箱，不能混装。</p>
                <p>3：此渠道建议提交个人身份证信息。并且严格照样要求打包。如不符合渠道要求，后果自负。</p>
                <a class="pull-right" href="#"> < 了解更多
                    <div class="hide">
                        <p>1：专属于各类奶粉的渠道，如各种品牌的幼儿奶粉、成年保健营养奶粉等</p>
                        <p>2：打包要求为成年奶粉6盒一箱，幼儿奶粉3罐一箱，不能混装。</p>
                        <p>3：此渠道建议提交个人身份证信息。并且严格照样要求打包。如不符合渠道要求，后果自负。</p>
                    </div>
                </a>
            </div>
        </div>

        <div class="row margin-bottom">
            <div class="col col-md-offset-1 col-md-10">
                <div class="img-container">
                    <img src="/static/assets/images/FeiYong/2.jpg">
                </div>
                <h3 style="margin-top: 0;">{$info[1]['express']}</h3>
                <p class="text-primary">￥{$info[1]['firstcost']}每磅</p>
                <p>（如产生关税，由虎芙货运管家承担）</p>
                <p>1：专属于各类保健品，鞋类，母婴产品，食品，纺织品。</p>
                <p>2：打包要求为保健品大罐6件一箱，小罐8件一箱。</p>
                <p style="margin-left: 8rem;">鞋子1双一箱。</p>
                <p style="margin-left: 8rem;">母婴产品，食品500RMB以内一箱。</p>
                <p style="margin-left: 8rem;">纺织品两件为一箱。</p>
                <p>3：此渠道建议严格按照包裹要求寄出，如不符合渠道要求，后果自负。</p>
                <a class="pull-right" href="#"> < 了解更多
                    <div class="hide">
                        <p>1：专属于各类保健品，鞋类，母婴产品，食品，纺织品。</p>
                        <p>2：打包要求为保健品大罐6件一箱，小罐8件一箱。</p>
                        <p style="margin-left: 2rem;">鞋子1双一箱</p>
                        <p>母婴产品，食品500RMB以内一箱。</p>
                        <p>纺织品两件为一箱。</p>
                        <p>3：此渠道建议严格按照包裹要求寄出，如不符合渠道要求，后果自负。</p>
                    </div>
                </a>
            </div>
        </div>

        <div class="row margin-bottom">
            <div class="col col-md-offset-1 col-md-10">
                <div class="img-container">
                    <img src="/static/assets/images/FeiYong/2.jpg">
                </div>
                <h3 style="margin-top: 0;">{$info[2]['express']}</h3>
                <p class="text-primary">￥{$info[2]['firstcost']}每磅</p>
                <p>（如产生关税，由虎芙货运管家承担）</p>
                <p>1：专属于大件包裹的渠道，如普货混装等。</p>
                <p>2：打包要求为10磅以内一件包裹，鞋子两双一箱，食品与保健品混装可8件一箱</p>
                <p style="margin-left: 8rem;">食品与日用品和母婴产品混装可12件为一箱。</p>
                <p style="margin-left: 8rem;">纺织品可10件为一箱。</p>
                <p>
                    3：此渠道建议严格按照包裹要求寄出，如不符合渠道要求，后果自负。
                </p>
                <a class="pull-right" href="#"> < 了解更多
                    <div class="hide">
                        <p>1：如产生关税，由虎芙FEEL货运管家承担。</p>
                        <p>
                            2：专属于各类个人洗护以及彩妆商品，如：男士化妆品以及护肤品、女士化妆品以及护肤品、各类香水和MK、KS、COACH非一线包包等。每箱限重五磅（首磅：45元，续磅：45元，续磅按照0.1磅计费，每0.1磅4元。）</p>
                    </div>
                </a>
            </div>
        </div>

        <div class="row margin-bottom">
            <div class="col col-md-offset-1 col-md-10">
                <div class="img-container">
                    <img src="/static/assets/images/FeiYong/4.jpg">
                </div>
                <h3 style="margin-top: 0;">{$info[3]['express']}</h3>
                <p class="text-primary">￥{$info[3]['firstcost']}每磅</p>
                <p>（如产生关税，由虎芙货运管家承担）</p>
                <p>1：专属于电子产品的渠道，如，电动玩具、耳温计、麦克风、电子游戏配件、榨汁机、咖啡机。酸奶机、</p>
                <p style="margin-left: 17rem;">电动剃须刀、电动吸奶机、电动洗脸刷、洗尘器等。</p>
                <p>2：打包要求为耳温计，麦克风，电子游戏配件，电动玩具可2件一箱。</p>
                <p style="margin-left: 8rem;">榨汁机、咖啡机。酸奶机、电动剃须刀、电动吸奶机、电动洗脸刷、洗尘器可1件一箱</p>
                <p>
                    3：此渠道建议严格按照包裹要求寄出，如不符合渠道要求，后果自负。
                </p>
                <a class="pull-right" href="#"> < 了解更多
                    <div class="hide">
                        <p>（如产生关税，由虎芙货运管家承担）</p>
                        <p>1：专属于电子产品的渠道，如，电动玩具、耳温计、麦克风、电子游戏配件、榨汁机、咖啡机。酸奶机、电动剃须刀、电动吸奶机、电动洗脸刷、洗尘器等。</p>
                        <p>
                            2：打包要求为耳温计，麦克风，电子游戏配件，电动玩具可2件一箱。榨汁机、咖啡机。酸奶机、电动剃须刀、电动吸奶机、电动洗脸刷、洗尘器可1件一箱
                        </p>
                        <p>
                            3：此渠道建议严格按照包裹要求寄出，如不符合渠道要求，后果自负。
                        </p>
                    </div>
                </a>
            </div>
        </div>

        <div class="row margin-bottom">
            <div class="col col-md-offset-1 col-md-10">
                <div class="img-container">
                    <img src="/static/assets/images/FeiYong/5.jpg">
                </div>
                <h3 style="margin-top: 0;">{$info[4]['express']}</h3>
                <p class="text-primary">￥{$info[4]['firstcost']} 每磅</p>
                <p>（如产生关税，由虎芙货运管家承担）</p>
                <p>1：专属于各类个人洗护以及彩妆商品，如男士化妆品以及护肤品、女士化妆品以及护肤品等</p>
                <p>2：打包要求为大盒化妆品2件一箱，套装2件一箱。</p>
                <p style="margin-left: 8rem;">小盒化妆品4件一箱，化妆品小样可8件一箱。</p>
                <p>3：此渠道建议严格按照包裹要求寄出，如不符合渠道要求，后果自负。</p>
                <a class="pull-right" href="#"> < 了解更多
                    <div class="hide">
                        <p>（如产生关税，由虎芙货运管家承担）</p>
                        <p>1：专属于各类个人洗护以及彩妆商品，如男士化妆品以及护肤品、女士化妆品以及护肤品等</p>
                        <p>2：打包要求为大盒化妆品2件一箱，套装2件一箱。小盒化妆品4件一箱，化妆品小样可8件一箱。</p>
                        <p>3：此渠道建议严格按照包裹要求寄出，如不符合渠道要求，后果自负。</p>
                    </div>
                </a>
            </div>
        </div>

        <div class="row margin-bottom">
            <div class="col col-md-offset-1 col-md-10">
                <div class="img-container">
                    <img src="/static/assets/images/FeiYong/5.jpg">
                </div>
                <h3 style="margin-top: 0;">{$info[5]['express']}</h3>
                <p class="text-primary">￥{$info[5]['firstcost']} 每磅</p>
                <p>（如产生关税，由虎芙货运管家承担）</p>
                <p>1：专属于各类化妆品大包裹渠道，如化妆品混装等。</p>
                <p>2：打包要求为大盒化妆品5件一箱，套装4件一箱。</p>
                <p style="margin-left: 8rem;">小盒化妆品8件一箱，化妆品小样可14件为一箱。</p>
                <p>3：此渠道建议严格按照包裹要求寄出，如不符合渠道要求，后果自负。</p>
                <a class="pull-right" href="#"> < 了解更多
                    <div class="hide">
                        <p>（如产生关税，由虎芙货运管家承担）</p>
                        <p>1：专属于各类化妆品大包裹渠道，如化妆品混装等。</p>
                        <p>2：打包要求为大盒化妆品5件一箱，套装4件一箱。小盒化妆品8件一箱，化妆品小样可14件为一箱。</p>
                        <p>3：此渠道建议严格按照包裹要求寄出，如不符合渠道要求，后果自负。</p>
                    </div>
                </a>
            </div>
        </div>

        <div class="row margin-bottom">
            <div class="col col-md-offset-1 col-md-10">
                <div class="img-container">
                    <img src="/static/assets/images/FeiYong/3.jpg">
                </div>
                <h3 style="margin-top: 0;">{$info[6]['express']}</h3>
                <p class="text-primary">￥{$info[6]['firstcost']} 每磅</p>
                <p>（如产生关税，由虎芙货运管家承担）</p>
                <p>1：专属于各类二线包包渠道，如MK,COACH,KS各类非一线品牌包包等。</p>
                <p>2：打包要求为提包可1件一箱，注：可提包带2个小钱包。</p>
                <p style="margin-left: 8rem;">钱包可4件一箱。</p>
                <p>3：此渠道建议严格按照包裹要求寄出，如不符合渠道要求，后果自负。</p>
                <a class="pull-right" href="#"> < 了解更多
                    <div class="hide">
                        <p>（如产生关税，由虎芙货运管家承担）</p>
                        <p>1：专属于各类二线包包渠道，如MK,COACH,KS各类非一线品牌包包等。</p>
                        <p>2：打包要求为提包可1件一箱，注：可提包带2个小钱包。钱包可4件一箱。</p>
                    </div>
                </a>
            </div>
        </div>

        <div class="row margin-bottom">
            <div class="col col-md-offset-1 col-md-10">
                <div class="img-container">
                    <img src="/static/assets/images/FeiYong/6.jpg">
                </div>
                <h3 style="margin-top: 0;">{$info[7]['express']}</h3>
                <p class="text-primary">￥{$info[7]['firstcost']} 每磅</p>
                <!--<p class="text-primary">￥40 续磅</p>-->
                <p>（如产生关税，由虎芙货运管家承担）</p>
                <p>1：专属于各类不可分割的大件商品。如儿童安全座椅、儿童座椅、婴儿推车、木马玩具以及儿童摇椅、旅行箱等。</p>
                <p>2：打包要求为一件一箱。（无体积费）</p>
                <p>3：此渠道建议严格按照包裹要求寄出，如不符合渠道要求，后果自负。</p>
                <a class="pull-right" href="#"> < 了解更多
                    <div class="hide">
                        <p>（如产生关税，由虎芙货运管家承担）</p>
                        <p>1：专属于各类不可分割的大件商品。如儿童安全座椅、儿童座椅、婴儿推车、木马玩具以及儿童摇椅、旅行箱等。</p>
                        <p>2：打包要求为一件一箱。（无体积费）</p>
                        <p>3：此渠道建议严格按照包裹要求寄出，如不符合渠道要求，后果自负。</p>
                    </div>
                </a>
            </div>
        </div>

        <!-- --------------------------------------->
        <div class="row margin-bottom">
            <div class="col col-md-offset-1 col-md-10">
                <div class="img-container">
                    <img src="/static/assets/images/FeiYong/7.jpg">
                </div>
                <h3 style="margin-top: 0;">{$info[8]['express']}</h3>
                <p class="text-primary">￥{$info[8]['firstcost']} 每磅</p>
                <p class="text-primary">￥{$info[8]['nextcost']} 续磅</p>
                <p>（此渠道无限重）</p>
                <p>1：税金自理，虎芙不承担任何税金；</p>
                <p>2：可发任何商品高档名贵商品（除违禁品以外）。（此渠道无限重，首磅 {$info[8]['firstcost']} 元，续磅 {$info[8]['nextcost']} 元，续磅按照 0.1
                    磅计费，每 0.1 磅 3.2 元。）</p>
                <a class="pull-right" href="#"> < 了解更多
                    <div class="hide">
                        <p>1：税金自理，虎芙不承担任何税金；</p>
                        <p>
                            2：可发任何商品高档名贵商品（除违禁品以外）。（此渠道无限重,首磅：{$info[8]['firstcost']}元，续磅{$info[8]['nextcost']}元，续磅按照0.1磅计费，每0.1磅3.2元。）</p>
                    </div>
                </a>
            </div>
        </div>

        <div class="row margin-bottom">
            <div class="col col-md-offset-1 col-md-10" style="border-bottom: none;">
                <div class="img-container">
                    <img src="/static/assets/images/FeiYong/8.jpg">
                </div>
                <h3 style="margin-top: 0;">{$info[9]['express']}</h3>
                <p class="text-primary">￥{$info[9]['firstcost']} 每磅</p>
                <!--<p class="text-primary">￥40 续磅</p>-->
                <p>（此渠道无限重）</p>
                <p>1：税金自理，虎芙不承担任何税金；</p>
                <p>2：可发任何商品（除违禁品以外）高档名贵超规格大件商品，如家具、沙发、衣柜、超大屏幕电视机等。（此渠道无限重，首磅 40 元，续磅 40 元，续磅按照 0.1 磅计费，每 0.1 磅 4 元。）</p>
                <a class="pull-right" href="#"> < 了解更多
                    <div class="hide">
                        <p>1：税金自理，虎芙不承担任何税金；</p>
                        <p>2：可发任何商品（除违禁品除外）高档名贵超规格大件商品，如家具，沙发，衣柜，超大屏幕电视机等。（此渠道无限重，首磅：40元，续磅40元，续磅按照0.1磅计费，每0.1磅4元。）</p>
                    </div>
                </a>
            </div>
        </div>
    </div>
</div>

<div class="show-modal hide" id="affiche-modal">
    <div class="body white-text">
        <div class="content" id="content">
            <div id="affiche-content"></div>
        </div>
        <div class="close-modal">
            <img src="/static/assets/images/close.png" alt="">
        </div>
    </div>
</div>

<#include "*/_layout/v1.0/footer.ftl"/>
<script>
    $(function () {
        $('a.pull-right').click(function (event) {
            event.preventDefault();
            var content = '';

            var title = $(this).parent().find('h3').html();
            content += '<h3 class="text-primary">' + title + '</h3>';
            content += $(this).find('div').html();
            $('#affiche-content').html(content);

            $('#affiche-modal').removeClass('hide');
        })
    });
</script>
</body>
</html>