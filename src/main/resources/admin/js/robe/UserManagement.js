function initializeUserManagement() {
    var dataSource = new kendo.data.DataSource({
        transport: {
            read: {
                type: "GET",
                url: getBackendURL()+"user/all",
                dataType: "json",
                contentType: "application/json"
            },
            update: {
                type: "POST",
                url: getBackendURL()+"user",
                dataType: "json",
                contentType: "application/json"
            },
            destroy: {
                type: "DELETE",
                url: getBackendURL()+"user",
                dataType: "json",
                contentType: "application/json"
            },
            create: {
                type: "PUT",
                url: getBackendURL()+"user",
                dataType: "json",
                contentType: "application/json"
            },
            parameterMap: function(options, operation) {
                if (operation !== "read") {
                    return kendo.stringify(options);
                }
            }
        },
        batch: false,
        pageSize: 20,
        schema: {
            model: User
        }
    });

    $("#gridUsers").kendoGrid({
        dataSource: dataSource,
        groupable: {
            messages: {
              empty: "Gruplandırma için kolonu buraya sürükleyin"
            }
        },
        sortable: true,
        filterable: true,
        resizable: true,
        pageable: {
            refresh: true
        },
        toolbar: [{name:"create",text:"Yeni Kullanıcı"}],
        columns: [{
            field: "name",
            title: "Ad"
        }, {
            field: "surname",
            title: "Soyad"
        }, {
            field: "email",
            title: "E-posta"
        },{
            field: "password",
            title: "Şifre",
            hidden:true

        },{
			  field: "roleOid",
			  title: "Rol",
              editor: userRoleDropDownEditor,
              hidden:true
		},{
			  field: "firmOid",
			  title: "Firma",
			  editor: userFirmDropDownEditor,
			  hidden:true
		}, {
            field: "active",
            title: "Aktif mi?",
            template: "#= (active)? 'Evet':'Hayır'#"
        }, {
            command: [{name:"edit",text:""},{name: "destroy",text:""}],
            title: "&nbsp;",
            width: "130px"
        }],
        editable: "popup"
    });

    function userRoleDropDownEditor(container, options) {
    	var cmbDataSource = new kendo.data.DataSource({
		   transport: {
			   read: {
				   type: "GET",
				   url: getBackendURL()+"role/all",
				   dataType: "json",
				   contentType: "application/json"
			   }
		   },
		   schema: {
			   model: Role
		   }
	   });
		$('<input required  data-text-field="name" data-value-field="oid"  data-bind="value:' + options.field + '"/>')
			.appendTo(container)
			.kendoDropDownList({
				autoBind: false,
				dataTextField: "name",
                dataValueField: "oid",
				text:"Seçiniz...",
				dataSource: cmbDataSource ,
				placeholder: "Seçiniz...",
				index:-1
		});
    }
    function userFirmDropDownEditor(container, options) {
        	var cmbDataSource = new kendo.data.DataSource({
    		   transport: {
    			   read: {
    				   type: "GET",
    				   url: getBackendURL()+"firm/all",
    				   dataType: "json",
    				   contentType: "application/json"
    			   }
    		   },
    		   schema: {
    			   model: Firm
    		   }
    	   });
    		$('<input required data-bind="value:' + options.field + '"/>')
    			.appendTo(container)
    			.kendoDropDownList({
    				autoBind: false,
    				dataTextField: "organizationIdentifier",
                    dataValueField: "oid",
    				dataSource: cmbDataSource  ,
    				index:-1 ,
    				text: "Seçiniz..."
    		});
        }

}