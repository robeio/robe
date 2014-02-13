//@ sourceURL=Datasources.js
var UserDataSource = new robe.data.SingletonDataSource("UserDataSource",{
    transport: {
        read: {
            type: "GET",
            url: getBackendURL() + "user/all",
            dataType: "json",
            contentType: "application/json"
        },
        update: {
            type: "POST",
            url: getBackendURL() + "user",
            dataType: "json",
            contentType: "application/json"
        },
        destroy: {
            type: "DELETE",
            url: getBackendURL() + "user",
            dataType: "json",
            contentType: "application/json"
        },
        create: {
            type: "PUT",
            url: getBackendURL() + "user",
            dataType: "json",
            contentType: "application/json"
        },
        parameterMap: function (options, operation) {
            if (operation !== "read") {
                return kendo.stringify(options);
            }
        }
    },
    batch: false,
    pageSize: 20,
    schema: {
        model: UserModel
    }
});


var RoleDataSource = new robe.data.SingletonDataSource("RoleDataSource",{
    transport: {
        read: {
            type: "GET",
            url: getBackendURL() + "role/all",
            dataType: "json",
            contentType: "application/json"
        },
        update: {
            type: "POST",
            url: getBackendURL() + "role",
            dataType: "json",
            contentType: "application/json"
        },
        destroy: {
            type: "DELETE",
            url: getBackendURL() + "role",
            dataType: "json",
            contentType: "application/json"
        },
        create: {
            type: "PUT",
            url: getBackendURL() + "role",
            dataType: "json",
            contentType: "application/json"
        },
        parameterMap: function (options, operation) {
            if (operation !== "read") {
                return kendo.stringify(options);
            }
        }
    },
    batch: false,
    pageSize: 20,
    schema: {
        model: RoleModel
    }
});

var GroupedRoleDataSource = new robe.data.SingletonDataSource("GroupedRoleDataSource",{
    data: [],
    schema: {
        model: RoleModel
    }
});
var UnGroupedRoleDataSource = new robe.data.SingletonDataSource("UnGroupedRoleDataSource",{
    data: [],
    schema: {
        model: RoleModel
    }
});

var MenuDataSource = new robe.data.SingletonDataSource("MenuDataSource",{
    transport: {
        read: {
            type: "GET",
            url: getBackendURL() + "menu/all",
            dataType: "json",
            contentType: "application/json"
        },
        update: {
            type: "POST",
            url: getBackendURL() + "menu",
            dataType: "json",
            contentType: "application/json"
        },
        destroy: {
            type: "DELETE",
            url: getBackendURL() + "menu",
            dataType: "json",
            contentType: "application/json"
        },
        create: {
            type: "PUT",
            url: getBackendURL() + "menu",
            dataType: "json",
            contentType: "application/json"
        },
        parameterMap: function (options, operation) {
            if (operation !== "read") {
                return kendo.stringify(options);
            }
        }
    },
    change: function (e) {
        MenuHierarchicalDataSource.get();
    },
    batch: false,
    pageSize: 20,
    schema: {
        model: MenuModel
    }
});

var ServiceDataSource = new robe.data.SingletonDataSource("ServiceDataSource",{
    transport: {
        read: {
            type: "GET",
            url: getBackendURL() + "service/all",
            dataType: "json",
            contentType: "application/json"
        }
    },
    batch: false,
    schema: {
        model: ServiceModel
    }
});

/*  DATASOURCE AJAX REQUEST CONTROL */
