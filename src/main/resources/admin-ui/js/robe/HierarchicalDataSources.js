//@ sourceURL=HierarchicalDataSources.js
var MenuHierarchicalDataSource = new robe.data.SingletonHierarchicalDataSource("MenuHierarchicalDataSource",{
    transport: {
        read: {
            type: "GET",
            url: getBackendURL() + "menu/roots",
            dataType: "json",
            contentType: "application/json"
        }

    },
    schema: MenuTreeModel
});