// @ sourceURL = robe/core/Singleton.js
//robe.core = robe.core || {};
//robe.core.Singleton = {
//    name: "robe.core.Singleton",
//    data: null,
//    initialize: function () {
//        console.warn("Child class must implement function.");
//    },
//
//    read: function () {
//
//    },
//    get: function (read) {
//        if (this.data == null) {
//            console.log("Initializing " + this.name);
//            this.data = this.initialize();
//        } else if (read) {
//            read();
//        }
//        return this.data;
//    }
//};

function Singleton() {
    this.data = null;

    Robe.call(this, "Singleton");

    Singleton.prototype.initialize = function () {
        console.warn("Child class must implement function.");
    };

    Singleton.prototype.read = function () {

    };

    Singleton.prototype.get = function (read) {
        if (this.data == null) {
            console.log("Initializing " + this.name);
        } else if (read) {
            read();
        }

        return this.data;
    }
}

Singleton.prototype = new Datasource();
