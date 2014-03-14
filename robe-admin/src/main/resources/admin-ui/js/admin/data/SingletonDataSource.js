//@ sourceURL=SingletonDataSource.js
//admin.data = admin.data || {};
//define([
//    'robe/core/Singleton', 'admin/Models', 'robe/AlertDialog'], function () {
//    admin.data.SingletonDataSource = robe.util.inherit(robe.core.Singleton, {
//        data: null,
//        name: "admin.data.SingletonDataSource",
//        parameters: null,
//        initialize: function () {
//            data = new kendo.data.DataSource(this.parameters);
//            data.bind("error", this.requestError);
//            data.bind("requestEnd", this.requestEnd);
//            data.read();
//            return data;
//        },
//        read: function () {
//            console.log("Refreshing " + this.name);
//            data.read();
//        },
//        requestError: function (e) {
//            var response = e.response;
//            var type = e.type;
//            var message = "";
//            if (type === "update") {
//                message = 'Güncelleme sırasında bir hata oluştu.';
//            }
//            else if (type === "destroy") {
//                message = "Silme sırasında bir hata oluştu.";
//            }
//            else if (type === "read") {
//                message = "Veriler getirilirken bir hata oluştu.";
//            }
//            else if (type === "create") {
//                message = "Oluşturulma sırasında bir hata oluştu.";
//            }
//            if (message != "")
//                showToast("error", message);
//
//        },
//        requestEnd: function (e) {
//            var response = e.response;
//            var type = e.type;
//            var message = "";
//            if (type === "update") {
//                message = "Başarı ile güncellendi";
//            }
//            else if (type === "destroy") {
//                message = "Başarı ile silindi";
//            }
//            else if (type === "create") {
//                message = "Başarı ile oluşturuldu";
//            }
//            if (message != "")
//                showToast("info", message);
//        }
//
//    });
//});

define([
    'robe/core/Singleton', 'admin/Models', 'robe/AlertDialog'], function () {
    function SingletonDataSource() {
        this.data = null;
        this.parameters = null;

        Robe.call(this, "SingletonDataSource");

        SingletonDataSource.prototype.initialize = function () {
            this.data = new kendo.data.DataSource(this.parameters);
            this.data.bind("error", this.requestError);
            this.data.bind("requestEnd", this.requestEnd);
            this.data.read();
            return data;
        };

        SingletonDataSource.prototype.setParameters = function (parameters) {
            this.parameters = parameters;

            return this.parameters;
        }

        SingletonDataSource.prototype.read = function () {
            console.log("Refreshing " + this.name);
            data.read();
        };

        SingletonDataSource.prototype.requestError = function (e) {
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

        SingletonDataSource.prototype.requestEnd = function (e) {
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
                showToast("info", message);
        };
    };

    SingletonDataSource.prototype = new Singleton();
});