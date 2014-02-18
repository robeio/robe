//@ sourceURL=Login.js
var Login = robe.util.inherit(robe.view.Page, {
    htmlPath: "./html/Login.html",
    initialize: function () {
        var token = $.cookie.read("auth-token");
        $("#username").val("admin@robe.io");
        $("#password").val("123123");
        if (token != null) {
            //   login(token);
        }

        $('#loginError').hide();
        $('#login-button').kendoButton({
            click: this.login,
            imageUrl: "./icon/checkmark.png"
        });
        $(document.body).keydown(function (e) {
            if (e.keyCode == 13) {
                console.log("hop");
                $("#login-button")[0].focus();
            }
        });
    },
    parentPage: null,
    login: function (token) {
        var me = this;
        $.ajax({
            type: "POST",
            url: AdminApp.getBackendURL() + "authentication/login",
            data: JSON.stringify({username: $("#username").val(), password: CryptoJS.SHA256($("#password").val()).toString()}),
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                $.cookie.write("userEmail", $("#username").val());
                $(document.body).unbind("keydown");
                $('#dialog').data("kendoWindow").close();
                Login.parentPage.loadMenu();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(errorThrown);
            },
            statusCode: {
                401: function () {
                    $('#loginError').show();
                },
                422: function (xhr) {
                    var errors = JSON.parse(xhr.responseText);
                    var msg = "";
                    $.each(errors, function (index, error) {
                        msg += "<br> <b>" + error.name + ":</b> " + error.message;
                    });
                    robe.App.instance.showDialog(msg);
                }
            }
        });
    }
});

