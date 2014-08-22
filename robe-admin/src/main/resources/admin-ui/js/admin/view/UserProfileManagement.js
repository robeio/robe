var UserProfileManagementView;
define([
    'text!html/UserProfileManagement.html',
    'admin/data/DataSources',
    'cryptojs/core-min',
    'cryptojs/enc-base64-min',
    'cryptojs/sha256',
    'kendo/kendo.grid.min',
    'kendo/kendo.window.min',
    'kendo/kendo.button.min',
    'kendo/kendo.dropdownlist.min',
    'kendo/kendo.upload.min',
    'robe/view/RobeView'
], function (view) {

    UserProfileManagementView = new RobeView("UserProfileManagementView", view, "container");

    UserProfileManagementView.render = function () {
        $('#container').append(view);
        UserProfileManagementView.initialize();
    };

    UserProfileManagementView.initialize = function () {

        var me = this;
        $.ajax({
            type: "GET",
            url: AdminApp.getBackendURL() + "user/profile",
            contentType: "application/json",
            success: function (response) {
                $("#emailAddress").val(response.email);
                $("#firstAndLastName").val(response.name + " " + response.surname);
                me.data = response;
            },
            error: function (e) {

            }
        });

        var goodColor = "#66cc66";
        var badColor = "#ff6666";

        $("#newPassword").focusout(function () {
            if (validatePassword()) {
                document.getElementById('newPassword').style.background = goodColor;
            } else {
                document.getElementById('newPassword').style.background = badColor;
            }

        });

        $("#reNewPassword").keyup(function () {
            if (validatePassword() && isMatch()) {
                document.getElementById('newPassword').style.background = goodColor;
                document.getElementById('reNewPassword').style.background = goodColor;
                document.getElementById('matchMessage').innerHTML = "";
                document.getElementById('confirmMessage').innerHTML = "";
            } else {
                document.getElementById('newPassword').style.background = badColor;
                document.getElementById('reNewPassword').style.background = badColor;
            }
        });


        function validatePassword() {
            var error = "";
            var isValid = true;

            var newPassword = document.getElementById('newPassword');
            var message = document.getElementById('confirmMessage');

            if ((newPassword.value.length < 4) || (newPassword.value.length > 15)) {
                error += "Şifreniz en az 4 en fazla 15 karakter uzunluğunda olmalı.<br/>";
                message.innerHTML = error;
                isValid = false;
            }
            // Accepts Only Alphanumeric Chars
            if (!(newPassword.value.match(/^.*(?=.*[a-zA-Z])(?=.*\d).*$/i))) {
                error += "Şifrenizde en az bir adet rakam ve bir adet harf olmalıdır<br/>";
                message.innerHTML = error;

                isValid = false;
            }

            if (!(newPassword.value.match(/^\S*$/))) {
                error += "Şifrenizde boşluk olamaz.<br/>";
                message.innerHTML = error;

                isValid = false;
            }

            $("#confirmMessage").val(error);
            return isValid;
        }

        function isMatch() {
            var newPassword = document.getElementById('newPassword');
            var reNewPassword = document.getElementById('reNewPassword');
            var matchMessage = document.getElementById('matchMessage');
            if (newPassword.value != reNewPassword.value) {
                matchMessage.innerHTML = "Şifreleriniz eşleşmiyor.";
                return false;
            }
            return true;
        }

        $("#savePassword").kendoButton({
                click: function () {

                    if (validatePassword() && isMatch()) {
                        $.ajax({
                            type: "POST",
                            url: AdminApp.getBackendURL() + "user/updatePassword",
                            data: {
                                newPassword: CryptoJS.SHA256($("#newPassword").val()).toString(),
                                oldPassword: CryptoJS.SHA256($("#oldPassword").val()).toString()
                            },
                            success: function (response) {
                                showToast("success", "Şifreniz Başarılı Bir Şekilde Güncellendi");
                                $("#oldPassword").val("");
                                $("#newPassword").val("");
                                $("#reNewPassword").val("");

                                document.getElementById('newPassword').style.background = "White";
                                document.getElementById('reNewPassword').style.background = "White";
                            },
                            error: function (e) {
                                showToast("error", "Hata: Şifre Güncellenemedi !");
                                $("#oldPassword").val("");
                                $("#newPassword").val("");
                                $("#reNewPassword").val("");

                                document.getElementById('newPassword').style.background = "White";
                                document.getElementById('reNewPassword').style.background = "White";
                            }
                        });
                    } else {
                        showToast("error", "Hata: Şifreler Uyumsuz ve Hatalı !");
                    }
                }
            }
        );
    };

    return UserProfileManagementView;
})
;


