//@ sourceURL=Datasources.js
var UserDataSource, RoleDataSource, GroupedRoleDataSource, UnGroupedRoleDataSource, MenuDataSource, ServiceDataSource, MailManagementDataSource, SystemLanguageDatasource, QuartzJobDataSource;
define([
    'admin/data/SingletonDataSource', 'admin/Models'], function (S, HDS) {
    console.log("Loading : Datasources");

    UserDataSource = new SingletonDataSource();
    UserDataSource.setParameters({
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
    UserDataSource.setName("UserDataSource");
    UserDataSource.initialize();

    RoleDataSource = new SingletonDataSource();
    RoleDataSource.setParameters({
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
    RoleDataSource.setName("RoleDataSource");
    RoleDataSource.initialize();

    GroupedRoleDataSource = new SingletonDataSource();
    GroupedRoleDataSource.setParameters({
        data: [],
        schema: {
            model: RoleModel
        }
    });
    GroupedRoleDataSource.setName("GroupedRoleDataSource");
    GroupedRoleDataSource.initialize();

    UnGroupedRoleDataSource = new SingletonDataSource();
    UnGroupedRoleDataSource.setParameters({
        data: [],
        schema: {
            model: RoleModel
        }
    });
    UnGroupedRoleDataSource.setName("UnGroupedRoleDataSource");
    UnGroupedRoleDataSource.initialize();

    MenuDataSource = new SingletonDataSource();
    MenuDataSource.setParameters({
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
    MenuDataSource.setName("MenuDataSource");
    MenuDataSource.initialize();

    ServiceDataSource = new SingletonDataSource();
    ServiceDataSource.setParameters({
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
    ServiceDataSource.setName("ServiceDataSource");
    ServiceDataSource.initialize();

    MailManagementDataSource = new SingletonDataSource();
    MailManagementDataSource.setParameters({
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
    MailManagementDataSource.setName("MailManagementDataSource");
    MailManagementDataSource.initialize();

    SystemLanguageDatasource = new SingletonDataSource();
    SystemLanguageDatasource.setParameters({
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
    SystemLanguageDatasource.setName("SystemLanguageDatasource");
    SystemLanguageDatasource.initialize();

    QuartzJobDataSource = new SingletonDataSource();
    QuartzJobDataSource.setParameters({
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
    QuartzJobDataSource.setName("QuartzJobDataSource");
    QuartzJobDataSource.initialize();

    console.log("Finished : Datasources");
});


