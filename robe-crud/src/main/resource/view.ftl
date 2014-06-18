var ${view}View;
define([
    'text!html/${view}.html',
    'admin/data/DataSources',

    'kendo/kendo.grid.min',
    'kendo/kendo.window.min',
    'robe/view/RobeView'
], function (view) {
    ${view}View = new RobeView("${view}View", view, "container");
    ${view}View.render = function () {
        $('#container').append(view);
        ${view}View.initialize();
    };

    ${view}View.initialize = function () {
        $("#grid${view}").kendoGrid({
            dataSource: ${dataSource}.get(),
            sortable: true,
            toolbar: [
                {
                    name: "create",
text: "New"
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

        $("#btn${view}ViewHelp").kendoButton({
            click: onShowHelp
        });

        function onShowHelp() {
            var wnd = $("#${view?uncap_first}HelpWindow").kendoWindow({
                title: "Help",
                modal: true,
                visible: false,
                resizable: false,
                width: 500
            }).data("kendoWindow");

            wnd.center().open();
        };
    };

    return ${view}View;
});

