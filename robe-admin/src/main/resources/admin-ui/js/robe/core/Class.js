// @ sourceURL = robe/core/Class.js
//var robe = robe || {
//    util: {
//        inherit: function (parent, child) {
//            for (var prop in parent) {
//                if (prop in child) {
//                    continue;
//                }
//                child[prop] = parent[prop];
//            }
//            return child;
//        },
//        isArray: function (o) {
//            return Object.prototype.toString.call(o) === '[object Array]';
//        },
//        isObject: function (o) {
//            return Object.prototype.toString.call(o) === '[object Object]';
//        },
//        isString: function (o) {
//            return Object.prototype.toString.call(o) === '[object String]';
//        },
//        isFunction: function (o) {
//            return Object.prototype.toString.call(o) === '[object Function]';
//        }
//
//    }
//};
//robe.core = robe.core || {};
//
//robe.core.Class = {
//    init:function(){
//
//    },
//    destruct:function(){
//
//    },
//    name: "robe.core.Class"
//}

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

var robe = new Robe("Robe");