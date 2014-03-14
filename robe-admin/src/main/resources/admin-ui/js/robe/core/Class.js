// @ sourceURL = robe/core/Class.js
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
    Robe.prototype.isArray = function (o) {
        return Object.prototype.toString.call(o) === '[object Array]';
    };
    Robe.prototype.isObject = function (o) {
        return Object.prototype.toString.call(o) === '[object Object]';
    };
    Robe.prototype.isString = function (o) {
        return Object.prototype.toString.call(o) === '[object String]';
    };
    Robe.prototype.isFunction = function (o) {
        return Object.prototype.toString.call(o) === '[object Function]';
    };

    return Robe;
})();