//@ sourceURL=MenuManagement.js
define([
    'text!html/MenuManagement.html',
    'admin/data/HierarchicalDataSources',
    'admin/data/DataSources',

    'kendo/kendo.grid.min',
    'kendo/kendo.window.min',
    'kendo/kendo.treeview.min'

], function (view) {
    var MenuManagementView = Backbone.View.extend({
        el: $('#container'),
        render: function () {
            // Append our compiled template to this Views "el"
            this.$el.append(view);
            this.initial();
        },

        initial: function () {
            MenuHierarchicalDataSource.read();
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


            $("#treeMenus").kendoTreeView({
                dragAndDrop: true,
                dataSource: MenuHierarchicalDataSource,
                dataTextField: "name",
                drop: this.onTreeMenuDrop,
                drag: this.onTreeMenuDrag

            });


            $("#btnMenuManagementHelp").kendoButton({
                click: this.onShowHelp
            });
        },

        onShowHelp: function () {
            wnd = $("#menuManagementHelpWindow").kendoWindow({
                title: "Yardım",
                modal: true,
                visible: false,
                resizable: false,
                width: 500
            }).data("kendoWindow");

            wnd.center().open();

        },

        onTreeMenuDrag: function (e) {
            // if the current status is "insert-top/middle/bottom"
            if (e.statusClass.indexOf("insert") >= 0) {
                // deny the operation
                e.setStatusClass("k-denied");
                return;
            }
        },

        onTreeMenuDrop: function (e) {

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

        }
    });
    return MenuManagementView;
});
