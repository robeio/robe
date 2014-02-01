var UserModel = kendo.data.Model.define({
    id: "oid",
    fields: {
        oid: {
            editable: false,
            nullable: false,
            type: "string"
        },
        lastUpdated: {
            editable: true,
            nullable: false,
            type: "string"
        },
        deleted: {
            editable: false,
            nullable: true,
            type: "string"
        },
        name: {
            editable: true,
            nullable: false,
            type: "string"
        },
        surname: {
            editable: true,
            nullable: false,
            type: "string"
        },
        email: {
            editable: true,
            nullable: false,
            type: "string"
        },
        active: {
            type: "boolean"
        },
        roleOid: {
            editable: true,
            nullable: false,
        },
        role: {},
        password: {
            editable: true,
            nullable: false,
            hidden: true,
            type: "string"
        },
        firmOid: {
            editable: true,
            nullable: false,
            type: "string"
        },
        firm: {

        }

    }
});

var RoleModel = kendo.data.Model.define({
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
});

var MenuModel = kendo.data.Model.define({
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
});

var MenuTreeModel = {
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
};


var ServiceModel = kendo.data.Model.define({
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
        path: {
            editable: true,
            nullable: true
        },
        method: {
            editable: true,
            nullable: false
        }
    }
});