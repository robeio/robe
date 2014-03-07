// RequireJS Router - A scalable, lazy loading, AMD router.
//
// Version: 0.7.3
// 
// The MIT License (MIT)
// Copyright (c) 2014 Erik Ringsmuth
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
// IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
// OR OTHER DEALINGS IN THE SOFTWARE.

define([], function() {
  'use strict';

  // Private closure variables
  var cachedUrlPaths = {};
  var eventHandlers = {
    statechange: [],
    routeload: []
  };

  // In some modern browsers a hashchange also fires a popstate. There isn't a check to see if the browser will fire
  // one or both. We have to keep track of the previous state to prevent it from fireing a statechange twice.
  var previousState = '';
  var popstateHashchangeEventLisener = function popstateHashchangeEventLisener() {
    if (previousState != window.location.href) {
      previousState = window.location.href;
      router.fire('statechange');
    }
  };

  // router public interface
  //
  // There is only one instance of the router. Loading it in multiple modules will always load the same router.
  var router = {
    // router.init([options]) - initializes the router
    init: function init(options) {
      if (typeof(options) === 'undefined') options = {};

      // Set up the window popstate and hashchange event listeners
      if (window.addEventListener) {
        window.addEventListener('popstate', popstateHashchangeEventLisener, false);
        window.addEventListener('hashchange', popstateHashchangeEventLisener, false);
      } else {
        // IE 8 and lower
        window.attachEvent('popstate', popstateHashchangeEventLisener); // In case pushState has been polyfilled
        window.attachEvent('onhashchange', popstateHashchangeEventLisener);
      }

      // Call loadCurrentRoute on every statechange event
      if (options.loadCurrentRouteOnStateChange !== false) {
        router.on('statechange', function onstatechange() {
          router.loadCurrentRoute();
        });
      }

      // Fire the initial statechange event
      if (options.fireInitialStateChange !== false) {
        router.fire('statechange');
      }

      return router;
    },

    // router.routes - All registered routes
    routes: {},

    // router.activeRoute - A reference to the active route
    activeRoute: {},

    // router.registerRoutes(routes) - Register routes
    //
    // This will add the routes to the existing routes. Specifying a route with the same name as an existing route will
    // overwrite the old route with the new one.
    //
    // Example
    // router.registerRoutes({
    //   home: {path: '/', moduleId: 'home/homeView'},
    //   customer: {path: '/customer/:id', moduleId: 'customer/customerView'},
    //   notFound: {path: '*', moduleId: 'notFound/notFoundView'}
    // })
    registerRoutes: function registerRoutes(routes) {
      for (var route in routes) {
        if (routes.hasOwnProperty(route)) {
          router.routes[route] = routes[route];
        }
      }
      return router;
    },

    // router.on(eventName, eventHandler([arg1, [arg2]]) {}) - Register an event handler
    //
    // The two main events are 'statechange' and 'routeload'.
    on: function on(eventName, eventHandler) {
      if (typeof(eventHandlers[eventName]) === 'undefined') eventHandlers[eventName] = [];
      eventHandlers[eventName].push(eventHandler);
      return router;
    },

    // router.fire(eventName, [arg1, [arg2]]) - Fire an event
    //
    // This will call all eventName event handlers with the arguments passed in.
    fire: function fire(eventName) {
      if (eventHandlers[eventName]) {
        var eventArguments = Array.prototype.slice.call(arguments, 1);
        for (var i = 0; i < eventHandlers[eventName].length; i++) {
          eventHandlers[eventName][i].apply(router, eventArguments);
        }
      }
      return router;
    },

    // router.off(eventName, eventHandler) - Remove an event handler
    //
    // If you want remove an event handler you need to keep a reference to it so you can tell router.off() with the
    // original event handler.
    off: function off(eventName, eventHandler) {
      if (eventHandlers[eventName]) {
        var eventHandlerIndex = eventHandlers[eventName].indexOf(eventHandler);
        if (eventHandlerIndex !== -1) {
          eventHandlers[eventName].splice(eventHandlerIndex, 1);
        }
      }
      return router;
    },

    // router.loadCurrentRoute() - Manually tell the router to load the module for the current route
    loadCurrentRoute: function loadCurrentRoute() {
      for (var i in router.routes) {
        if (router.routes.hasOwnProperty(i)) {
          var route = router.routes[i];
          if (router.testRoute(route)) {
            // This is the first route to match the current URL
            // Replace router.activeRoute with this route
            router.activeRoute.active = false;
            route.active = true;
            router.activeRoute = route;

            // Load the route's module
            require([route.moduleId], function(module) {
              // Make sure this is still the active route from when loadCurrentRoute was called. The asynchronous nature
              // of AMD loaders means we could have fireed multiple hashchanges or popstates before the AMD module finished
              // loading. If we navigate to route /a then navigate to route /b but /b finishes loading before /a we don't
              // want /a to be rendered since we're actually at route /b.
              if (route.active) {
                router.fire('routeload', module, router.routeArguments(route, window.location.href));
              }
            });
            break;
          }
        }
      }
      return router;
    },

    // urlPath(url) - Parses the url to get the path
    //
    // This will return the hash path if it exists or return the real path if no hash path exists.
    //
    // Example URL = 'http://domain.com/other/path?queryParam3=false#/example/path?queryParam1=true&queryParam2=example%20string'
    // path = '/example/path'
    //
    // Note: The URL must contain the protocol like 'http(s)://'
    urlPath: function urlPath(url) {
      // Check the cache to see if we've already parsed this URL
      if (typeof(cachedUrlPaths[url]) !== 'undefined') {
        return cachedUrlPaths[url];
      }

      // The relative URI is everything after the third slash including the third slash
      // Example relativeUri = '/other/path?queryParam3=false#/example/path?queryParam1=true&queryParam2=example%20string'
      var splitUrl = url.split('/');
      var relativeUri = '/' + splitUrl.splice(3, splitUrl.length - 3).join('/');

      // The path is everything in the relative URI up to the first ? or #
      // Example path = '/other/path'
      var path = relativeUri.split(/[\?#]/)[0];

      // The hash is everything from the first # up to the the search starting with ? if it exists
      // Example hash = '#/example/path'
      var hashIndex = relativeUri.indexOf('#');
      if (hashIndex !== -1) {
        var hash = relativeUri.substring(hashIndex).split('?')[0];
        if (hash.substring(0, 2) === '#/') {
          // Hash path
          path = hash.substring(1);
        } else if (hash.substring(0, 3) === '#!/') {
          // Hashbang path
          path = hash.substring(2);
        }
      }

      // Cache the path for this URL
      cachedUrlPaths[url] = path;

      return path;
    },

    // router.testRoute(route, [url]) - Test if the route matches the current URL
    //
    // This algorithm tries to fail or succeed as quickly as possible for the most common cases.
    testRoute: function testRoute(route, url) {
      // Example path = '/example/path'
      // Example route: `exampleRoute: {path: '/example/*', moduleId: 'example/exampleView'}`
      var path = router.urlPath(url || window.location.href);

      // If the path is an exact match or '*' then the route is a match
      if (route.path === path || route.path === '*') {
        return true;
      }

      // Test if it's a regular expression
      if (route.path instanceof RegExp) {
        return route.path.test(path);
      }

      // Look for wildcards
      if (route.path.indexOf('*') === -1 && route.path.indexOf(':') === -1) {
        // No wildcards and we already made sure it wasn't an exact match so the test fails
        return false;
      }

      // Example pathSegments = ['', example', 'path']
      var pathSegments = path.split('/');

      // Example routePathSegments = ['', 'example', '*']
      var routePathSegments = route.path.split('/');

      // There must be the same number of path segments or it isn't a match
      if (pathSegments.length !== routePathSegments.length) {
        return false;
      }

      // Check equality of each path segment
      for (var i in routePathSegments) {
        if (routePathSegments.hasOwnProperty(i)) {
          // The path segments must be equal, be a wildcard segment '*', or be a path parameter like ':id'
          var routeSegment = routePathSegments[i];
          if (routeSegment !== pathSegments[i] && routeSegment !== '*' && routeSegment.charAt(0) !== ':') {
            // The path segment wasn't the same string and it wasn't a wildcard or parameter
            return false;
          }
        }
      }

      // Nothing failed. The route matches the URL.
      return true;
    },

    // router.routeArguments([route, [url]]) - Gets the path variables and query parameter values from the URL
    //
    // Both parameters are optional.
    routeArguments: function routeArguments(route, url) {
      if (!route) route = router.activeRoute;
      if (!url) url = window.location.href;
      var args = {};
      var path = router.urlPath(url);

      // Example pathSegments = ['', example', 'path']
      var pathSegments = path.split('/');

      // Example routePathSegments = ['', 'example', '*']
      var routePathSegments = [];
      if (route && route.path && !(route.path instanceof RegExp)) {
        routePathSegments = route.path.split('/');
      }

      // Get path variables
      // URL '/customer/123'
      // and route `{path: '/customer/:id'}`
      // gets id = '123'
      for (var segmentIndex in routePathSegments) {
        if (routePathSegments.hasOwnProperty(segmentIndex)) {
          var routeSegment = routePathSegments[segmentIndex];
          if (routeSegment.charAt(0) === ':') {
            args[routeSegment.substring(1)] = pathSegments[segmentIndex];
          }
        }
      }

      // Get the query parameter values
      // The search is the query parameters including the leading '?'
      var searchIndex = url.indexOf('?');
      var search = '';
      if (searchIndex !== -1) {
        search = url.substring(searchIndex);
        var hashIndex = search.indexOf('#');
        if (hashIndex !== -1) {
          search = search.substring(0, hashIndex);
        }
      }
      // If it's a hash URL we need to get the search from the hash
      var hashPathIndex = url.indexOf('#/');
      var hashBangPathIndex = url.indexOf('#!/');
      if (hashPathIndex !== -1 || hashBangPathIndex !== -1) {
        var hash = '';
        if (hashPathIndex !== -1) {
          hash = url.substring(hashPathIndex);
        } else {
          hash = url.substring(hashBangPathIndex);
        }
        searchIndex = hash.indexOf('?');
        if (searchIndex !== -1) {
          search = hash.substring(searchIndex);
        }
      }

      var queryParameters = search.substring(1).split('&');
      // split() on an empty string has a strange behavior of returning [''] instead of []
      if (queryParameters.length === 1 && queryParameters[0] === '') {
        queryParameters = [];
      }
      for (var i in queryParameters) {
        if (queryParameters.hasOwnProperty(i)) {
          var queryParameter = queryParameters[i];
          var queryParameterParts = queryParameter.split('=');
          args[queryParameterParts[0]] = queryParameterParts.splice(1, queryParameterParts.length - 1).join('=');
        }
      }

      // Parse the arguments into unescaped strings, numbers, or booleans
      for (var arg in args) {
        var value = args[arg];
        if (value === 'true') {
          args[arg] = true;
        } else if (value === 'false') {
          args[arg] = false;
        } else if (!isNaN(value) && value !== '') {
          // numeric
          args[arg] = +value;
        } else {
          // string
          args[arg] = decodeURIComponent(value);
        }
      }

      return args;
    }
  };

  // Return the router
  return router;
});
