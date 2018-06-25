function convertFormToJSON(form) {
    var array = $(form).serializeArray();
    var json = {};

    $.each(array, function() {
        json[this.name] = this.value || '';
    });

    return json;
}

function submitAsJSON(form) {
    var array = convertFormToJSON(form);

    var cName = "_csrf";
    var cValue = array[cName];
    if (cValue != null) {
        delete array[cName];
    }

    var HackyJSONString = JSON.stringify(array);
    var hackyForm = "<form method='POST' action='" + $(form).attr('action') + "'>";

    if (cValue != null) {
        hackyForm += "<input type='hidden' name='" + cName + "' value='" + cValue + "'>";
    }


    hackyForm += "<input type='hidden' name='json' value='" + HackyJSONString + "'>" +
        "</form>";

    $(hackyForm).appendTo('body').submit();
}