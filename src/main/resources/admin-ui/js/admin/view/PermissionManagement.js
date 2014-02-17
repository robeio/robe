var PermissionManagement = robe.util.inherit(robe.view.Page, {
    name: "PermissionManagement",
    htmlPath:"./html/PermissionManagement.html",
    initialize: function () {

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
                    template: "<input type='checkbox' class='checkbox' />",
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

        $("#gridServices").data("kendoGrid").table.on("click", ".checkbox", this.selectRow);


        $("#cmbRoles").kendoDropDownList({
            dataTextField: "name",
            dataValueField: "oid",
            dataSource: RoleDataSource.get(),
            change: this.onCmbRolesChange,
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
            click: this.onSave
        });


        $("#btnMenuManagementHelp").kendoButton({
            click: this.onShowHelp
        });
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

    onCmbRolesChange: function () {
        var roleOid = $("#cmbRoles").val();
        var me = this;
        $.ajax({
            type: "GET",
            url: getBackendURL() + "permission/" + roleOid + "/menu",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                $("#treeMenus").data("kendoTreeView").expand(".k-item");
                me.checkByNodeIds($("#treeMenus").data("kendoTreeView").dataSource.data(), response);
            }
        });
        $.ajax({
            type: "GET",
            url: getBackendURL() + "permission/" + roleOid + "/service",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success: function (response) {
                me.checkRows(response);
            }
        });
    },

    onSave: function () {
        var roleOid = $("#cmbRoles").val();
        var checkedNodes = [];
        var treeMenus = $("#treeMenus").data("kendoTreeView");

        this.checkedNodeIds(treeMenus.dataSource.view(), checkedNodes);
        $.ajax({
            type: "PUT",
            url: getBackendURL() + "permission/" + roleOid + "/menu",
            dataType: "json",
            data: kendo.stringify(checkedNodes),
            contentType: "application/json; charset=utf-8",
            success: function () {
            }
        });


        $.ajax({
            type: "PUT",
            url: getBackendURL() + "permission/" + roleOid + "/service",
            dataType: "json",
            data: kendo.stringify(getCheckedRows()),
            contentType: "application/json; charset=utf-8",
            success: function (result) {
            }
        });

    },

// function that gathers IDs of checked nodes
    checkedNodeIds: function (nodes, checkedNodes) {
        for (var i = 0; i < nodes.length; i++) {
            if (nodes[i].checked) {
                this.checkedNodes.push(nodes[i].id);
            }

            if (nodes[i].hasChildren) {
                this.checkedNodeIds(nodes[i].children.view(), checkedNodes);
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
            tree.findByUid(nodeUid).find(":checkbox").prop("checked", isChecked);
            nodes[i].set("checked", isChecked);
            if (nodes[i].hasChildren) {
                this.checkByNodeIds(nodes[i].children.data(), targetNodes);
            }
        }
    },

//on click of the checkbox:
    selectRow: function () {
        var checked = this.checked,
            row = $(this).closest("tr"),
            grid = $("#gridServices").data("kendoGrid"),
            dataItem = grid.dataItem(row);
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
            if (checkedServiceOids.indexOf(view[i].id) >= 0) {
                gridTbody.find("tr[data-uid='" + view[i].uid + "']")
                    .addClass("k-state-selected")
                    .find(".checkbox")
                    .attr("checked", "checked");
            } else {
                gridTbody.find("tr[data-uid='" + view[i].uid + "']")
                    .removeClass("k-state-selected")
                    .find(".checkbox")
                    .removeAttr("checked");
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