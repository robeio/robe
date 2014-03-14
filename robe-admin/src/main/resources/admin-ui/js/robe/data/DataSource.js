//robe.data = robe.data || {};
function Datasource() {
    this.name;

    Robe.call(this, "Datasource");

    Datasource.prototype.getName = function () {
        return this.name + " kaan";
    };
};

Datasource.prototype = new Robe();