//@ sourceURL=ProfileManagement.js
function initializeProfileManagement() {
    var data;

    $.ajax({
        type: "GET",
        url: getBackendURL() + "user/email/" + $.cookie.read("userEmail"),
        contentType: "application/json",
        success: function (response) {
            $("#userEmail").val(response.email);
            $("#userName").val(response.name);
            $("#userSurname").val(response.surname);
            data = response;
        }
    });

    $("#btnProfileManagement").kendoButton({
        click: onBtnProfileManagement
    });

    function onBtnProfileManagement() {
        data.email = $("#userEmail").val();
        data.name = $("#userName").val();
        data.surname = $("#userSurname").val();

        $.ajax({
            type: "POST",
            url: getBackendURL() + "user",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                console.log(response);
                showToast("success", "Profil bilgileriniz başarı ile güncellendi.");

                /*  LOGOUT  */
                $.cookie.destroy("MedyAuthToken");
                location.reload();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(errorThrown);
                showToast("error", "Güncelleme esnasında bir hata oluştu.");
            }
        });
    }
}
