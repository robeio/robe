var MenuHierarchicalDataSource = new kendo.data.HierarchicalDataSource({
    transport: {
        read: {
            type: "GET",
            url: getBackendURL() + "menu/roots",
            dataType: "json",
            contentType: "application/json"
        },

    },
    schema: MenuTreeModel
    
});