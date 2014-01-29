function initializeRoleManagement() {
    var dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: getBackendURL()+"role/all",
                dataType: "json",
                contentType: "application/json"
            },
            update: {
                type: "POST",
                url: getBackendURL()+"role",
                dataType: "json",
                contentType: "application/json"
            },
            destroy: {
                type: "DELETE",
                url: getBackendURL()+"role",
                dataType: "json",
                contentType: "application/json"
            },
            create: {
                type: "PUT",
                url: getBackendURL()+"role",
                dataType: "json",
                contentType: "application/json"
            },
            parameterMap: function(options, operation) {
                if (operation !== "read") {
                    return kendo.stringify(options);
                }
            }
        },
        batch: false,
        pageSize: 20,
        schema: {
            model: Role
        }
    });

    $("#gridRoles").kendoGrid({
        dataSource: dataSource,
        pageable: true,
        // height: 430,
        toolbar: [{name:"create",text:"Ekle"}],
        columns: [{
            field: "name",
            title: "Ad"
        }, {
            field: "code",
            title: "Kod"
        }, {
            command: [{name:"edit",text:""},{name: "destroy",text:""}],
            title: "&nbsp;",
            width: "100px"
        }],
        editable: "popup"
    });

}