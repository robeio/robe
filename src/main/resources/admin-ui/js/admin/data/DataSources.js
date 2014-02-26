//@ sourceURL=Datasources.js
var UserDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
    name: "UserDataSource",
    parameters: {
        transport: {
            read: {
                type: "GET",
                url: AdminApp.getBackendURL() + "user/all",
                dataType: "json",
                contentType: "application/json"
            },
            update: {
                type: "POST",
                url: AdminApp.getBackendURL() + "user",
                dataType: "json",
                contentType: "application/json"
            },
            destroy: {
                type: "DELETE",
                url: AdminApp.getBackendURL() + "user",
                dataType: "json",
                contentType: "application/json"
            },
            create: {
                type: "PUT",
                url: AdminApp.getBackendURL() + "user",
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
    }
});


var RoleDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
    name: "RoleDataSource",
    parameters: {
        transport: {
            read: {
                type: "GET",
                url: AdminApp.getBackendURL() + "role/all",
                dataType: "json",
                contentType: "application/json"
            },
            update: {
                type: "POST",
                url: AdminApp.getBackendURL() + "role",
                dataType: "json",
                contentType: "application/json"
            },
            destroy: {
                type: "DELETE",
                url: AdminApp.getBackendURL() + "role",
                dataType: "json",
                contentType: "application/json"
            },
            create: {
                type: "PUT",
                url: AdminApp.getBackendURL() + "role",
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
    }
});

var GroupedRoleDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
    name: "GroupedRoleDataSource",
    parameters: {
        data: [],
        schema: {
            model: RoleModel
        }
    }
});
var UnGroupedRoleDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
    name: "UnGroupedRoleDataSource",
    parameters: {
        data: [],
        schema: {
            model: RoleModel
        }
    }
});

var MenuDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
    name: "MenuDataSource",
    parameters: {
        transport: {
            read: {
                type: "GET",
                url: AdminApp.getBackendURL() + "menu/all",
                dataType: "json",
                contentType: "application/json"
            },
            update: {
                type: "POST",
                url: AdminApp.getBackendURL() + "menu",
                dataType: "json",
                contentType: "application/json"
            },
            destroy: {
                type: "DELETE",
                url: AdminApp.getBackendURL() + "menu",
                dataType: "json",
                contentType: "application/json"
            },
            create: {
                type: "PUT",
                url: AdminApp.getBackendURL() + "menu",
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
    }
});

var ServiceDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
    name: "ServiceDataSource",
    parameters: {
        transport: {
            read: {
                type: "GET",
                url: AdminApp.getBackendURL() + "service/all",
                dataType: "json",
                contentType: "application/json"
            }
        },
        batch: false,
        schema: {
            model: ServiceModel
        }
    }
});

var MailManagementDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
    name: "MailManagementDataSource",
    parameters: {
        transport: {
            read: {
                type: "GET",
                url: AdminApp.getBackendURL() + "mailtemplate/all",
                dataType: "json",
                contentType: "application/json"
            },
            create: {
                type: "PUT",
                url: AdminApp.getBackendURL() + "mailtemplate",
                dataType: "json",
                contentType: "application/json"
            },
            update: {
                type: "POST",
                url: AdminApp.getBackendURL() + "mailtemplate",
                dataType: "json",
                contentType: "application/json"
            },
            destroy: {
                type: "DELETE",
                url: AdminApp.getBackendURL() + "mailtemplate",
                dataType: "json",
                contentType: "application/json"
            }
        },
        parameterMap: function (options, operation) {
            if (operation !== "read") {
                return JSON.stringify(options);
            }
        },
        batch: false,
        schema: {
            model: MailManagementModel
        }
    }
});

var SystemLanguageDatasource = robe.util.inherit(admin.data.SingletonDataSource, {
    name: "SystemLanguageDatasource",
    parameters: {
        transport: {
            read: {
                type: "GET",
                url: AdminApp.getBackendURL() + "language/all",
                dataType: "json",
                contentType: "application/json"
            }
        },
        batch: false,
        schema: {
            model: SystemLanguageModel
        }
    }
});

var QuartzJobDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
    name: "QuartzJobDataSource",
    parameters: {
        transport: {
            read: {
                type: "GET",
                url: AdminApp.getBackendURL() + "quartzJob",
                dataType: "json",
                contentType: "application/json"
            },
             update: {
                 type: "POST",
                 url: AdminApp.getBackendURL() + "quartzJob/update",
                 dataType: "json",
                 contentType: "application/json"
             },
             create: {
                 type: "POST",
                 url: AdminApp.getBackendURL() + "quartzJob/fire",
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
        schema: {
                model: QuartzJobModel
        }
    }
});


