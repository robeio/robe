function initializeMenuManagement() {
    var gridDataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: getBackendURL()+"menu/all",
                dataType: "json",
                contentType: "application/json"
            },
            update: {
                type: "POST",
                url: getBackendURL()+"menu",
                dataType: "json",
                contentType: "application/json"
            },
            destroy: {
                type: "DELETE",
                url: getBackendURL()+"menu",
                dataType: "json",
                contentType: "application/json"
            },
            create: {
                type: "PUT",
                url: getBackendURL()+"menu",
                dataType: "json",
                contentType: "application/json"
            },
            parameterMap: function(options, operation) {
                if (operation !== "read") {
                    return kendo.stringify(options);
                }
            }
        },
        batch: false,
        pageSize: 20,
        schema: {
            model: {
                id: "oid",
                fields: {
                    oid: {
                        editable: false,
                        nullable: false
                    },
                    lastUpdated: {
                        editable: true,
                        nullable: false
                    },
                    deleted: {
                        editable: false,
                        nullable: true
                    },
                    name: {
                        editable: true,
                        nullable: false
                    },
                    code: {
                        editable: true,
                        nullable: false
                    }
                }
            }
        }
    });

    $("#gridMenus").kendoGrid({
        dataSource: gridDataSource,
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
        toolbar: [{name:"create",text:"Ekle"}],
        columns: [{
            field: "name",
            title: "Ad",
            width: "75px"
        }, {
            field: "code",
            title: "Kod",
            width: "110px"
        }, {
            command: [{
                name: "edit",
                text: ""
            }, {
                name: "destroy",
                text: ""
            }],
            title: "&nbsp;",
            width: "90px"
        }],
        group: {
            field: "parentOid",
            aggregates: [{
                field: "oid",
                aggregate: "count"
            }]
        },
        editable: "popup",
    });
    var treeModel = {
        model: {
            id: "oid",
            fields: {
                oid: {
                    editable: false,
                    nullable: false
                },
                lastUpdated: {
                    editable: true,
                    nullable: false
                },
                deleted: {
                    editable: false,
                    nullable: true
                },
                name: {
                    editable: true,
                    nullable: false
                },
                code: {
                    editable: true,
                    nullable: false
                },
                children: {}
            },
            hasChildren: function(item) {
                return item.items != null;
            }
        }
    }
    var treeDataSource = new kendo.data.HierarchicalDataSource({
        transport: {
            read: {
                type: "GET",
                url: getBackendURL()+"menu/roots",
                dataType: "json",
                contentType: "application/json"
            },

        },
        schema: treeModel
    });


    $("#treeMenus").kendoTreeView({
        dragAndDrop: true,
        dataSource: treeDataSource,
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
        url: getBackendURL()+"menu/movenode/" + sourceOid + "/" + destinationOid,
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function() {
            var treeview = $("#treeMenus").data("kendoTreeView");
        }
    });

}