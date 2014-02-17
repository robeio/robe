//@ sourceURL=App.js
var robe = robe || {
    util: {
        inherit: function (parent, child) {
            for (var prop in parent) {
                if (prop in child) {
                    continue;
                }
                child[prop] = parent[prop];
            }
            return child;
        }
    }
};
robe.App = function (name, parameters) {
    this.name = name;
    this.isReady = false;
    var parameters = parameters;

    this.initialize = function () {
        console.log("Initializing " + this.name);
        //TODO: set all configurations
        this.loadConfig();

        <!--KENDO JS START-->
        var libs = [
            "./js/kendoui/js/kendo.web.min.js",
            "./js/kendoui/js/cultures/kendo.culture.tr-TR.min.js",
            "./js/alertmessage/jquery.toastmessage.js",
            "./js/zebra_cookie.js",
            "./js/cryptojs/core-min.js",
            "./js/cryptojs/enc-base64-min.js",
            "./js/cryptojs/sha256.js",
            "./js/highcharts/highcharts.js",
            "./js/highcharts//exporting.js",
            "./js/highcharts//highcharts-more.js",
            "./js/robe/core/Class.js",
            "./js/robe/core/Singleton.js",
            "./js/robe/Charts.js",
            "./js/robe/Validations.js",
            "./js/robe/view/Page.js"
        ]

        //Initialize models
        if (parameters.models)
            libs = libs.concat(parameters.models);

        //Initialize Datasources
        if (parameters.datasources)
            libs = libs.concat(parameters.datasources);

        //Initialize Pages
        if (parameters.views)
            libs = libs.concat(parameters.views);

        robe.App.instance = this;
        this.loadJS(libs);
    };

    this.destroy = function () {
        console.log("Initialize must be implemented.")
    };


    this.loadJS = function (sources) {
        var size = sources.length;
        try {
            if (size > 0) {
                console.log("Loading " + sources[0]);
                $.getScript(sources[0],function (data, textStatus, jqxhr) {
                    robe.App.instance.loadJS(sources.slice(1));
                }).fail(function (jqxhr, settings, exception) {
                    console.error(jqxhr, settings, exception);

                });
            } else {
                this.isReady = true;
                this.ready();
                console.log("Ready...");
            }
        } catch (e) {
            console.error(e);
        }
    };
    this.loadConfig = function () {
        console.log("Loading configuration.");
        var response = JSON.parse($.ajax({
            dataType: "json",
            url: "./config.json",
            async: false
        }).responseText);
        this.backendURL = response.backendURL;
    };
    var backendURL = "";

    this.getBackendURL = function () {
        return this.backendURL;
    }
    this.ready = function () {
        console.log("This function must be implemented be developer.");
    };
};
robe.App.instance = null;

