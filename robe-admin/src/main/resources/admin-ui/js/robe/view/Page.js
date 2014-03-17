//@ sourceURL=robe/view/Page.js
//robe.view = robe.view || {};
//
//robe.view.Page = robe.view.Page || {};
//
//
//robe.view.Page = {
//    containerId: null,
//    htmlPath: null,
//    setContainerId: function (containerId) {
//        this.containerId = containerId;
//    },
//    bindHtml: function (path) {
//        this.htmlPath = path
//    },
//
//    show: function (preAction,postAction) {
//        if(preAction)
//            preAction();
//
//        var me = this;
//        $("#" + this.containerId).load(this.htmlPath, function () {
//            me.initialize();
//            if(postAction)
//                postAction();
//        });
//    },
//    initialize: function () {
//        console.warn("Child class must implement function.");
//    }
//}

var RobeView = (function () {
    this.name = null;
    this.containerId = null;
    this.htmlPath = null;

    function RobeView(name, htmlPath, containerId) {
        this.name = name;
        this.htmlPath = htmlPath;
        this.containerId = containerId;
    }

    RobeView.prototype.show = function (preAction, postAction) {
        if (preAction) {
            preAction();
        }

        var me = this;

        $("#" + this.containerId).load(this.htmlPath, function () {
            me.initialize();
            if (postAction) {
                postAction();
            }
        });
    };

    RobeView.prototype.render = function () {
        console.warn(this.name + " render run");
    }

    RobeView.prototype.initialize = function () {
        console.warn(this.name + " initialize run");
    }

    return RobeView;
})();

