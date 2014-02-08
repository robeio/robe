    var selectedGroup;

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
                },
                className:"grid-command-iconfix"
            }, {
                name: "destroy",
                text: "",
                className:"grid-command-iconfix"
            }],
            title: "&nbsp;",
            width: "80px"
        }],
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
		dataSource: RoleDataSource,
        template: "<div class='tags k-block'>#:name#</div>",
        selectable:"single",
		change: onListChange
	});
	$("#listGroupedRoles").kendoListView({
    		dataSource: GroupedRoleDataSource,
    		template:"<div class='tags move  k-block'>#:name#</div><a href='javascript:' class='tagitemcls' onclick=\"removeItem(this,'#:uid#')\"><span class='k-icon k-i-close'></span></a>",
    });
    $("#listUnGroupedRoles").kendoListView({
    		dataSource: UnGroupedRoleDataSource,
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

            var data = RoleDataSource.view();
            var groupOid = selectedGroup;

            var item = UnGroupedRoleDataSource.getByUid(e.draggable.hint.data().uid);
            if(groupOid === item.oid) {
                return;
            }
            else {
                $.ajax({
                    type: "PUT",
                    url: getBackendURL() + "role/group/" + groupOid + "/" + item.oid,
                    dataType: "json",
                    contentType: "application/json; charset=utf-8",
                    success: function() {
                        GroupedRoleDataSource.add(item);
                        UnGroupedRoleDataSource.remove(item);
                    }
                });
            }
        }
    });
    $("#btnRoleManagementHelp").kendoButton({
        click: onShowHelp
    });
    function onShowHelp () {
        wnd = $("#roleManagementHelpWindow").kendoWindow({
                title: "Yardım",
                modal: true,
                visible: false,
                resizable: false,
                width: 500
            }).data("kendoWindow");
        wnd.center().open();

    };


	function onListChange (e) {
		var data = RoleDataSource.view(), selected = $.map(this.select(), function(item) {
            selectedGroup = data[$(item).index()].oid;
            return selectedGroup;
	    });

		$.ajax({
			type: "GET",
			url: getBackendURL() + "role/" + selected,
			dataType: "json",
			contentType: "application/json; charset=utf-8",
			success: function(response){
				GroupedRoleDataSource.data(response.roles);
				var oids = [];
				for(var i = 0 ; i<response.roles.length;i++ ){
			    	oids.push(response.roles[i].oid);
				}
				oids.push(selectedGroup);
				var unGrouped = RoleDataSource.data().filter( function ( elem ) {
                    return oids.indexOf( elem.oid ) === -1;
                });
                UnGroupedRoleDataSource.data(unGrouped);
			}
	    });
    };

    $("#tabstrip").kendoTabStrip();
}
    function removeItem(e, id) {
            var item = GroupedRoleDataSource.getByUid(id);

            $.ajax({
                type: "DELETE",
                url: getBackendURL() + "role/destroyRoleGroup/" + selectedGroup + "/" + item.oid,
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                success: function(response) {
                        UnGroupedRoleDataSource.add(item);
                        GroupedRoleDataSource.remove(item);
                        $(e).parent().remove();
                },
                error: function(e) {
                    $.pnotify({
                        text: "Bir hata oluştu",
                        type: 'error'
                    });
                }
            });
        };