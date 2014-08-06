var PermissionManagement;
var checkAllFlag = true;

define([
    'text!html/PermissionManagement.html',
    'admin/data/DataSources',

    'kendo/kendo.splitter.min',
    'kendo/kendo.window.min',
    'kendo/kendo.grid.min',
    'kendo/kendo.dropdownlist.min',
    'kendo/kendo.treeview.min',
    'robe/view/RobeView'
], function (view) {

    PermissionManagement = new RobeView("PermissionManagement", view, "container");

    PermissionManagement.render = function () {
        console.log("PermissionManagement");
        $('#container').append(view);
        PermissionManagement.initialize();
    };

    PermissionManagement.initialize = function () {
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
                    template: '<input type="checkbox" class="checkRow"/>',
                    headerTemplate: '<input type="checkbox" id="checkAll"/>',
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


        $("#checkAll").click(function () {
            if (checkAllFlag) {
                for (var i = 0; i < $(".checkRow").length; i++) {
                    var row = $(".checkRow")[i];
                    row.setAttribute("checked", true);
                    row.parentElement.parentElement.setAttribute("class", "k-state-selected");
                }
                checkAllFlag = false;
            } else {
                for (var i = 0; i < $(".checkRow").length; i++) {
                    var row = $(".checkRow")[i];
                    row.removeAttribute("checked");
                    row.parentElement.parentElement.removeAttribute("class", "k-state-selected");
                }
                checkAllFlag = true;
            }
        });

        $("#gridServices").data("kendoGrid").table.on("click", "input[type=checkbox]", selectRow);


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
                        checkByNodeIds(tree.dataSource.data(), response);
                    }
                });
                $.ajax({
                    type: "GET",
                    url: AdminApp.getBackendURL() + "permission/" + roleOid + "/service",
                    dataType: "json",
                    contentType: "application/json; charset=utf-8",
                    success: function (response) {
                        checkRows(response);
                    }
                });
            },
            autoBind: false,
            text: "Seçiniz...",
            index: -1
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
            checkboxes: {
                checkChildren: true
            },
            dataTextField: "name"
        });

        $("#btnSavePermission").kendoButton({
            click: function () {
                var roleOid = $("#cmbRoles").val();
                var checkedNodes = [];
                var treeMenus = $("#treeMenus").data("kendoTreeView");

                checkedNodeIds(me, treeMenus.dataSource.view(), checkedNodes);
                $.ajax({
                    type: "PUT",
                    url: AdminApp.getBackendURL() + "permission/" + roleOid + "/menu",
                    dataType: "json",
                    data: kendo.stringify(checkedNodes),
                    contentType: "application/json; charset=utf-8",
                    success: function () {
                        showToast("success", "Başarılı")
                    }
                });


                $.ajax({
                    type: "PUT",
                    url: AdminApp.getBackendURL() + "permission/" + roleOid + "/service",
                    dataType: "json",
                    data: kendo.stringify(getCheckedRows()),
                    contentType: "application/json; charset=utf-8",
                    success: function (result) {
                    }
                });
            }
        });

        $("#btnMenuManagementHelp").kendoButton({
            click: onShowHelp
        });
        // function that gathers IDs of checked nodes
        function checkedNodeIds(me, nodes, checkedNodes) {
            for (var i = 0; i < nodes.length; i++) {
                if (nodes[i].checked) {
                    checkedNodes.push(nodes[i].id);
                }

                if (nodes[i].hasChildren) {
                    checkedNodeIds(me, nodes[i].children.view(), checkedNodes);
                }
            }
        };

        // function that gathers IDs of checked nodes
        function checkByNodeIds(nodes, targetNodes) {
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
                    checkByNodeIds(nodes[i].children.data(), targetNodes);
                }
            }
        };

        function onShowHelp() {
            var wnd = $("#permissionManagementHelpWindow").kendoWindow({
                title: "Yardım",
                modal: true,
                visible: false,
                resizable: false,
                width: 500
            }).data("kendoWindow");

            wnd.center().open();
        };

        //on click of the checkbox:
        function selectRow() {
            var checked = this.checked,
                row = $(this).closest("tr");
            if (checked) {
                //-select the row
                row.addClass("k-state-selected");
            } else {
                //-remove selection
                row.removeClass("k-state-selected");
            }
        };

        //on dataBound event restore previous selected rows:
        function checkRows(checkedServiceOids) {
            var grid = $("#gridServices").data("kendoGrid");
            var gridTbody = grid.tbody;
            var view = grid.dataSource.view();
            for (var i = 0; i < view.length; i++) {
                var row = gridTbody.find("tr[data-uid='" + view[i].uid + "']");
                if (checkedServiceOids.indexOf(view[i].id) >= 0) {
                    row.find("input[type=checkbox]").prop("checked", true);
                    row.addClass("k-state-selected");
                } else {
                    row.find("input[type=checkbox]").prop("checked", false);
                    row.removeClass("k-state-selected");
                }
            }
        };

        function getCheckedRows() {
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

    };

    return PermissionManagement;
});