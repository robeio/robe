// Require.js allows us to configure shortcut alias
// There usage will become more apparent further along in the tutorial.
require.config({
    paths: {
        jquery: 'bower_components/jquery/dist/jquery.min',
        underscore: 'bower_components/underscore/underscore',
        router: 'bower_components/requirejs-router/router',
        myrouter: "router",
        backbone: 'bower_components/backbone/backbone',
        text: 'require/text',
        html: '../html',
        view: 'admin/view',
        kendo: 'kendoui/js',
        datasources: 'admin/data'
    },
    shim: {
        'cryptojs/enc-base64-min': {
            deps: ['cryptojs/core-min']
        },
        'cryptojs/sha256': {
            deps: ['cryptojs/enc-base64-min']
        },
        'robe/core/Singleton':{
            deps:['robe/core/Class']
        }

    }
});

// Load our app module and pass it to our definition function
require([
    'admin/AdminUIApp',
    'robe/core/Class',
    'robe/core/Singleton'], function (App) {
    App.initialize();
});

