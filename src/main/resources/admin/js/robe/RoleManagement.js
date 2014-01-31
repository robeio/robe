function initializeRoleManagement() {
    var dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: getBackendURL() + "role/all",
                dataType: "json",
                contentType: "application/json"
            },
            update: {
                type: "POST",
                url: getBackendURL() + "role",
                dataType: "json",
                contentType: "application/json"
            },
            destroy: {
                type: "DELETE",
                url: getBackendURL() + "role",
                dataType: "json",
                contentType: "application/json"
            },
            create: {
                type: "PUT",
                url: getBackendURL() + "role",
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
        sortable: true,
        resizable: true,
        pageable: {
            refresh: true
        },
        toolbar: [{
            name: "create",
            text: "Yeni Rol"
        }],
        columns: [{
            field: "name",
            title: "Ad"
        }, {
            field: "code",
            title: "Kod"
        }, {
            command: [{
                name: "edit",
                text: {
                    edit: "",
                    update: "Güncelle",
                    cancel: "İptal"
                }
            }, {
                name: "destroy",
                text: ""
            }],
            title: "<b>İşlemler</b>",
            width: "130px"
        }],
        editable: {
            mode: "popup",
            window: {
                title: "Kayıt"
            }
        }
    });

}