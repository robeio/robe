function initializeRoleManagement() {

    $("#gridRoles").kendoGrid({
        dataSource: RoleDataSource,
        sortable: true,
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