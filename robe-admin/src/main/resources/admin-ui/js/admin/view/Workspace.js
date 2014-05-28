var WorkspaceView;
define([
    'text!html/Workspace.html',
    'view/Login',
    'view/ProfileManagement',
    'admin/data/SingletonDataSource',
    'admin/data/SingletonHierarchicalDataSource',
    'admin/Models',

    'kendo/kendo.fx.min',
    'kendo/kendo.progressbar.min',
    'kendo/kendo.button.min',
    'kendo/kendo.window.min',
    'kendo/kendo.panelbar.min',
    'robe/view/RobeView'
], function (view, LoginView, ProfileManagementView) {

    WorkspaceView = new RobeView("WorkspaceView", view, "container");

    WorkspaceView.render = function () {
        kendo.destroy($('#body'));
        $('#body').html('');
        $('#body').append(view);
        WorkspaceView.initialize();
    };

    WorkspaceView.initialize = function () {
        var me = this;
        $("#progressBar").kendoProgressBar({
            min: 0,
            max: 1,
            type: "value",
            showStatus: false,
            animation: {
                duration: 200
            }
        });
        kendo.destroy($("#container"));
        $("#container").html("");
        $("#profile").click(function () {

            showDialog(null, "Profil Bilgileri");
            kendo.destroy($('#dialogMessage'));
            $('#dialogMessage').html('');
            ProfileManagementView.render();
        });

        $("#logout").click(function () {
            $.cookie.destroy("auth-token");
            location.reload();
        });

        $("#settings").kendoButton({
            click: onClickSettingsButton
        });

        $('#dialog').kendoWindow({
            actions: ["Close"],
            modal: true,
            visible: false
        });

        $(document).ajaxStart(function () {
            showIndicator(true);
        });
        $(document).ajaxStop(function () {
            showIndicator(false);
        });

        loadLogin();

        function onClickSettingsButton(e) {
            $("#dropdownMenu").toggle("slow");
        };

        function showIndicator(show) {
            if (show)
                $("#progressBar").data("kendoProgressBar").value(0);
            else
                $("#progressBar").data("kendoProgressBar").value(1);
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

        function loadLogin() {
            showDialog(null, "Giriş");
            kendo.destroy($('#dialogMessage'));
            $('#dialogMessage').html('');
            LoginView.parentPage = me;
            LoginView.render();
        };
    };

    WorkspaceView.loadMenu = function () {
        $.ajax({
            type: "GET",
            url: AdminApp.getBackendURL() + "menu/user",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                WorkspaceView.addIcons(response[0]);
                $('#menu').kendoPanelBar({
                    dataSource: response[0].items,
                    select: function (e) {
                        var selection = "k-";
                        for (var i = 0; i < e.item.classList.length; i++) {
                            var css = e.item.classList[i];
                            if (css.indexOf("command:") == 0) {
                                selection = css.substring(8);
                                break;
                            }
                        }
                        WorkspaceView.openMenuItem(selection);
                    }
                });

            }
        });
    };

    WorkspaceView.previousItem = "";

    WorkspaceView.openMenuItem = function (menuitem) {

        if (menuitem.indexOf("k-") == 0)
            return;
        if (WorkspaceView.previousItem == menuitem)
            return;
        else
            WorkspaceView.previousItem = menuitem;
        try {
            kendo.destroy($('#container'));
            $('#container').html('');
            window.location.href = '#/' + menuitem;
        } catch (e) {
            console.error(menuitem + " JS: " + e);
        }
        kendo.fx($("#container")).fade("in").play();
    };

    WorkspaceView.addIcons = function (menu) {
        if (menu.hasOwnProperty("items")) {
            for (var i = 0; i < menu.items.length; i++) {
                WorkspaceView.addIcons(menu.items[i]);
            }
        }
        menu.imageUrl = "./icon/menu/" + menu.cssClass.substring(8) + ".png";
    };


    return WorkspaceView;
});
