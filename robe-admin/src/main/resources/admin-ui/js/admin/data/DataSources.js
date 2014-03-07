//@ sourceURL=Datasources.js
var UserDataSource, RoleDataSource, GroupedRoleDataSource, UnGroupedRoleDataSource, MenuDataSource, ServiceDataSource, MailManagementDataSource, SystemLanguageDatasource, QuartzJobDataSource;
define([
    'admin/data/SingletonDataSource','admin/data/HierarchicalDataSources', 'admin/Models'], function (S,HDS) {
    console.log("Loading : Datasources");
    UserDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
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


    RoleDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
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

    GroupedRoleDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
        name: "GroupedRoleDataSource",
        parameters: {
            data: [],
            schema: {
                model: RoleModel
            }
        }
    });
    UnGroupedRoleDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
        name: "UnGroupedRoleDataSource",
        parameters: {
            data: [],
            schema: {
                model: RoleModel
            }
        }
    });

    MenuDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
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
                MenuHierarchicalDataSource.read();
            },
            batch: false,
            pageSize: 20,
            schema: {
                model: MenuModel
            }
        }
    });

    ServiceDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
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

    MailManagementDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
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

    SystemLanguageDatasource = robe.util.inherit(admin.data.SingletonDataSource, {
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

    QuartzJobDataSource = robe.util.inherit(admin.data.SingletonDataSource, {
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
    console.log("Finished : Datasources");

});


