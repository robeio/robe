//@ sourceURL=RoleManagement.js
var RoleManagementView;
define([
    'text!html/RoleManagement.html',
    'admin/data/DataSources',

    'kendo/kendo.grid.min',
    'kendo/kendo.window.min',
    'kendo/kendo.listview.min',
    'kendo/kendo.tabstrip.min',
    'robe/view/Page'
], function (view) {

    RoleManagementView = new RobeView("RoleManagementView", view, "container");

    RoleManagementView.render = function () {
        $('#container').append(view);
        RoleManagementView.initialize();
    };

    RoleManagementView.initialize = function () {
        var selectedGroup = null;

        $("#gridRoles").kendoGrid({
            dataSource: RoleDataSource.get(),
            sortable: true,
            toolbar: [
                {
                    name: "create",
                    text: "Yeni Rol"
                }
            ],
            columns: [
                {
                    field: "name",
                    title: "Ad"
                },
                {
                    field: "code",
                    title: "Kod"
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

        $("#listAllRoles").kendoListView({
            dataSource: RoleDataSource.get(),
            template: "<div class='tags k-block'>#:name#</div>",
            selectable: "single",
            change: onListChange
        });

        $("#listGroupedRoles").kendoListView({
            dataSource: GroupedRoleDataSource.get(),
            template: "<div class='tags move  k-block'>#:name#</div><a href='javascript:' class='tagitemcls' onclick=\"removeItem(this,'#:uid#')\"><span class='k-icon k-i-close'></span></a>",
        });

        $("#listUnGroupedRoles").kendoListView({
            dataSource: UnGroupedRoleDataSource.get(),
            template: "<div class='tags move k-block'>#:name#</div>"
        });

        $("#listUnGroupedRoles").kendoDraggable({
            filter: ".move",
            hint: function (element) {
                return element.clone();
            }
        });

        $("#listGroupedRoles").kendoDropTarget({
            dragenter: function (e) {
                e.draggable.hint.css("opacity", 0.6);
            },
            dragleave: function (e) {
                e.draggable.hint.css("opacity", 1);
            },
            drop: function (e) {

                var data = RoleDataSource.get().view();
                var groupOid = selectedGroup;

                var item = UnGroupedRoleDataSource.get().getByUid(e.draggable.hint.data().uid);
                if (groupOid === item.oid) {
                    return;
                }
                else {
                    $.ajax({
                        type: "PUT",
                        url: AdminApp.getBackendURL() + "role/group/" + groupOid + "/" + item.oid,
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        success: function () {
                            GroupedRoleDataSource.get().add(item);
                            UnGroupedRoleDataSource.get().remove(item);
                        }
                    });
                }
            }
        });

        $("#btnRoleManagementHelp").kendoButton({
            click: this.onShowHelp
        });

        function onListChange(e) {
            var data = RoleDataSource.get().view(), selected = $.map(this.select(), function (item) {
                selectedGroup = data[$(item).index()].oid;
                return selectedGroup;
            });

            $.ajax({
                type: "GET",
                url: AdminApp.getBackendURL() + "role/" + selected,
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function (response) {
                    GroupedRoleDataSource.get().data(response.roles);
                    var oids = [];
                    for (var i = 0; i < response.roles.length; i++) {
                        oids.push(response.roles[i].oid);
                    }
                    oids.push(selectedGroup);
                    var unGrouped = RoleDataSource.get().data().filter(function (elem) {
                        return oids.indexOf(elem.oid) === -1;
                    });
                    UnGroupedRoleDataSource.get().data(unGrouped);
                }
            });
        };

        function onShowHelp() {
            wnd = $("#roleManagementHelpWindow").kendoWindow({
                title: "Yardım",
                modal: true,
                visible: false,
                resizable: false,
                width: 500
            }).data("kendoWindow");
            wnd.center().open();

        };

        function removeItem(e, id) {
            var item = GroupedRoleDataSource.get().getByUid(id);

            $.ajax({
                type: "DELETE",
                url: AdminApp.getBackendURL() + "role/destroyRoleGroup/" + selectedGroup + "/" + item.oid,
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function (response) {
                    UnGroupedRoleDataSource.get().add(item);
                    GroupedRoleDataSource.get().remove(item);
                    $(e).parent().remove();
                },
                error: function (e) {
                    $.pnotify({
                        text: "Bir hata oluştu",
                        type: 'error'
                    });
                }
            });
        }

        $("#tabstrip").kendoTabStrip();
    };

    return RoleManagementView;
});

