var UserModel = kendo.data.Model.define({
    id: "oid",
    fields: {
        oid: {
            editable: false,
            nullable: true,
            type: "string"
        },
        lastUpdated: {
            editable: true,
            nullable: true,
            type: "string"
        },
        name: {
            editable: true,
            nullable: false,
            type: "string",
            validation: getValidations("name", "Ad", true, false, 2, 50, "[A-Za-z]+")
        },
        surname: {
            editable: true,
            nullable: false,
            type: "string",
            validation: getValidations("surname", "Soyad", true, false, 2, 50, "[A-Za-z]+")
        },
        email: {
            editable: true,
            nullable: false,
            type: "string",
            validation: getValidations("email", "Eposta", true, true, 5, 50)
        },
        active: {
            type: "boolean"
        },
        roleOid: {
            editable: true,
            nullable: false
        },
        role: {}
    }
});

var RoleModel = kendo.data.Model.define({
    id: "oid",
    fields: {
        oid: {
            editable: false,
            nullable: true
        },
        lastUpdated: {
            editable: false,
            nullable: true
        },
        name: {
            editable: true,
            nullable: false,
            validation: getValidations("name", "Ad", true, false, 2, 50, "[A-Za-z]+")
        },
        code: {
            editable: true,
            nullable: false,
            validation: getValidations("code", "Kod", true, false, 2, 20, "[A-Za-z]+")
        }
    }
});

var MenuModel = kendo.data.Model.define({
    id: "oid",
    fields: {
        oid: {
            editable: false,
            nullable: true
        },
        lastUpdated: {
            editable: true,
            nullable: true
        },
        name: {
            editable: true,
            nullable: false,
            validation: getValidations("name", "Ad", true, false, 2, 50, "[A-Za-z]+")
        },
        code: {
            editable: true,
            nullable: false,
            validation: getValidations("code", "Kod", true, false, 2, 50, "[A-Za-z]+")
        }
    }
});

var MenuTreeModel = {
    model: {
        id: "oid",
        fields: {
            oid: {
                editable: false,
                nullable: true
            },
            lastUpdated: {
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
        hasChildren: function (item) {
            return item.items != null;
        }
    }
};


var ServiceModel = kendo.data.Model.define({
    id: "oid",
    fields: {
        oid: {
            editable: false,
            nullable: true
        },
        lastUpdated: {
            editable: false,
            nullable: true
        },
        path: {
            editable: true,
            nullable: true,
            validation: getValidations("code", "Kod", true, false, 1, 100)
        },
        method: {
            editable: true,
            nullable: false,
            validation: getValidations("code", "Kod", true, false, 1, 10, "[A-Z]+")
        }
    }
});