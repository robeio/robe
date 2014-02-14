//@ sourceURL=Workspace.js
$.ajaxSetup({
    dataType: "json",
    statusCode: {
        401: function () {
            loadLogin();
        },
        422: function (xhr) {
            var errors = JSON.parse(xhr.responseText);
            var msg = "<span style='float:right;'>";
            $.each(errors, function (index, error) {
                if (error.name != "") {
                    msg += "<br> <b>" + error.name
                    if (error.message != "")
                        msg += ":";
                    msg += "</b> "
                }
                if (error.message != "") {
                    msg += error.message;
                }
            });
            showDialog("<img style='float:left; margin-right:10px;' src='./icon/close.png'/>" + msg + "</span>", "Hata");
        }
    },
    beforeSend: function (xhr) {

    },
    complete: function (xhr, status) {
        console.log(status + " : " + xhr);
    }
});
loadConfig();
$(document).ready(function () {

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
        $('#dialogMessage').load("./ProfileManagement.html", function () {
            $.getScript("../js/robe/ProfileManagement.js", function () {
                showDialog(null, "Profil Bilgileri");
                eval("initializeProfileManagement();");
            });
        });
    });

    $("#logout").click(function () {
        $.cookie.destroy("MedyAuthToken");
        location.reload();
    });

    $("#settings").kendoButton({
        click: onClickSettingsButton
    });

    function onClickSettingsButton(e) {
        $("#dropdownMenu").toggle("slow");
    }

    $('#dialog').kendoWindow({
        actions: ["Close"],
        modal: true,
        visible: false
    });


    loadLogin();

    $(document).ajaxStart(function () {
        showIndicator(true);
    });
    $(document).ajaxStop(function () {
        showIndicator(false);
    });


});

function loadMenu() {
    $.ajax({
        type: "GET",
        url: getBackendURL() + "menu/user",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            addIcons(response[0]);
            $('#menu').kendoPanelBar({
                dataSource: response[0].items,
                select: onSelect
            });

        }
    });
}

function addIcons(menu) {
    if (menu.hasOwnProperty("items")) {
        for (var i = 0; i < menu.items.length; i++) {
            addIcons(menu.items[i]);
        }
    }
    menu.imageUrl = "../icon/menu/" + menu.cssClass.substring(8) + ".png";
}

function onSelect(e) {

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

function openMenuItem(menuitem) {

    kendo.destroy($('#container'));
    $('#container').html('');

    if (menuitem.indexOf("k-") == 0)
        return;

    $('#container').load("./" + menuitem + ".html", function () {
        try {
            $.getScript("../js/robe/" + menuitem + ".js", function () {
                try {
                    eval("initialize" + menuitem + "();");
                } catch (e) {
                    console.error(menuitem + " JS: " + e);
                }
            });
        } catch (e) {
            console.error(menuitem + " HTML: " + e);
        }
        kendo.fx($("#container")).fade("in").play();
    });
}
//
function showIndicator(show) {
    if (show)
        $("#progressBar").data("kendoProgressBar").value(0);
    else
        $("#progressBar").data("kendoProgressBar").value(1);

}

function showDialog(message, title) {
    if (message != null)
        $('#dialogMessage').html(message);
    if (title == null)
        title = "";
    $('#dialog').data("kendoWindow").title(title);
    $('#dialog').data("kendoWindow").center();
    $('#dialog').data("kendoWindow").open();
}

function loadLogin() {
    $('#dialogMessage').load("./Login.html", function () {
        showDialog(null, "Giriş");
        $.getScript("../js/robe/Login.js", function () {
            eval("initializeLogin();");
        });

    });
}

function loadConfig() {
    $.ajax({
        dataType: "json",
        url: "../config.json",
        success: function (response) {
            backendURL = response.backendURL;
            adminURL = response.adminURL;

            loadJS(['../js/robe/Validations.js',
                '../js/robe/Models.js',
                '../js/robe/data/SingletonHierarchicalDataSource.js',
                '../js/robe/data/SingletonDataSource.js',
                '../js/robe/HierarchicalDataSources.js',
                '../js/robe/DataSources.js']);


        }
    });
}
var backendURL = "../../robe/";
var adminURL = "";

function getBackendURL() {
    return backendURL;
}

function getAdminURL() {
    return adminURL;
}


function loadJS(sources) {
    var size = sources.length;
    if (size > 0) {
        $.getScript(sources[0], function () {
            loadJS(sources.slice(1));
        });
    }
}