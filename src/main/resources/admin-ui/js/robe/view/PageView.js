//@ sourceURL=SingletonDataSource.js
var robe = robe || {};
robe.view = robe.view || {};
robe.view.PageView = function(name,parameters) {

    this.initialize = function(){
        console.log("Initialize must be implemented.")
    }

    this.destroy = function(){
        console.log("Initialize must be implemented.")
    }
};