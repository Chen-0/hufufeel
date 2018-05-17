$(function () {
    var $alert = $('.alert-message');

    if ($alert.length > 0) {
        //init
        $alert.hide();
        $alert.fadeIn();

        setTimeout(function () {
            $alert.fadeOut();
        }, 5000);
    }
});

