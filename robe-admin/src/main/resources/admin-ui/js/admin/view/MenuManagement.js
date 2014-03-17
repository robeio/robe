//@ sourceURL=MenuManagement.js
var MenuManagementView;
define([
    'text!html/MenuManagement.html',
    'admin/data/DataSources',

    'kendo/kendo.grid.min',
    'kendo/kendo.window.min',
    'kendo/kendo.treeview.min',
    'robe/view/Page'
], function (view) {

    MenuManagementView = new RobeView("MenuManagementView", view, "container");
    MenuManagementView.render = function () {
        $('#container').append(view);
        MenuManagementView.initialize();
    };

    MenuManagementView.initialize = function () {
        $("#gridMenus").kendoGrid({
            dataSource: MenuDataSource.get(),
            sortable: true,
            resizable: true,
            pageable: {
                refresh: true
            },
            toolbar: [
                {
                    name: "create",
                    text: "Yeni Menü"
                }
            ],
            columns: [
                {
                    field: "name",
                    title: "Ad",
                    width: "110px"
                },
                {
                    field: "code",
                    title: "Kod",
                    width: "110px"
                },
                {
                    command: [
                        {
                            name: "edit",
                            text: {
                                edit: "",
                                update: "Güncelle",
                                cancel: "İptal"
                            },
                            className: "grid-command-iconfix"
                        },
                        {
                            name: "destroy",
                            text: "",
                            className: "grid-command-iconfix"
                        }
                    ],
                    title: "&nbsp;",
                    width: "80px"
                }
            ],
            group: {
                field: "parentOid",
                aggregates: [
                    {
                        field: "oid",
                        aggregate: "count"
                    }
                ]
            },
            editable: {
                mode: "popup",
                window: {
                    title: "Kayıt"
                },
                confirmation: "Silmek istediğinizden emin misiniz?",
                confirmDelete: "Yes"
            }
        });

        $.ajax({
            type: "GET",
            url: AdminApp.getBackendURL() + "menu/roots",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                var dataSource = new kendo.data.HierarchicalDataSource({
                    data: response,
                    schema: MenuTreeModel
                });
                $("#treeMenus").data("kendoTreeView").setDataSource(dataSource);
            }
        });

        $("#treeMenus").kendoTreeView({
            dragAndDrop: true,
            dataTextField: "name",
            drop: this.onTreeMenuDrop,
            drag: this.onTreeMenuDrag

        });

        $("#btnMenuManagementHelp").kendoButton({
            click: this.onShowHelp
        });

        function onShowHelp() {
            wnd = $("#menuManagementHelpWindow").kendoWindow({
                title: "Yardım",
                modal: true,
                visible: false,
                resizable: false,
                width: 500
            }).data("kendoWindow");

            wnd.center().open();

        };

        function onTreeMenuDrag(e) {
            // if the current status is "insert-top/middle/bottom"
            if (e.statusClass.indexOf("insert") >= 0) {
                // deny the operation
                e.setStatusClass("k-denied");
                return;
            }
        };

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
                url: AdminApp.getBackendURL() + "menu/movenode/" + sourceOid + "/" + destinationOid,
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function () {
                    var treeview = $("#treeMenus").data("kendoTreeView");
                }
            });

        };
    };

    return MenuManagementView;
});
