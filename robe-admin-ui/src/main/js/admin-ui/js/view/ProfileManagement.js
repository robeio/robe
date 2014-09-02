var ProfileManagementView;

define([
    'text!html/ProfileManagement.html',
    'kendo/kendo.button.min',
    'robe/AlertDialog',
    'robe/view/RobeView'
], function (view) {

    ProfileManagementView = new RobeView("ProfileManagementView", view, "container");

    ProfileManagementView.render = function () {
        $('#dialogMessage').append(view);
        ProfileManagementView.initialize();
    };

    var data = null;

    ProfileManagementView.initialize = function () {
        var me = this;
        $.ajax({
            type: "GET",
            url: AdminApp.getBackendURL() + "user/profile/" + $.cookie.read("userEmail"),
            contentType: "application/json",
            success: function (response) {
                $("#userEmail").val(response.email);
                $("#userName").val(response.name);
                $("#userSurname").val(response.surname);
                me.data = response;
                $("#btnDialogClose").css('display', 'none');
            }
        });

        $("#btnProfileManagement").kendoButton({
            icon: "tick",
            click: function () {
                me.data.email = $("#userEmail").val();
                me.data.name = $("#userName").val();
                me.data.surname = $("#userSurname").val();

                $.ajax({
                    type: "POST",
                    url: AdminApp.getBackendURL() + "user",
                    data: JSON.stringify(me.data),
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
        $("#btnProfileManagementClose").kendoButton({
            icon: "close",
            click: function () {
                $('#dialog').data("kendoWindow").close();
            }
        });
    };

    return ProfileManagementView;
});