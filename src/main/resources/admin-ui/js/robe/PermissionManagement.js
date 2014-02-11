//@ sourceURL=PermissionManagement.js

function initializePermissionManagement() {

    $("#horizontalTabStrips").kendoSplitter({
        //TODO Attributes
        panes: [
            { collapsible: false, size: "20%", resizable: false },
            { collapsible: false, size: "20%", resizable: false  },
            { collapsible: false, size: "60%", scrollable: true}
        ]
    });

    $("#gridServices").kendoGrid({
        dataSource: ServiceDataSource,
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

    $("#gridServices").data("kendoGrid").table.on("click", ".checkbox", selectRow);


    $("#cmbRoles").kendoDropDownList({
        dataTextField: "name",
        dataValueField: "oid",
        dataSource: RoleDataSource,
        change: onCmbRolesChange,
        autoBind: false,
        text: "Seçiniz...",
        index: -1
    });


    $("#treeMenus").kendoTreeView({
        checkboxes: {
            checkChildren: true
        },
        dataSource: MenuHierarchicalDataSource,
        dataTextField: "name"
    });

    $("#btnSavePermission").kendoButton({
        click: onSave
    });


    $("#btnMenuManagementHelp").kendoButton({
        click: onShowHelp
    });
}

function onShowHelp() {
    wnd = $("#permissionManagementHelpWindow").kendoWindow({
        title: "Yardım",
        modal: true,
        visible: false,
        resizable: false,
        width: 500
    }).data("kendoWindow");

    wnd.center().open();

};

function onCmbRolesChange() {
    var roleOid = $("#cmbRoles").val();
    $.ajax({
        type: "GET",
        url: getBackendURL() + "permission/" + roleOid + "/menu",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            $("#treeMenus").data("kendoTreeView").expand(".k-item");
            checkByNodeIds($("#treeMenus").data("kendoTreeView").dataSource.data(), response);
        }
    });
    $.ajax({
        type: "GET",
        url: getBackendURL() + "permission/" + roleOid + "/service",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            checkRows(response);
        }
    });
}

function onSave() {
    var roleOid = $("#cmbRoles").val();
    var checkedNodes = [];
    var treeMenus = $("#treeMenus").data("kendoTreeView");

    checkedNodeIds(treeMenus.dataSource.view(), checkedNodes);
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

}

// function that gathers IDs of checked nodes
function checkedNodeIds(nodes, checkedNodes) {
    for (var i = 0; i < nodes.length; i++) {
        if (nodes[i].checked) {
            checkedNodes.push(nodes[i].id);
        }

        if (nodes[i].hasChildren) {
            checkedNodeIds(nodes[i].children.view(), checkedNodes);
        }
    }
}
// function that gathers IDs of checked nodes
function checkByNodeIds(nodes, targetNodes) {
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
            checkByNodeIds(nodes[i].children.data(), targetNodes);
        }
    }
}

//on click of the checkbox:
function selectRow() {
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
}

//on dataBound event restore previous selected rows:
function checkRows(checkedServiceOids) {
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
}

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