//@ sourceURL=Login.js
var LoginView;
define([
    'text!html/Login.html',

    'kendo/kendo.button.min',
    'zebra_cookie',
    'cryptojs/core-min',
    'cryptojs/enc-base64-min',
    'cryptojs/sha256',
    'robe/view/Page'
], function (view) {

    LoginView = new RobeView("LoginView", view, "container");
    LoginView.render = function () {
        $('#dialogMessage').append(view);
        LoginView.initialize();
    };

    LoginView.initialize = function () {
        var token = $.cookie.read("auth-token");
        $("#username").val("admin@robe.io");
        $("#password").val("123123");

        $('#loginError').hide();
        var me = this;
        $('#login-button').kendoButton({
            click: function (token) {
                $.ajax({
                    type: "POST",
                    url: AdminApp.getBackendURL() + "authentication/login",
                    data: JSON.stringify({username: $("#username").val(), password: CryptoJS.SHA256($("#password").val()).toString()}),
                    contentType: "application/json; charset=utf-8",
                    success: function (response) {
                        $.cookie.write("userEmail", $("#username").val());
                        $(document.body).unbind("keydown");
                        $('#dialog').data("kendoWindow").close();
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
            },
            imageUrl: "./icon/checkmark.png"
        });
        $(document.body).keydown(function (e) {
            if (e.keyCode == 13) {
                $("#login-button")[0].focus();
            }
        });
    };

    return LoginView;
});

