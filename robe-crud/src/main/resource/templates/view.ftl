define([
    'text!./${entity}Management.html',
    './${entity}DataSource',
    'kendo/kendo.grid.min',
    'kendo/kendo.window.min',
    'robe/view/RobeView'
], function (html, ${entity}DataSource) {
    var ${entity}ManagementView = require('robe/view/RobeView').define({
        name: "${entity}ManagementView",
        html: html,
        containerId: "container",
        initialize: function () {
            $("#grid${entity}").kendoGrid({
                dataSource: ${entity}DataSource.get(),
                sortable: true,
                pageable: {
                refresh: true
            },
            toolbar: [
                {
                    name: "create",
                    text: "Yeni Ekle"
                }
            ],
                columns: [
            <#list fields as field>
            {
                field : "${field.name}",
                title : "${field.name}"
            },
            </#list>
            {
                command: [
                    {
                        name: "edit",
                        text: {
                            edit: "",
                            update: "Update",
                            cancel: "Cancel"
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
                    title: "Save"
                },
                confirmation: "Are you sure you want to delete?",
                    confirmDelete: "Yes"
            }
        });

        }
    });

    return ${entity}ManagementView;
});