//@ sourceURL=HierarchicalDataSources.js
var MenuHierarchicalDataSource;
define(['kendo/kendo.data.min','admin/Models'], function () {
    console.log("Loading : HierarchicalDataSources");
    MenuHierarchicalDataSource = new kendo.data.HierarchicalDataSource("MenuHierarchicalDataSource", {
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
    console.log("Finished : HierarchicalDataSources");
});
