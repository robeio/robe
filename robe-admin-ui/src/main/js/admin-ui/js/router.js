define([
    'lib/requirejs-router/router.min'

], function (Router) {
    console.log("Registering router.");
    Router.registerRoutes({
        Workspace: {
            path: '/Workspace',
            moduleId: 'js/view/Workspace'
        },
        Login: {
            path: '/Login',
            moduleId: 'js/view/Login'
        },
        ForgotPassword: {
            path: '/ForgotPassword',
            moduleId: 'js/view/ForgotPassword'
        },
        UserManagement: {
            path: '/UserManagement',
            moduleId: 'js/view/UserManagement'
        },
        RoleManagement: {
            path: '/RoleManagement',
            moduleId: 'js/view/RoleManagement'
        },
        MenuManagement: {
            path: '/MenuManagement',
            moduleId: 'js/view/MenuManagement'
        },
        PermissionManagement: {
            path: '/PermissionManagement',
            moduleId: 'js/view/PermissionManagement'
        },
        Dashboard: {
            path: '/Dashboard',
            moduleId: 'js/view/Dashboard'
        },
        MailTemplateManagement: {
            path: '/MailTemplateManagement',
            moduleId: 'js/view/MailTemplateManagement'
        },
        QuartzJobManagement: {
            path: '/QuartzJobManagement',
            moduleId: 'js/view/QuartzJobManagement'
        },
        UserProfileManagement: {
            path: '/UserProfileManagement',
            moduleId: 'js/view/UserProfileManagement'
        }
    }).on('routeload', function (View, routeArguments) {
        View.render();
    }).init();

    var href = window.location.href;
    if (href.indexOf("#/Workspace", href.length - "#/Workspace".length) == -1) {
        $('#body').html('');
        window.location.href = "#/Workspace";
    }
    return Router;

});