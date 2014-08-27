// Require.js allows us to configure shortcut alias
// There usage will become more apparent further along in the tutorial.
require.config({
    paths: {
        text: 'lib/requirejs/text',
        html: 'html',
        view: 'js/view',
        kendo: 'lib/kendoui/js',
        robe: 'lib/robe',
        admin: 'js'
    },
    shim: {
        'lib/jquery/jquery.min': {
            exports: '$'
        },
        'lib/underscore/underscore': {
            exports: '_'
        },
        'lib/cryptojs/enc-base64-min': {
            deps: ['lib/cryptojs/core-min']
        },
        'lib/cryptojs/sha256': {
            deps: ['lib/cryptojs/enc-base64-min']
        },
        'lib/robe/core/Singleton': {
            deps: ['lib/robe/core/Robe', 'lib/robe/data/DataSource']
        },
        'lib/robe/data/DataSource': {
            deps: ['lib/robe/core/Robe']
        },
        'js/data/SingletonDataSource': {
            deps: ['lib/alertmessage/jquery.toastmessage', 'lib/robe/core/Singleton']
        },
        'lib/robe/Charts': {
            deps: ['lib/highcharts/highcharts']
        },
        'lib/highcharts/highcharts': {
            deps: [ "lib/jquery/jquery.min"],
            exports: 'Highcharts'
        },
        'lib/highcharts/highcharts-more': {
            deps: ['lib/highcharts/highcharts']
        },
        'lib/highcharts/exports': {
            deps: ['lib/highcharts/highcharts']
        },
        'js/router': {
            deps: ['lib/jquery/jquery.min']
        },
        'lib/requirejs-router/router.min': {
            exports: 'Router'
        },
        'lib/robe/AlertDialog': {
            deps: ['lib/alertmessage/jquery.toastmessage']
        }
    }
});

// Load our app module and pass it to our definition function
define([
    'js/AdminUIApp',
    'lib/jquery/jquery.min.js',
    'lib/underscore/underscore',
    'lib/highcharts/highcharts'
], function (App) {
    App.initialize();
});

