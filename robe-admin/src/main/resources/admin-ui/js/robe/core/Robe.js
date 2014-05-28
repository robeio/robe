var Robe = (function () {
    this.name;

    function Robe(name) {
        this.name = name;
    };

    Robe.prototype.setName = function (name) {
        this.name = name;
    };
    Robe.prototype.getName = function () {
        return this.name;
    };

    return Robe;
})();