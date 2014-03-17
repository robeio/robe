// Require.js allows us to configure shortcut alias
// There usage will become more apparent further along in the tutorial.
require.config({
    paths: {
        text: 'require/text',
        html: '../html',
        view: 'admin/view',
        kendo: 'kendoui/js'
    },
    shim: {
        'bower_components/jquery/dist/jquery.min': {
            exports: '$'
        },
        'bower_components/underscore/underscore': {
            exports: '_'
        },
        'cryptojs/enc-base64-min': {
            deps: ['cryptojs/core-min']
        },
        'cryptojs/sha256': {
            deps: ['cryptojs/enc-base64-min']
        },
        'robe/core/Singleton': {
            deps: ['robe/core/Class', 'robe/data/DataSource']
        },
        'robe/data/DataSource': {
            deps: ['robe/core/Class']
        },
        'admin/data/SingletonDataSource': {
            deps: ['robe/core/Singleton']
        },
        'robe/Charts': {
            deps: ['highcharts/highcharts']
        },
        'highcharts/highcharts': {
            deps: [ "bower_components/jquery/dist/jquery.min"],
            exports: 'Highcharts'
        },
        'highcharts/highcharts-more': {
            deps: ['highcharts/highcharts']
        },
        'highcharts/exports': {
            deps: ['highcharts/highcharts']
        },
        'robe/AlertDialog': {
            deps: ['alertmessage/jquery.toastmessage']
        },
        'router': {
            deps: ['bower_components/jquery/dist/jquery.min']
        }
    }
});

// Load our app module and pass it to our definition function
define([
    'admin/AdminUIApp',
    'bower_components/jquery/dist/jquery.min',
    'bower_components/underscore/underscore',
    'highcharts/highcharts'
], function (App) {
    App.initialize();
});

