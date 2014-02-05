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
	 change: function(e) {
     	MenuHierarchicalDataSource.read();
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

/*  DATASOURCE AJAX REQUEST CONTROL */
MenuDataSource.bind("error", dataSourceError);
MenuDataSource.bind("requestEnd", dataSourceRequestEnd);
MenuDataSource.fetch();

UserDataSource.bind("error", dataSourceError);
UserDataSource.bind("requestEnd", dataSourceRequestEnd);
UserDataSource.fetch();

RoleDataSource.bind("error", dataSourceError);
RoleDataSource.bind("requestEnd", dataSourceRequestEnd);
RoleDataSource.fetch();

ServiceDataSource.bind("error", dataSourceError);
ServiceDataSource.bind("requestEnd", dataSourceRequestEnd);
ServiceDataSource.fetch();

function dataSourceError(e) {
        var response = e.response;
          var type = e.type;
          if(type === "update") {
            $.pnotify({
                title: "Güncelleme Hatası",
                text: "Güncelleme sırasında bir hata oluştu.",
                type: 'error',
            });
          }
          else if(type === "destroy") {
            $.pnotify({
                title: "Silme Hatası",
                text: "Silme sırasında bir hata oluştu.",
                type: 'error'
            });
          }
          else if(type === "read") {
            $.pnotify({
                title: "Okuma Hatası",
                text: "Veriler getirilirken bir hata oluştu.",
                type: 'info'
            });
          }
          else if(type === "create") {
            $.pnotify({
                title: "Oluşturma Hatası",
                text: "Oluşturulma sırasında bir hata oluştu.",
                type: 'error'
            });
            }
}

function dataSourceRequestEnd (e) {
          var response = e.response;
          var type = e.type;
          if(type === "update") {
            $.pnotify({
                title: "Güncellendi",
                type: 'success'
            });
          }
          else if(type === "destroy") {
            $.pnotify({
                title: "Silindi",
                type: 'info'
            });
          }
          else if(type === "create") {
            $.pnotify({
                title: "Eklendi",
                type: 'success'
            });
            }

}
/*  DATASOURCE AJAX REQUEST CONTROL */
