function initializeMenuManagement() {

    $("#gridMenus").kendoGrid({
        dataSource: MenuDataSource,
        groupable: {
            messages: {
                empty: "Gruplandırma için kolonu buraya sürükleyin"
            }
        },
        sortable: true,
        resizable: true,
        pageable: {
            refresh: true
        },
        toolbar: [{
            name: "create",
            text: "Ekle"
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