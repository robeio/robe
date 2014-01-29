function initializePermissionManagement() {
  var cmbDataSource = new kendo.data.DataSource({
    transport: {
      read: {
        type: "GET",
        url: getBackendURL() + "role/all",
        dataType: "json",
        contentType: "application/json"
      }
    },
    schema: {
      model: {
        id: "oid",
        fields: {
          oid: {
            editable: false,
            nullable: false
          },
          code: {
            editable: true,
            nullable: false
          }
        }
      }
    },
  });
  cmbDataSource.read();

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
        url: getBackendURL() + "menu/roots",
        dataType: "json",
        contentType: "application/json"
      }
    },
    schema: treeModel
  });

  treeDataSource.read();

  var gridDataSource = new kendo.data.DataSource({
    transport: {
      read: {
        type: "GET",
        url: getBackendURL() + "service/all",
        dataType: "json",
        contentType: "application/json"
      },

    },
    batch: false,
    schema: {
      model: Service
    }
  });
  $("#gridServices").kendoGrid({
    dataSource: gridDataSource,
    width: 230,
    columns: [{
      template: "<input type='checkbox' class='checkbox' />",
      field: "selected",
      title: "&nbsp;"
    }, {
      field: "path",
      title: "Servis"
    }, {
      field: "method",
      title: "Method"
    }],
  });

  $("#gridServices").data("kendoGrid").table.on("click", ".checkbox", selectRow);


  $("#cmbRoles").kendoDropDownList({
    dataTextField: "name",
    dataValueField: "oid",
    dataSource: cmbDataSource,
    change: onCmbRolesChange,
    autoBind: false,
    text: "Seçiniz...",
    index: -1
  });



  $("#treeMenus").kendoTreeView({
    checkboxes: {
      checkChildren: true
    },
    dataSource: treeDataSource,
    dataTextField: "name"
  });

  $("#btnSavePermission").kendoButton({
    click: onSave
  });

}


function onCmbRolesChange() {
  var roleOid = $("#cmbRoles").val();
  $.ajax({
    type: "GET",
    url: getBackendURL() + "permission/" + roleOid + "/menu",
    dataType: "json",
    contentType: "application/json; charset=utf-8",
    success: function(response) {
      $("#treeMenus").data("kendoTreeView").expand(".k-item");
      checkByNodeIds($("#treeMenus").data("kendoTreeView").dataSource.data(), response);
    }
  });
  $.ajax({
    type: "GET",
    url: getBackendURL() + "permission/" + roleOid + "/service",
    dataType: "json",
    contentType: "application/json; charset=utf-8",
    success: function(response) {
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
    success: function() {}
  });

  

  $.ajax({
    type: "PUT",
    url: getBackendURL() + "permission/" + roleOid + "/service",
    dataType: "json",
    data: kendo.stringify(getCheckedRows()),
    contentType: "application/json; charset=utf-8",
    success: function(result) {
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
    tree.findByUid(nodeUid).find(":checkbox").prop("checked",isChecked);
	nodes[i].set("checked",isChecked);
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
    if (checkedServiceOids.indexOf(view[i].id)>=0) {
      gridTbody.find("tr[data-uid='" + view[i].uid + "']")
        .addClass("k-state-selected")
        .find(".checkbox")
        .attr("checked", "checked");
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
    if (className!=null && className.indexOf("k-state-selected")>=0) {
      checkedServiceOids.push(view[i].get("oid"))
    }
  }
  return checkedServiceOids;
}