//@ sourceURL=MailTemplateManagement.js
var MailTemplateManagement;

define([
    'text!html/MailTemplateManagement.html',
    'admin/data/DataSources',

    'kendo/kendo.button.min',
    'kendo/kendo.grid.min',
    'kendo/kendo.editor.min',
    'kendo/kendo.dropdownlist.min',
    'robe/AlertDialog',
    'robe/view/Page'
], function (view) {

    MailTemplateManagement = new RobeView("MailTemplateManagement", view, "container");

    MailTemplateManagement.render = function () {
        $('#container').append(view);
        MailTemplateManagement.initialize();
    };

    MailTemplateManagement.initialize = function () {
        $("#templateGrid").kendoGrid({
            dataSource: MailManagementDataSource.get(),
            sortable: true,
            toolbar: [
                {
                    name: "create",
                    text: "Yeni Template",
                    height: 100,
                    width: 100
                }
            ],
            columns: [
                {
                    field: "lang",
                    title: "Dil",
                    editor: userTemplateLanguagePopupEditor

                },
                {
                    field: "code",
                    title: "Kod"
                },
                {
                    field: "template",
                    title: "Template",
                    editor: userTemplatePopupEditor,
                    hidden: true
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
            },
            edit: this.onEdit
        });
        $("#btnMailTemplateManagementHelp").kendoButton({
            click: onBtnMailTemplateManagementHelp
        });

        function onEdit(e) {
            var editWindow = this.editable.element.data("kendoWindow");
            editWindow.wrapper.css({ width: 800 });
        };
        function userTemplatePopupEditor(container, options) {
            var exampleTemplate =
                "<p>Sayın $name $username,</p>" +
                    " <p>Kısa süre önce bir şifre sıfırlama isteği aldık. Şifrenizi sıfırlamak istiyorsanız <a>buradan</a> " +
                    "işleminizi gerçekleştirebilirsiniz.</p>" +
                    "<p>Eğer isteğin sizin tarafınızdan gönderilmediğini düşünüyorsanız, lütfen bunu bize aşağıdaki linkten" + "bildiriniz.</p>" +
                    "<p><a>BildirimLinki</a></p>";
            $('<textarea id="editor" style="width: 600px;" data-bind="value:' + options.field + '"/>')
                .appendTo(container)
                .kendoEditor({
                    tools: [
                        "insertHtml",
                        "bold",
                        "italic",
                        "underline",
                        "strikethrough",
                        "justifyLeft",
                        "justifyCenter",
                        "justifyRight",
                        "justifyFull",
                        "insertUnorderedList",
                        "insertOrderedList",
                        "indent",
                        "outdent",
                        "createLink",
                        "unlink",
                        "insertImage",
                        "subscript",
                        "superscript",
                        "createTable",
                        "addRowAbove",
                        "addRowBelow",
                        "addColumnLeft",
                        "addColumnRight",
                        "deleteRow",
                        "deleteColumn",
                        "viewHtml",
                        "formatting",
                        "fontName",
                        "fontSize",
                        "foreColor",
                        "backColor"
                    ],
                    insertHtml: [
                        {text: "Example Mail", value: exampleTemplate}
                    ]
                });

        };
        function userTemplateLanguagePopupEditor(container, options) {
            $('<input id="cmbLanguage" data-text-field="name" data-value-field="code" class="pull-left" style="width: 600px;" data-bind="value:' + options.field + '"/>')
                .appendTo(container)
                .kendoDropDownList({
                    optionLabel: "Dil seçiniz...",
                    dataTextField: "name",
                    dataValueField: "code",
                    dataSource: SystemLanguageDatasource.get(),
                    index: 0
                });
        };
        function onBtnMailTemplateManagementHelp() {
            var wnd = $("#mailTemplateManagementHelpWindow").kendoWindow({
                title: "Yardım",
                modal: true,
                visible: false,
                resizable: false,
                width: 500
            }).data("kendoWindow");

            wnd.center().open();
        };
    };

    return MailTemplateManagement;
});

