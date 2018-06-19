$(document).ready(function () {
    $('.sidebar-menu').tree();

    $('.x-remove').click(function (e) {
        e.preventDefault();

        var url = $(this).attr("href");

        if (confirm("确实删除该条目")) {
            window.location.href = url;
        }
    });

    var msgContainer = $('.message-container');

    if (msgContainer.length > 0) {
        msgContainer.fadeOut(3000);
    }
});

