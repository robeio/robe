// @ sourceURL = robe/core/Singleton.js
robe.core = robe.core || {};
robe.core.Singleton = robe.util.inherit(robe.core.Class, {
    name: "robe.core.Singleton",
    data: null,
    initialize: function () {
        console.warn("Child class must implement function.");
    },

    read: function () {

    },
    get: function (read) {
        if (this.data == null) {
            console.log("Initializing " + this.name);
            this.data = this.initialize();
        } else {
            this.read();
            if (read)
                read();
        }
        return this.data;
    }
});
