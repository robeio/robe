var UserDataSource = new kendo.data.DataSource({
    transport: {
        read: {
            type: "GET",
            url: getBackendURL() + "user/all",
            dataType: "json",
            contentType: "application/json"
        },
        update: {
            type: "POST",
            url: getBackendURL() + "user",
            dataType: "json",
            contentType: "application/json"
        },
        destroy: {
            type: "DELETE",
            url: getBackendURL() + "user",
            dataType: "json",
            contentType: "application/json"
        },
        create: {
            type: "PUT",
            url: getBackendURL() + "user",
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
        model: UserModel
    }
});

var RoleDataSource = new kendo.data.DataSource({
    transport: {
        read: {
            type: "GET",
            url: getBackendURL() + "role/all",
            dataType: "json",
            contentType: "application/json"
        },
        update: {
            type: "POST",
            url: getBackendURL() + "role",
            dataType: "json",
            contentType: "application/json"
        },
        destroy: {
            type: "DELETE",
            url: getBackendURL() + "role",
            dataType: "json",
            contentType: "application/json"
        },
        create: {
            type: "PUT",
            url: getBackendURL() + "role",
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
        model: RoleModel
    }
});

var MenuDataSource =  new kendo.data.DataSource({
	 transport: {
		 read: {
			 type: "GET",
			 url: getBackendURL() + "menu/all",
			 dataType: "json",
			 contentType: "application/json"
		 },
		 update: {
			 type: "POST",
			 url: getBackendURL() + "menu",
			 dataType: "json",
			 contentType: "application/json"
		 },
		 destroy: {
			 type: "DELETE",
			 url: getBackendURL() + "menu",
			 dataType: "json",
			 contentType: "application/json"
		 },
		 create: {
			 type: "PUT",
			 url: getBackendURL() + "menu",
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
		 model: MenuModel
	 }
});

var ServiceDataSource = new kendo.data.DataSource({
    transport: {
      read: {
        type: "GET",
        url: getBackendURL() + "service/all",
        dataType: "json",
        contentType: "application/json"
      },

    },
    batch: false,
    schema: {
      model: ServiceModel
    }
});