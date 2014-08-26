admin.data = admin.data || {};
admin.data.SingletonHierarchicalDataSource = {};
define(['js/Models', 'robe/AlertDialog'], function () {
    admin.data.SingletonHierarchicalDataSource = function (name, parameters) {
        this.data = null;
        this.name = name;
        this.parameters = parameters;
        this.get = function () {
            if (this.data == null) {
                console.log("Initializing " + name);
                this.data = new kendo.data.HierarchicalDataSource(this.parameters);
                this.data.bind("error", this.requestError);
                this.data.bind("requestEnd", this.requestEnd);
            } else {
                console.log("Refreshing " + name);
                this.data.read();
            }
            return this.data;
        };
        this.requestError = function (e) {
            var response = e.response;
            var type = e.type;
            var message = "";
            if (type === "update") {
                message = 'Güncelleme sırasında bir hata oluştu.';
            }
            else if (type === "destroy") {
                message = "Silme sırasında bir hata oluştu.";
            }
            else if (type === "read") {
                message = "Veriler getirilirken bir hata oluştu.";
            }
            else if (type === "create") {
                message = "Oluşturulma sırasında bir hata oluştu.";
            }
            if (message != "")
                showToast("error", message);

        };

        this.requestEnd = function (e) {
            var response = e.response;
            var type = e.type;
            var message = "";
            if (type === "update") {
                message = "Başarı ile güncellendi";
            }
            else if (type === "destroy") {
                message = "Başarı ile silindi";
            }
            else if (type === "create") {
                message = "Başarı ile oluşturuldu";
            }
            if (message != "")
                showToast("success", message);
        };

    };
});