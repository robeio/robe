//@ sourceURL=Workspace.js
var Workspace = robe.util.inherit(robe.view.Page, {
    name:"Workspace",
    htmlPath:"./html/Workspace.html",
    initialize: function () {
        $("#progressBar").kendoProgressBar({
            min: 0,
            max: 1,
            type: "value",
            showStatus: false,
            animation: {
                duration: 200
            }
        });

        $("#profile").click(function () {
            Workspace.showDialog(null, "Profil Bilgileri");
            ProfileManagement.show();
        });

        $("#logout").click(function () {
            $.cookie.destroy("auth-token");
            location.reload();
        });

        $("#settings").kendoButton({
            click: this.onClickSettingsButton
        });


        $('#dialog').kendoWindow({
            actions: ["Close"],
            modal: true,
            visible: false
        });


        $(document).ajaxStart(function () {
            Workspace.showIndicator(true);
        });
        $(document).ajaxStop(function () {
            Workspace.showIndicator(false);
        });

        Login.parentPage = this;
        Login.setContainerId("dialogMessage");

        ProfileManagement.parentPage = this;
        ProfileManagement.setContainerId("dialogMessage");

        Workspace.loadLogin();


    },
    onClickSettingsButton: function (e) {
        $("#dropdownMenu").toggle("slow");
    },
    loadMenu: function () {
        $.ajax({
            type: "GET",
            url: AdminApp.getBackendURL() + "menu/user",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                Workspace.addIcons(response[0]);
                $('#menu').kendoPanelBar({
                    dataSource: response[0].items,
                    select: Workspace.onSelect
                });

            }
        });
    },
    addIcons: function (menu) {
        if (menu.hasOwnProperty("items")) {
            for (var i = 0; i < menu.items.length; i++) {
                Workspace.addIcons(menu.items[i]);
            }
        }
        menu.imageUrl = "./icon/menu/" + menu.cssClass.substring(8) + ".png";
    },

    onSelect: function (e) {
        var selection = "k-";
        for (var i = 0; i < e.item.classList.length; i++) {
            var css = e.item.classList[i];
            if (css.indexOf("command:") == 0) {
                selection = css.substring(8);
                break;
            }
        }
        Workspace.openMenuItem(selection);
    },
    openMenuItem: function (menuitem) {

        kendo.destroy($('#container'));
        $('#container').html('');

        if (menuitem.indexOf("k-") == 0)
            return;
        try {
            eval(menuitem + ".setContainerId('container'); " + menuitem + ".show();");
        } catch (e) {
            console.error(menuitem + " JS: " + e);
        }
        kendo.fx($("#container")).fade("in").play();
    },
    showIndicator: function (show) {
        if (show)
            $("#progressBar").data("kendoProgressBar").value(0);
        else
            $("#progressBar").data("kendoProgressBar").value(1);

    },
    showDialog: function (message, title) {
        if (message != null)
            $('#dialogMessage').html(message);
        if (title == null)
            title = "";
        $('#dialog').data("kendoWindow").title(title);
        $('#dialog').data("kendoWindow").center();
        $('#dialog').data("kendoWindow").open();
    },

    loadLogin: function () {
        Workspace.showDialog(null, "Giriş");
        Login.show();
    }
});
