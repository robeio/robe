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

    function showDialog(message, title) {
        if (message != null)
            $('#dialogMessage').html(message);
        if (title == null)
            title = "";
        $('#dialog').data("kendoWindow").title(title);
        $('#dialog').data("kendoWindow").center();
        $('#dialog').data("kendoWindow").open();
    };

    UserProfileManagementView.initialize = function () {

        var me = this;
        $.ajax({
            type: "GET",
            url: AdminApp.getBackendURL() + "user/profile/" + $.cookie.read("userEmail"),
            contentType: "application/json",
            success: function (response) {
                $("#emailAddress").val(response.email);
                $("#firstAndLastName").val(response.name + " " + response.surname);
                me.data = response;
            },
            error: function (e) {

            }
        });

        function validatePassword() {
            var error = "";
            var illegalChars = /[\W_]/; // allow only letters and numbers

            var newPassword = document.getElementById('newPassword');
            var reNewPassword = document.getElementById('reNewPassword');
            var message = document.getElementById('confirmMessage');

            var goodColor = "#66cc66";
            var badColor = "#ff6666";

            if (newPassword.value == "") {
                newPassword.style.background = badColor;
                reNewPassword.style.background = badColor;
                error = "Henüz Şifre Girmediniz.\n";
                message.innerHTML = error;
            } else if ((newPassword.value.length < 4) || (newPassword.value.length > 15)) {
                error = "Şifreniz en az 4 en fazla 15 karakter uzunluğunda olmalı. \n";
                message.innerHTML = error;
                newPassword.style.background = badColor;
                reNewPassword.style.background = badColor;
            } else if (illegalChars.test(newPassword.value)) {
                error = "Şifreniz geçersiz karakterler içermektedir.\n";
                message.innerHTML = error;
                newPassword.style.background = badColor;
                reNewPassword.style.background = badColor;
            } else if (!((newPassword.value.search(/(a-z)+/)) && (newPassword.value.search(/(0-9)+/)))) {
                error = "Şifrenizde en az bir adet rakam olmalıdır.\n";
                message.innerHTML = error;
                newPassword.style.background = badColor;
                reNewPassword.style.background = badColor;
            } else if (newPassword.value != reNewPassword.value) {
                error = "Şifreler Eşleşmiyor.\n";
                message.innerHTML = error;
                newPassword.style.background = badColor;
                reNewPassword.style.background = badColor;
            } else {
                newPassword.style.background = goodColor;
                reNewPassword.style.background = goodColor;
                message.innerHTML = "";
            }
            return error;
        }


        $("#savePassword").kendoButton({
            click: function () {
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
                    },
                    error: function (e) {
                        showToast("error", "Hata: Şifre Güncellenemedi !");
                        $("#oldPassword").val("");
                        $("#newPassword").val("");
                        $("#reNewPassword").val("");
                    }
                });
            }
        });


    };


    return UserProfileManagementView;
});


