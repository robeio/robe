var admin = admin || {};
var AdminApp;

define(['js/router'], function () {

    var initialize = function () {
        AdminApp = {
            backendURL: "",

            getBackendURL: function () {
                return this.backendURL;
            }
        };

        $.getJSON("./config.json", function(response){
            AdminApp.backendURL = response.backendURL;
        });

    }

    return {
        initialize: initialize
    };
});






