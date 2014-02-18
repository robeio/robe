//@ sourceURL=ProfileManagement.js
var ProfileManagement = robe.util.inherit(robe.view.Page, {
    name: "ProfileManagement",
    htmlPath: "./html/ProfileManagement.html",
    data: null,
    initialize: function () {
        $.ajax({
            type: "GET",
            url: AdminApp.getBackendURL() + "user/email/" + $.cookie.read("userEmail"),
            contentType: "application/json",
            success: function (response) {
                $("#userEmail").val(response.email);
                $("#userName").val(response.name);
                $("#userSurname").val(response.surname);
                data = response;
            }
        });

        $("#btnProfileManagement").kendoButton({
            click: this.onBtnProfileManagement
        });
    },

    onBtnProfileManagement: function () {
        data.email = $("#userEmail").val();
        data.name = $("#userName").val();
        data.surname = $("#userSurname").val();

        $.ajax({
            type: "POST",
            url: AdminApp.getBackendURL() + "user",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                console.log(response);
                showToast("success", "Profil bilgileriniz başarı ile güncellendi.");

                /*  LOGOUT  */
                $.cookie.destroy("auth-token");
                location.reload();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(errorThrown);
                showToast("error", "Güncelleme esnasında bir hata oluştu.");
            }
        });
    }
});
