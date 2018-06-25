function convertFormToJSON(b){var c=$(b).serializeArray();var a={};$.each(c,function(){a[this.name]=this.value||""});return a}function submitAsJSON(c){var d=JSON.stringify(convertFormToJSON(c)),b=d.slice(0,-1)+', "trash": "';var a="<form method='POST'  enctype='text/plain' action='"+$(c).attr("action")+"'>"+"<input name='"+b+"' value='\"}'>"+"</form>";$(a).appendTo("body").submit()};
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

