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
        },
        isArray: function (o) {
            return Object.prototype.toString.call(o) === '[object Array]';
        },
        isObject: function (o) {
            return Object.prototype.toString.call(o) === '[object Object]';
        },
        isString: function (o) {
            return Object.prototype.toString.call(o) === '[object String]';
        },
        isFunction: function (o) {
            return Object.prototype.toString.call(o) === '[object Function]';
        }

    }
};
robe.App = {
    name: name,
    isReady: false,
    parameters: null,

    initialize: function () {
        console.log("Initializing " + this.name);
        //TODO: set all configurations
        this.loadConfig();

        <!--KENDO JS START-->
        var libs = [
            {
                1: ["./js/kendoui/js/kendo.web.min.js",
                    "./js/kendoui/js/cultures/kendo.culture.tr-TR.min.js"],
                2: "./js/alertmessage/jquery.toastmessage.js",
                3: "./js/zebra_cookie.js",
                4: [
                    "./js/cryptojs/core-min.js",
                    "./js/cryptojs/enc-base64-min.js",
                    "./js/cryptojs/sha256.js"],
                5: [
                    "./js/highcharts/highcharts.js",
                    "./js/highcharts//exporting.js",
                    "./js/highcharts//highcharts-more.js"]
            },
            "./js/robe/core/Class.js",
            "./js/robe/core/Singleton.js",
            {
                1: "./js/robe/Charts.js",
                2: "./js/robe/Validations.js",
                3: "./js/robe/view/Page.js"
            }
        ]

        //Initialize models
        if (this.parameters.models)
            libs = libs.concat(this.parameters.models);

        //Initialize Datasources
        if (this.parameters.datasources)
            libs = libs.concat(this.parameters.datasources);

        //Initialize Pages
        if (this.parameters.views)
            libs = libs.concat(this.parameters.views);
        function ready() {
            console.log("ready")
            if (!me.isReady) {
                $.ajaxSetup({
                    cache: false
                });
                me.isReady = true;
                me.ready();

            };
        }
        libs.push(ready);
        $.ajaxSetup({
            cache: true
        });
        var me = this;
        this.loadJS(me, libs);

    },

    destroy: function () {
        console.log("Initialize must be implemented.")
    },


    loadJS: function (me, sources, callback,initial) {
        if (robe.util.isArray(sources)) {
            me.loadSequential(me, sources, callback);
        } else if (robe.util.isObject(sources)) {
            me.loadSimultaneously(me, sources, callback);
        } else if (robe.util.isString(sources, callback)) {
            me.loadSingle(me, sources, callback);
        } else if (robe.util.isFunction(sources)){
            sources();
        }


    },
    loadSequential: function (me, sources, callback) {
        var size = sources.length;
        try {
            if (size > 0) {
                var remaining = sources.slice(1);
                me.loadJS(me, sources[0], function callback2() {
                    me.loadJS(me, remaining, callback);
                });
            }
        } catch (e) {
            console.error(e);
            console.log(Object.prototype.toString.call(sources));
        }

    },
    loadSimultaneously: function (me, sources, callback) {
        try {
            for (var i = 1; sources[i] != null; i++) {
                var isLast = sources[i+1] != null;
                if(isLast)
                    me.loadJS(me, sources[i],callback);
                else
                    me.loadJS(me, sources[i]);
            }

//            if (callback)
//                callback();
        } catch (e) {
            console.error(e);
        }
    },
    loadSingle: function (me, sources, callback) {
        try {
            console.log("Loading " + sources);

            $.getScript(sources).done(function (){ if(callback)callback();}).fail(function (jqxhr, settings, exception) {
                console.error(jqxhr, settings, exception);
            });
        } catch (e) {
            console.error(e);
        }
    },

    loadConfig: function () {
        console.log("Loading configuration.");
        var response = JSON.parse($.ajax({
            dataType: "json",
            url: "./config.json",
            async: false
        }).responseText);
        this.backendURL = response.backendURL;
    },

    backendURL: "",

    getBackendURL: function () {
        return this.backendURL;
    },

    ready: function () {
        console.log("This function must be implemented be developer.");
    }
};

