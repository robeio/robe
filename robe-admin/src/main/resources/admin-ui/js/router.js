define([
    'bower_components/requirejs-router/router'

], function (Router) {
    console.log("Registering router.");
    Router.registerRoutes({
        Workspace: {
            path: '/Workspace',
            moduleId: 'admin/view/Workspace'
        },
        Login: {
            path: '/Login',
            moduleId: 'admin/view/Login'
        },
        ProfileManagement: {
            path: '/ProfileManagement',
            moduleId: 'admin/view/ProfileManagemen'
        },
        UserManagement: {
            path: '/UserManagement',
            moduleId: 'admin/view/UserManagement'
        },
        RoleManagement: {
            path: '/RoleManagement',
            moduleId: 'admin/view/RoleManagement'
        },
        MenuManagement: {
            path: '/MenuManagement',
            moduleId: 'admin/view/MenuManagement'
        },
        PermissionManagement: {
            path: '/PermissionManagement',
            moduleId: 'admin/view/PermissionManagement'
        },
        Dashboard: {
            path: '/Dashboard',
            moduleId: 'admin/view/Dashboard'
        },
        MailTemplateManagement: {
            path: '/MailTemplateManagement',
            moduleId: 'admin/view/MailTemplateManagement'
        },
        QuartzJobManagement: {
            path: '/QuartzJobManagement',
            moduleId: 'admin/view/QuartzJobManagement'
        }
    }).on('routeload',function (View, routeArguments) {
        var view = new View(routeArguments);
        view.render();
    }).init();

    var href = window.location.href;
    if (href.indexOf("#/Workspace", href.length - "#/Workspace".length) == -1){
        $('#body').html('');
        window.location.href = "#/Workspace";
    }
    return Router;

});