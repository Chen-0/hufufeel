/**
 * Created by rache on 2016/11/3.
 */

function showModal(ele) {
    $(ele).removeClass('hide');
    $('body').addClass('hide-overflow')
}

function closeModal() {
    $('.show-modal').addClass('hide');
    $('body').removeClass('hide-overflow');
}

$('.close-modal').click(function () {
    closeModal();
});

$('#login-btn').click(function () {
    var url = $('#login-form').attr('action');
    var data = $('#login-form').serialize();

    $.ajax({
        url: url,
        data: data,
        type: 'POST',
        dataType: 'json',
        success: function (result) {
            console.log(result);
            if (result.data.res == 1) {
                window.location.reload();
            } else {
                $('#login-msg').html(result.data.msg);
                //$('#login-msg').html(result.data.res);
            }
        }
    })
});

$('#register-btn').click(function () {
    var url = $('#register-form').attr('action');
    var data = $('#register-form').serialize();

    $.ajax({
        url: url,
        data: data,
        type: 'POST',
        dataType: 'json',
        success: function (result) {
            console.log(result);
            if (result.data.res == 1) {
                window.location.reload();
            } else {
                $('#register-msg').html(result.data.res);
            }
        }
    })
});

$(document).ready(function () {
    $.ajax({
        type: "POST",
        url: "/Logistics/index.php/Index/isLogin",
        dataType: "json",
        success: function (result) {
            console.log(result);
            if (result.data.res == 1) {
                //login
                $('#username').html(result.data['username']);
                showMenu();

                $.ajax({
                    type: "POST",
                    url: "/Logistics/index.php/Index/get_msg",
                    dataType: "json",
                    success: function (result) {
                        if (result.data != null) {
                            $('.new').removeClass('hide');
                        }
                    }
                });
            } else {
                //not login
                showLogin();
            }
        }
    });

    function showLogin() {
        $('#login-container').removeClass('hide');
    }

    function showMenu() {
        $('#menu-container').removeClass('hide');
    }
});

$('#login').click(function () {
    $('#login-con').removeClass('hide');
});

$('#register').click(function () {
    $('#login-con').removeClass('hide');
});

$('#close-login').click(function () {
    $('#login-con').addClass('hide');
});

$('#reset-btn').click(function () {

    var url = $('#reset-form').attr('action');
    var data = $('#reset-form').serialize();

    $.ajax({
        url: url,
        data: data,
        dataType: 'json',
        type: 'post',
        success: function (data) {
            console.log(data);

            var res = data.data;

            console.log(res);
            if (res['res'] == 1) {
                swal("干得漂亮!", "新密码已发送到您的邮箱，快去查看吧！", "success");
            } else {
                sweetAlert("哎呦！", res['contect'], 'error');
            }
        }
    });
});

$('#showReset').click(function (event) {
    event.preventDefault();

    $('#login-con').addClass('hide');

    $('#reset').removeClass('hide');
});

$('#close-reset').click(function () {
    $('#reset').addClass('hide');
});

