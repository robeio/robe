//@ sourceURL=UserManagement.js
define([
    'jquery',
    'underscore',
    'backbone',

    'text!html/UserManagement.html',
    'datasources/DataSources',

    'kendo/kendo.grid.min',
    'kendo/kendo.window.min'

], function ($, _, Backbone, view) {
    var UserManagementView = Backbone.View.extend({
        el: $('#container'),
        render: function () {
            // Append our compiled template to this Views "el"
            this.$el.append(view);
            this.initial();
        },

    initial: function () {

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
                    editor: this.userRoleDropDownEditor,
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
            click: this.onShowHelp
        });


    },
    userRoleDropDownEditor: function (container, options) {
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
    },
    onShowHelp: function () {
        var wnd = $("#userManagementHelpWindow").kendoWindow({
            title: "Yardım",
            modal: true,
            visible: false,
            resizable: false,
            width: 500
        }).data("kendoWindow");

        wnd.center().open();
    }

    });
    return UserManagementView;
});

