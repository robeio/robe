//@ sourceURL = AdminUIApp.js
var admin = admin || {};

AdminApp = new robe.App("Robe Admin", {
    models: ["./js/admin/Models.js"],
    datasources: [
        "./js/admin/data/SingletonDataSource.js",
        "./js/admin/data/SingletonHierarchicalDataSource.js",
        "./js/admin/data/HierarchicalDataSources.js",
        "./js/admin/data/DataSources.js"],
    views: [
        "./js/admin/view/Workspace.js",
        "./js/admin/view/Login.js",
        "./js/admin/view/UserManagement.js",
        "./js/admin/view/RoleManagement.js",
        "./js/admin/view/MenuManagement.js",
        "./js/admin/view/PermissionManagement.js",
        "./js/admin/view/Dashboard.js",
        "./js/admin/view/ProfileManagement.js",
        "./js/admin/view/MailTemplateManagement.js"
    ]
});
AdminApp.ready = function () {
    Workspace.setContainerId("body");
    Workspace.show();
};

AdminApp.initialize();
