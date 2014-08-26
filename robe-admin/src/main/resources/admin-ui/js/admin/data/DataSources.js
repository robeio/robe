var UserDataSource, RoleDataSource, GroupedRoleDataSource, UnGroupedRoleDataSource, MenuDataSource, ServiceDataSource, MailManagementDataSource, SystemLanguageDatasource, QuartzJobDataSource, TriggerDataSource;
define([
    'admin/data/SingletonDataSource', 'admin/Models'], function (S, HDS) {
    console.log("Loading : Datasources");

    UserDataSource = new SingletonDataSource("UserDataSource", {
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
    });

    RoleDataSource = new SingletonDataSource("RoleDataSource", {
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
    });

    GroupedRoleDataSource = new SingletonDataSource("GroupedRoleDataSource", {
        data: [],
        schema: {
            model: RoleModel
        }
    });

    UnGroupedRoleDataSource = new SingletonDataSource("UnGroupedRoleDataSource", {
        data: [],
        schema: {
            model: RoleModel
        }
    });

    MenuDataSource = new SingletonDataSource("MenuDataSource", {
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
        batch: false,
        pageSize: 20,
        schema: {
            model: MenuModel
        }
    });

    ServiceDataSource = new SingletonDataSource("ServiceDataSource", {
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
    });

    MailManagementDataSource = new SingletonDataSource("MailManagementDataSource", {
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
    });

    SystemLanguageDatasource = new SingletonDataSource("SystemLanguageDatasource", {
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
    });

    QuartzJobDataSource = new SingletonDataSource("QuartzJobDataSource", {
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
    });

//
//    TriggerDataSource = new SingletonDataSource("TriggerDataSource", {
//        transport: {
//            read: {
//                type: "GET",
//                url: AdminApp.getBackendURL() + "trigger",
//                dataType: "json",
//                contentType: "application/json"
//            },
//            update: {
//                type: "POST",
//                url: AdminApp.getBackendURL() + "trigger/update",
//                dataType: "json",
//                contentType: "application/json"
//            },
//            create: {
//                type: "PUT",
//                url: AdminApp.getBackendURL() + "trigger",
//                dataType:"json",
//                contentType: "application/json"
//            },
//            parameterMap: function (options, operation) {
//                if (operation !== "read") {
//                    return kendo.stringify(options);
//                }
//            }
//        },
//        batch: false,
//        schema: {
//            model: TriggerModel
//        },
//        serverPaging: true,
//        serverSorting: true,
//        serverFiltering: true,
//        pageSize: 5,
//        filter: { field: "jobId", operator: "eq", value: e.data.oid }
//    });

    console.log("Finished : Datasources");
});


