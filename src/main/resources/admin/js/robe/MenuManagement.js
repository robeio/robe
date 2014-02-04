function initializeMenuManagement() {


    $("#gridMenus").kendoGrid({
        dataSource: MenuDataSource,
        sortable: true,
        resizable: true,
        pageable: {
            refresh: true
        },
        toolbar: [{
            name: "create",
            text: "Yeni Menü"
        }],
        columns: [{
            field: "name",
            title: "Ad",
            width: "110px"
        }, {
            field: "code",
            title: "Kod",
            width: "110px"
        }, {
            command: [{
                name: "edit",
                text: {
                    edit: "",
                    update: "Güncelle",
                    cancel: "İptal"
                },
                className:"grid-command-iconfix"
            }, {
                name: "destroy",
                text: "",
                className:"grid-command-iconfix"
            }],
            title: "&nbsp;",
            width: "80px"
        }],
        group: {
            field: "parentOid",
            aggregates: [{
                field: "oid",
                aggregate: "count"
            }]
        },
        editable: {
            mode: "popup",
            window: {
                title: "Kayıt"
            }
        }
    });
    

    $("#treeMenus").kendoTreeView({
        dragAndDrop: true,
        dataSource: MenuHierarchicalDataSource,
        dataTextField: "name",
        drop: onTreeMenuDrop,
        drag: onTreeMenuDrag

    });


    $("#btnMenuManagementHelp").kendoButton({
        click: onShowHelp
    });

    function onShowHelp () {
        wnd = $("#menuManagementHelpWindow").kendoWindow({
            title: "Yardım",
            modal: true,
            visible: false,
            resizable: false,
            width: 500
            }).data("kendoWindow");

            wnd.center().open();

    };

}

function onTreeMenuDrag(e) {
    // if the current status is "insert-top/middle/bottom"
    if (e.statusClass.indexOf("insert") >= 0) {
        // deny the operation
        e.setStatusClass("k-denied");
        return;
    }
}

function onTreeMenuDrop(e) {

    if (!e.valid) {
        return;
    }
    var treeview = $("#treeMenus").data("kendoTreeView");
    var sourceOid = treeview.dataItem(e.sourceNode).oid;
    var destinationOid = sourceOid;
    if (e.dropPosition == "over")
        destinationOid = treeview.dataItem(e.destinationNode).oid;

    $.ajax({
        type: "POST",
        url: getBackendURL() + "menu/movenode/" + sourceOid + "/" + destinationOid,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function() {
            var treeview = $("#treeMenus").data("kendoTreeView");
        }
    });

}
