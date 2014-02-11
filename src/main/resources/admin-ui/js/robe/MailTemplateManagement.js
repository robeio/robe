//@ sourceURL=MailTemplateManagement.js

function initializeMailTemplateManagement() {
    var exampleTemplate =
        "<p>Sayın $name $username,</p>" +
            " <p>Kısa süre önce bir şifre sıfırlama isteği aldık. Şifrenizi sıfırlamak istiyorsanız <a>buradan</a> " +
            "işleminizi gerçekleştirebilirsiniz.</p>" +
            "<p>Eğer isteğin sizin tarafınızdan gönderilmediğini düşünüyorsanız, lütfen bunu bize aşağıdaki linkten" + "bildiriniz.</p>" +
            "<p><a>BildirimLinki</a></p>";


    $("#editor").kendoEditor({
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

    $("#btnMailTemplateManagementHelp").kendoButton({
        click: onBtnMailTemplateManagementHelp
    });

    function onBtnMailTemplateManagementHelp() {
        $().toastmessage('showToast', {
            text: 'YARDIM EKLENECEK ;)',
            sticky: false,
            type: 'success',
            position: 'middle-right'
        });
    }

    $("#btnMailTemplateManagementSave").kendoButton({
        click: onBtnMailTemplateManagementSave
    });

    function onBtnMailTemplateManagementSave() {
        if ($("#cmbLanguage").val() === "" || $("#cmbLanguage").val() === null) {
            $.pnotify({
                text: "Lütfen öncelikle template diliniz seçiniz.",
                type: 'error'
            });
            event.preventDefault();
        } else if ($("#editor").val() === "" || $("#editor").val() === null) {
            $.pnotify({
                text: "Kaydetmeden önce bir template giriniz.",
                type: 'error'
            });
            event.preventDefault();
        } else {
            var mailTemplateData = {};
            var tLang = $("#cmbLanguage").val();
            var template = $("#editor").val();
            mailTemplateData["tLang"] = tLang;
            mailTemplateData["template"] = template;
            var data = JSON.stringify(mailTemplateData);

            $.ajax({
                type: "PUT",
                url: getBackendURL() + "mailtemplate",
                dataType: "json",
                data: data,
                contentType: "application/json; charset=utf-8",
                success: function () {
                    $().toastmessage('showToast', {
                        text: 'Template başarı ile eklendi.',
                        sticky: false,
                        type: 'success',
                        position: 'top-right'
                    });
                }
            });
        }
    }

    var data = [
        { text: "TR", value: "tr" },
        { text: "EN", value: "en" }
    ];

    $("#cmbLanguage").kendoDropDownList({
        optionLabel: "Dil seçiniz...",
        dataTextField: "text",
        dataValueField: "value",
        dataSource: data,
        index: 0
    });


}
