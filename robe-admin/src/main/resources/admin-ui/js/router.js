define([
    'bower_components/requirejs-router/router'

], function (Router) {
    console.log("Registering router.");
    Router.registerRoutes({
        Workspace:
            { path: '/Workspace'         , moduleId: 'admin/view/Workspace' },
        Login:
            { path: '/Login'             , moduleId: 'admin/view/Login' },
        ProfileManagement:
            { path: '/ProfileManagement', moduleId: 'admin/view/ProfileManagement' },
        UserManagement:
            { path:'/UserManagement'      ,moduleId:'admin/view/UserManagement'},
        RoleManagement:
            { path:'/RoleManagement'      ,moduleId:'admin/view/RoleManagement'},
        MenuManagement:
        { path:'/MenuManagement'      ,moduleId:'admin/view/MenuManagement'}
    }).on('routeload', function (View, routeArguments) {
        var view = new View(routeArguments);
        view.render();
    }).init();

    var href = window.location.href;
    if(href.indexOf("#/Workspace", href.length - "#/Workspace".length) == -1)
        window.location.href = "#/Workspace";
    return Router;

});