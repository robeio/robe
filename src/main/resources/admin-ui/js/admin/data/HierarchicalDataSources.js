//@ sourceURL=HierarchicalDataSources.js
var MenuHierarchicalDataSource = new admin.data.SingletonHierarchicalDataSource("MenuHierarchicalDataSource",{
    transport: {
        read: {
            type: "GET",
            url: AdminApp.getBackendURL() + "menu/roots",
            dataType: "json",
            contentType: "application/json"
        }

    },
    schema: MenuTreeModel
});
