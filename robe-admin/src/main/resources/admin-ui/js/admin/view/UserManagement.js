var UserManagementView;
define([
    'text!html/UserManagement.html',
    'admin/data/DataSources',

    'kendo/kendo.grid.min',
    'kendo/kendo.window.min',
    'robe/view/RobeView'
], function (view) {
    UserManagementView = new RobeView("UserManagementView", view, "container");
    UserManagementView.render = function () {
        $('#container').append(view);
        UserManagementView.initialize();
    };

    UserManagementView.initialize = function () {
        $("#gridUsers").kendoGrid({
            dataSource: UserDataSource.get(),
            sortable: true,
            toolbar: [
                {
                    name: "create",
                    text: "Yeni Kullanıcı"
                }
            ],
            columns: [
                {
                    field: "name",
                    title: "Ad"

                },
                {
                    field: "surname",
                    title: "Soyad"
                },
                {
                    field: "email",
                    title: "E-posta"
                },
                {
                    field: "roleOid",
                    title: "Rol",
                    editor: userRoleDropDownEditor,
                    hidden: true
                },
                {
                    field: "active",
                    title: "Aktif mi?",
                    template: "#= (active)? 'Evet':'Hayır'#"
                },
                {
                    command: [
                        {
                            name: "edit",
                            text: {
                                edit: "",
                                update: "Güncelle",
                                cancel: "İptal"
                            },
                            className: "grid-command-iconfix"
                        },
                        {
                            name: "destroy",
                            text: "",
                            className: "grid-command-iconfix"
                        }
                    ],
                    title: "&nbsp;",
                    width: "80px"
                }
            ],
            editable: {
                mode: "popup",
                window: {
                    title: "Kayıt"
                },
                confirmation: "Silmek istediğinizden emin misiniz?",
                confirmDelete: "Yes"
            }
        });

        $("#btnUserManagementHelp").kendoButton({
            click: onShowHelp
        });

        function userRoleDropDownEditor(container, options) {
            $('<input required  data-text-field="name" data-value-field="oid"  data-bind="value:' + options.field + '"/>')
                .appendTo(container)
                .kendoDropDownList({
                    autoBind: false,
                    dataTextField: "name",
                    dataValueField: "oid",
                    text: "Seçiniz...",
                    dataSource: RoleDataSource.get(),
                    placeholder: "Seçiniz...",
                    index: -1
                });
        };

        function onShowHelp() {
            var wnd = $("#userManagementHelpWindow").kendoWindow({
                title: "Yardım",
                modal: true,
                visible: false,
                resizable: false,
                width: 500
            }).data("kendoWindow");

            wnd.center().open();
        };
    };

    return UserManagementView;
});

