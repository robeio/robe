function SingletonDataSource(name, parameters) {
    this.name = name;
    this.data = null;
    this.parameters = parameters;

    Robe.call(this, name);

    SingletonDataSource.prototype.initialize = function () {
        this.data = new kendo.data.DataSource(this.parameters);
        this.data.bind("error", this.requestError);
        this.data.bind("requestEnd", this.requestEnd);
    };

    SingletonDataSource.prototype.read = function () {
        this.data.read();
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
            showToast("success", message);
    };
    this.initialize();
};

SingletonDataSource.prototype = new Singleton();

