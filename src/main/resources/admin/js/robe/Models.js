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
        name: {
            editable: true,
            nullable: false,
            type: "string",
			validation: getValidations("name","Ad",true,false,2,15)
		},
        surname: {
            editable: true,
            nullable: false,
            type: "string",
            validation: getValidations("surname","Soyad",true,false,2,15)
        },
        email: {
            editable: true,
            nullable: false,
            type: "string",
            validation: getValidations("email","Eposta",true,true)
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
        name: {
            editable: true,
            nullable: false,
            validation: getValidations("name","Ad",true,false,2,50,"[A-Z]+")
        },
        code: {
            editable: true,
            nullable: false,
            validation: getValidations("code","Kod",true,false,2,20,"[A-Z]+")
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
        name: {
            editable: true,
            nullable: false ,
            validation: getValidations("name","Ad",true,false,2,50,"[A-Z]+")
        },
        code: {
            editable: true,
            nullable: false,
            validation: getValidations("code","Kod",true,false,2,50,"[A-Z]+")
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