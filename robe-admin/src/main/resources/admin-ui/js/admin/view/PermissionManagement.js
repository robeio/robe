define([
    'text!html/PermissionManagement.html',
    'admin/data/HierarchicalDataSources',
    'admin/data/DataSources',

    'kendo/kendo.splitter.min',
    'kendo/kendo.window.min',
    'kendo/kendo.grid.min',
    'kendo/kendo.dropdownlist.min',
    'kendo/kendo.treeview.min'

], function (view) {
    return Backbone.View.extend({
        el: $('#container'),
        render: function () {
            // Append our compiled template to this Views "el"
            this.$el.append(view);
            this.initial();
        },

        me: "",

        initial: function () {
            me = this;
            var me = this;

            $("#horizontalTabStrips").kendoSplitter({
                panes: [
                    { collapsible: false, size: "20%", resizable: false },
                    { collapsible: false, size: "20%", resizable: false  },
                    { collapsible: false, size: "60%", scrollable: true}
                ]
            });

            $("#gridServices").kendoGrid({
                dataSource: ServiceDataSource.get(),
                width: 75,
                columns: [
                    {
                        template: "<input type='checkbox'/>",
                        field: "selected",
                        title: "&nbsp;",
                        width: 5
                    },
                    {
                        field: "method",
                        title: "Method",
                        width: 15
                    },
                    {
                        field: "path",
                        title: "Servis",
                        width: 50
                    }
                ]
            });

            $("#gridServices").data("kendoGrid").table.on("click", "input[type=checkbox]", this.selectRow);


            $("#cmbRoles").kendoDropDownList({
                dataTextField: "name",
                dataValueField: "oid",
                dataSource: RoleDataSource.get(),
                change: function () {
                    var roleOid = $("#cmbRoles").val();
                    $.ajax({
                        type: "GET",
                        url: AdminApp.getBackendURL() + "permission/" + roleOid + "/menu",
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        success: function (response) {
                            var tree = $("#treeMenus").data("kendoTreeView");
                            tree.expand(".k-item");
                            me.checkByNodeIds(tree.dataSource.data(), response);
                        }
                    });
                    $.ajax({
                        type: "GET",
                        url: AdminApp.getBackendURL() + "permission/" + roleOid + "/service",
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        success: function (response) {
                            me.checkRows(response);
                        }
                    });
                },
                autoBind: false,
                text: "Seçiniz...",
                index: -1
            });


            $("#treeMenus").kendoTreeView({
                checkboxes: {
                    checkChildren: true
                },
                dataSource: MenuHierarchicalDataSource.get(),
                dataTextField: "name"
            });

            $("#btnSavePermission").kendoButton({
                click: me.onSave
            });


            $("#btnMenuManagementHelp").kendoButton({
                click: me.onShowHelp
            });


        },

        // function that gathers IDs of checked nodes
        checkedNodeIds: function (me, nodes, checkedNodes) {
            for (var i = 0; i < nodes.length; i++) {
                if (nodes[i].checked) {
                    me.checkedNodes.push(nodes[i].id);
                }

                if (nodes[i].hasChildren) {
                    me.checkedNodeIds(me, nodes[i].children.view(), checkedNodes);
                }
            }
        },

        // function that gathers IDs of checked nodes
        checkByNodeIds: function (nodes, targetNodes) {
            var tree = $("#treeMenus").data("kendoTreeView");
            var nodeUid;
            var nodeOid;
            for (var i = 0; i < nodes.length; i++) {
                nodeUid = nodes[i].uid;
                nodeOid = nodes[i].id;
                var isChecked = $.inArray(nodes[i].id, targetNodes) != -1;
                tree.findByUid(nodeUid).find("input[type=checkbox]").prop("checked", isChecked);
                nodes[i].set("checked", isChecked);
                if (nodes[i].hasChildren) {
                    me.checkByNodeIds(nodes[i].children.data(), targetNodes);
                }
            }
        },

        onShowHelp: function () {
            wnd = $("#permissionManagementHelpWindow").kendoWindow({
                title: "Yardım",
                modal: true,
                visible: false,
                resizable: false,
                width: 500
            }).data("kendoWindow");

            wnd.center().open();

        },

        onSave: function () {
            var roleOid = $("#cmbRoles").val();
            var checkedNodes = [];
            var treeMenus = $("#treeMenus").data("kendoTreeView");

            me.checkedNodeIds(treeMenus.dataSource.view(), checkedNodes);
            $.ajax({
                type: "PUT",
                url: AdminApp.getBackendURL() + "permission/" + roleOid + "/menu",
                dataType: "json",
                data: kendo.stringify(checkedNodes),
                contentType: "application/json; charset=utf-8",
                success: function () {
                }
            });


            $.ajax({
                type: "PUT",
                url: AdminApp.getBackendURL() + "permission/" + roleOid + "/service",
                dataType: "json",
                data: kendo.stringify(this.getCheckedRows()),
                contentType: "application/json; charset=utf-8",
                success: function (result) {
                }
            });


        },


//on click of the checkbox:
        selectRow: function () {
            var checked = this.checked,
                row = $(this).closest("tr");
            if (checked) {
                //-select the row
                row.addClass("k-state-selected");
            } else {
                //-remove selection
                row.removeClass("k-state-selected");
            }
        },

//on dataBound event restore previous selected rows:
        checkRows: function (checkedServiceOids) {
            var grid = $("#gridServices").data("kendoGrid");
            var gridTbody = grid.tbody;
            var view = grid.dataSource.view();
            for (var i = 0; i < view.length; i++) {
                var row = gridTbody.find("tr[data-uid='" + view[i].uid + "']");
                if (checkedServiceOids.indexOf(view[i].id) >= 0) {
                    row.find("input[type=checkbox]").prop("checked",true);
                    row.addClass("k-state-selected");
                } else {
                    row.find("input[type=checkbox]").prop("checked",false);
                    row.removeClass("k-state-selected");
                }
            }
        },

        getCheckedRows: function () {
            var grid = $("#gridServices").data("kendoGrid");
            var gridTbody = grid.tbody;
            var view = grid.dataSource.view();
            var checkedServiceOids = [];
            for (var i = 0; i < view.length; i++) {
                var className = gridTbody.find("tr[data-uid='" + view[i].uid + "']").attr("class");
                if (className != null && className.indexOf("k-state-selected") >= 0) {
                    checkedServiceOids.push(view[i].get("oid"))
                }
            }
            return checkedServiceOids;
        }
    });
});