//@ sourceURL = AdminUIApp.js
var admin = admin || {};

AdminApp = robe.util.inherit(robe.App, {
    name: "Robe Admin",
    parameters: {
        models: ["./js/admin/Models.js"],
        datasources: [
            {
                1: "./js/admin/data/SingletonDataSource.js",
                2: "./js/admin/data/SingletonHierarchicalDataSource.js"
            },
            "./js/admin/data/HierarchicalDataSources.js",
            "./js/admin/data/DataSources.js"
        ],
        views: [
            "./js/admin/view/Workspace.js",
            {
                1: "./js/admin/view/Login.js",
                2: "./js/admin/view/UserManagement.js",
                3: "./js/admin/view/RoleManagement.js",
                4: "./js/admin/view/MenuManagement.js",
                5: "./js/admin/view/PermissionManagement.js",
                6: "./js/admin/view/Dashboard.js",
                7: "./js/admin/view/ProfileManagement.js",
                8: "./js/admin/view/MailTemplateManagement.js"
            }
        ]
    }
});
AdminApp.ready = function () {
    Workspace.setContainerId("body");
    Workspace.show();
};

AdminApp.initialize();


