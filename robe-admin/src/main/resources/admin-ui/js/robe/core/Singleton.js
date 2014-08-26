function Singleton() {
    this.data = null;

    Robe.call(this, "Singleton");

    Singleton.prototype.initialize = function () {
        console.warn("Child class must implement function.");
    };

    Singleton.prototype.read = function () {

    };

    Singleton.prototype.get = function (read) {
        if(typeof(read)==='undefined') read = true;

        if (this.data == null) {
            console.log("Initializing " + this.name);
        } else if (read) {
            this.read();
        }

        return this.data;
    }
}

Singleton.prototype = new Datasource();
