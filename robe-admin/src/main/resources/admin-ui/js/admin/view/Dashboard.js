//@ sourceURL=Dashboard.js
define([
    'text!html/Dashboard.html',
    'admin/data/DataSources',

    'kendo/kendo.button.min',
    'highcharts/highcharts',
    'highcharts/highcharts-more',
    'highcharts/exporting',
//    'highcharts/highcharts-all',
    'robe/Charts'
], function (view) {
    var Dashboard = Backbone.View.extend({
        render: function () {
            $('#container').append(view);
            this.initial();
        },

        initial: function () {
            $("#btnHeapDump").kendoButton({
                click: function (e) {
                    e.preventDefault();
                    window.open(getBackendURL() + "system/heapdump");
                    return false;
                }
            });
            $.ajax({
                type: "GET",
                url: "http://127.0.0.1:8080/admin/metrics",
                dataType: "json",
                crossDomain: true,

                contentType: "application/json; charset=utf-8",
                success: function (response) {
                    var data = response["jvm"]["memory"];
                    var servletInfo = response ["org.eclipse.jetty.servlet.ServletContextHandler"];
                    var serverUptime = "Server Uptime : " + response["jvm"]["uptime"] + " seconds";


                    //Memory data
                    var memoryData = [
                        ["Unused", (data["totalMax"] - data["totalUsed"])],
                        ["Used", (data["totalUsed"])]
                    ];

                    //Connection Pool Data
                    var connPool = response["org.eclipse.jetty.util.thread.QueuedThreadPool"];
                    var connMax = connPool["active-threads"]["value"] + connPool["idle-threads"]["value"];
                    var connUsed = connPool["active-threads"]["value"];


                    //Logback Data
                    var logbackAll = response["ch.qos.logback.core.Appender"]["all"]["count"];
                    var logbackDebug = response["ch.qos.logback.core.Appender"]["debug"]["count"];
                    var logbackError = response["ch.qos.logback.core.Appender"]["error"]["count"];
                    var logbackInfo = response["ch.qos.logback.core.Appender"]["info"]["count"];
                    var logbackTrace = response["ch.qos.logback.core.Appender"]["trace"]["count"];
                    var logbackWarn = response["ch.qos.logback.core.Appender"]["warn"]["count"];
                    var logBackData = [
                        ["Debug", (logbackDebug)],
                        ["Error", (logbackError)],
                        ["Info", (logbackInfo)],
                        ["Trace", (logbackTrace)],
                        ["Warn", (logbackWarn)]
                    ];


                    //Heap Memory Data
                    var heapMemoryData = [
                        ["Unused", (data["heapMax"] - data["heapUsed"])],
                        ["Used", (data["heapUsed"])]
                    ];
                    var heapUsage = data["heap_usage"];
                    var nonHeapUsage = data["non_heap_usage"];

                    // VM info and HTTP response counts data
                    var vmInfo = response["jvm"]["vm"];
                    var vmName = vmInfo["name"];
                    var vmVersion = vmInfo["version"];
                    var httpResponseCounts = [servletInfo["1xx-responses"]["count"], servletInfo["2xx-responses"]["count"], servletInfo["3xx-responses"]["count"], servletInfo["4xx-responses"]["count"], servletInfo["5xx-responses"]["count"]];
                    var categoryList = ['1**', '2**', '3**', '4**', '5**'];

                    // Authenticate io.robe.admin counts and response times
                    var authenticateServiceData = response["io.robe.admin.resources.AuthResource"];
                    var loginMin = authenticateServiceData["login"]["duration"]["min"];
                    var loginMax = authenticateServiceData["login"]["duration"]["max"];
                    var loginMean = authenticateServiceData["login"]["duration"]["mean"];
                    var loginCount = authenticateServiceData["login"]["rate"]["count"];

                    // Auth Token io.robe.admin counts and response times
                    var authTokenServiceData = response["io.robe.auth.impl.tokenbased.TokenBasedAuthenticator"];
                    var getTokenMin = authTokenServiceData ["gets"]["duration"]["min"];
                    var getTokenMax = authTokenServiceData ["gets"]["duration"]["max"];
                    var getTokenMean = authTokenServiceData ["gets"]["duration"]["mean"];

                    // Menu io.robe.admin counts and response times
                    var menuServiceData = response["io.robe.admin.resources.MenuResource"];
                    var createMenuMean = menuServiceData["create"]["duration"]["mean"];
                    var getMenuMean = menuServiceData["getMenus"]["duration"]["mean"];
                    var deleteMenu = menuServiceData["delete"]["duration"]["mean"];

                    Charts.pie("memory", memoryData, "Ram");
                    Charts.gauge("threads", [connUsed, connMax], "Threads");
                    Charts.pie("allLogback", logBackData, "LOGBACK");
                    Charts.pie("heapMemory", heapMemoryData, "Heap Memory");
                    Charts.column("responseCount", httpResponseCounts, 'Http Response Code', " Response Counts", "HTTP Response Code Counts", serverUptime, categoryList);
                    Charts.column("responseTime", [getMenuMean, getTokenMean, loginMean], "Http Response Time", "Time (miliSeconds) ", "Http Response Time", serverUptime, ["Get Menus", "Get Token", " Post Login"])

                    document.getElementById("vmName").innerHTML = vmName;
                    document.getElementById("vmVersion").innerHTML = vmVersion;
                    document.getElementById("heapUsage").innerHTML = "%" + parseInt(heapUsage * 100);
                    document.getElementById("nonHeapUsage").innerHTML = "%" + parseInt(nonHeapUsage * 100);

                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log(arguments);
                    alert('HTTP Error: ' + errorThrown + ' | Error Message: ' + textStatus);
                    return;
                }
            });
        }
    });
    return Dashboard;
});