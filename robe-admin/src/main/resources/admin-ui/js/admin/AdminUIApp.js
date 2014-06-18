var admin = admin || {};
var AdminApp;

define(['router'], function () {

    console.info("Loading configuration.");

    var initialize = function () {
        AdminApp = {
            backendURL: "",

            getBackendURL: function () {
                return this.backendURL;
            }
        }
        var response = JSON.parse($.ajax({
            dataType: "json",
            url: "./config.json",
            async: false
        }).responseText);
        AdminApp.backendURL = response.backendURL;
        console.debug(response);
    }

    return {
        initialize: initialize
    };
});






