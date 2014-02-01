function initializeUserManagement() {

    $("#gridUsers").kendoGrid({
        dataSource: UserDataSource,
        sortable: true,
        resizable: true,
        pageable: {
            refresh: true
        },
        toolbar: [{
            name: "create",
            text: "Yeni Kullanıcı"
        }],
        columns: [{
            field: "name",
            title: "Ad"

        }, {
            field: "surname",
            title: "Soyad"
        }, {
            field: "email",
            title: "E-posta"
        }, {
            field: "password",
            title: "Şifre",
            hidden: true

        }, {
            field: "roleOid",
            title: "Rol",
            editor: userRoleDropDownEditor,
            hidden: true
        },  {
            field: "active",
            title: "Aktif mi?",
            template: "#= (active)? 'Evet':'Hayır'#"
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
            title: "&nbsp;",
            width: "130px"
        }],
        editable: {
            mode: "popup",
            window: {
                title: "Kayıt"
            }
        }
    });

    function userRoleDropDownEditor(container, options) {
        $('<input required  data-text-field="name" data-value-field="oid"  data-bind="value:' + options.field + '"/>')
            .appendTo(container)
            .kendoDropDownList({
                autoBind: false,
                dataTextField: "name",
                dataValueField: "oid",
                text: "Seçiniz...",
                dataSource: RoleDataSource,
                placeholder: "Seçiniz...",
                index: -1
            });
    }

}