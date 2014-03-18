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

