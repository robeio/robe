//@ sourceURL=Workspace.js
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
    'robe/view/Page'
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

            showProfileDialog(null, "Profil Bilgileri");
            kendo.destroy($('#dialogMessage'));
            $('#dialogMessage').html('');
            var profileView = new ProfileManagementView();
            profileView.parentPage = this;
            profileView.render();
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

        loadMenu();
        loadLogin();
    };

    function showProfileDialog(message, title) {
        if (message != null)
            $('#dialogMessage').html(message);
        if (title == null)
            title = "";
        $('#dialog').data("kendoWindow").title(title);
        $('#dialog').data("kendoWindow").center();
        $('#dialog').data("kendoWindow").open();
    }

    function onClickSettingsButton(e) {
        $("#dropdownMenu").toggle("slow");
    };

    function loadMenu() {
        var me = this;
        $.ajax({
            type: "GET",
            url: AdminApp.getBackendURL() + "menu/user",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                addIcons(response[0]);
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
                        openMenuItem(selection);
                    }
                });

            }
        });
    };

    function addIcons(menu) {
        if (menu.hasOwnProperty("items")) {
            for (var i = 0; i < menu.items.length; i++) {
                addIcons(menu.items[i]);
            }
        }
        menu.imageUrl = "./icon/menu/" + menu.cssClass.substring(8) + ".png";
    };

    var previousItem = "";

    function openMenuItem(menuitem) {

        if (menuitem.indexOf("k-") == 0)
            return;
        if (this.previousItem == menuitem)
            return;
        else
            this.previousItem = menuitem;
        try {
            kendo.destroy($('#container'));
            $('#container').html('');
            window.location.href = '#/' + menuitem;
        } catch (e) {
            console.error(menuitem + " JS: " + e);
        }
        kendo.fx($("#container")).fade("in").play();
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
        LoginView.render();
    };

    // Our module now returns our view
    return WorkspaceView;
});
