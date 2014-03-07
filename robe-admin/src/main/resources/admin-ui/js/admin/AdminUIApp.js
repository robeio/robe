//@ sourceURL = AdminUIApp.js
var admin = admin || {};

var AdminApp = {
    backendURL: "",

    getBackendURL: function () {
        return this.backendURL;
    }
}
define([
    'jquery',
    'underscore',
    'router',
    'backbone',
    'myrouter'
], function ($, _, Router, Backbone,MyRouter) {

    console.log("Loading configuration.");
    var response = JSON.parse($.ajax({
        dataType: "json",
        url: "./config.json",
        async: false
    }).responseText);
    AdminApp.backendURL = response.backendURL;


    var initialize = function () {
//                models: ["./js/admin/Models.js"],
//                datasources: [
//                    {
//                        1: "./js/admin/data/SingletonDataSource.js",
//                        2: "./js/admin/data/SingletonHierarchicalDataSource.js"
//                    },
//                    "./js/admin/data/HierarchicalDataSources.js",
//                    "./js/admin/data/DataSources.js"
//                ],
//                views: [
//                    "./js/admin/view/Workspace.js",
//                    {
//                        1: "./js/admin/view/Login.js",
//                        2: "./js/admin/view/UserManagement.js",
//                        3: "./js/admin/view/RoleManagement.js",
//                        4: "./js/admin/view/MenuManagement.js",
//                        5: "./js/admin/view/PermissionManagement.js",
//                        6: "./js/admin/view/Dashboard.js",
//                        7: "./js/admin/view/ProfileManagement.js",
//                        8: "./js/admin/view/MailTemplateManagement.js",
//                        9: "./js/admin/view/QuartzJobManagement.js"
//                    }
//                ]

//        Router.initialize();

    }

    return {
        initialize: initialize
    };
});






