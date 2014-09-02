var Dashboard;

define([
    'text!html/Dashboard.html',
    'kendo/kendo.button.min',
    'kendo/kendo.dropdownlist.min',
    'lib/highcharts/highcharts',
    'lib/highcharts/highcharts-more',
    'lib/highcharts/exporting',
    'robe/Charts',
    'robe/view/RobeView'
], function (view) {

    Dashboard = new RobeView("Dashboard", view, "container");

    Dashboard.render = function () {
        $('#container').append(view);
        Dashboard.initialize();
    };

    Dashboard.initialize = function () {
        $("#btnHeapDump").kendoButton({
            click: function (e) {
                e.preventDefault();
                window.open(AdminApp.getBackendURL() + "system/heapdump");
                return false;
            }
        });
        $.ajax({
            type: "GET",
            url: AdminApp.getBackendURL() + "../admin/metrics",
            dataType: "json",
            crossDomain: true,

            contentType: "application/json; charset=utf-8",
            success: function (response) {
                var data = response["gauges"];
                var servletInfo = response ["meters"];
                var serverUptime = "Server Uptime : " + response["gauges"]["jvm.gc.PS-Scavenge.time"]["value"] + " seconds";


                //Memory data
                var memoryData = [
                    ["Unused", (data["jvm.memory.total.max"]["value"] - data["jvm.memory.total.used"]["value"])],
                    ["Used", (data["jvm.memory.total.used"]["value"])]
                ];

                //Connection Pool Data

                var connMax = data["jvm.threads.count"].value + data["jvm.threads.daemon.count"].value;
                var connUsed = data["jvm.threads.runnable.count"].value;


                //Logback Data
                var logbackAll = response["meters"]["ch.qos.logback.core.Appender.all"]["count"];
                var logbackDebug = response["meters"]["ch.qos.logback.core.Appender.debug"]["count"];
                var logbackError = response["meters"]["ch.qos.logback.core.Appender.error"]["count"];
                var logbackInfo = response["meters"]["ch.qos.logback.core.Appender.info"]["count"];
                var logbackTrace = response["meters"]["ch.qos.logback.core.Appender.trace"]["count"];
                var logbackWarn = response["meters"]["ch.qos.logback.core.Appender.warn"]["count"];
                var logBackData = [
                    ["Debug", (logbackDebug)],
                    ["Error", (logbackError)],
                    ["Info", (logbackInfo)],
                    ["Trace", (logbackTrace)],
                    ["Warn", (logbackWarn)]
                ];


                //Heap Memory Data
                var heapMemoryData = [
                    ["Unused", (data["jvm.memory.heap.max"]["value"] - data["jvm.memory.heap.used"]["value"])],
                    ["Used", (data["jvm.memory.heap.used"]["value"])]
                ];
                var heapUsage = data["jvm.memory.heap.usage"]["value"];
                var nonHeapUsage = data["jvm.memory.non-heap.usage"]["value"];

                var vmName = "";
                var vmVersion = response["version"];
                var httpResponseCounts = [servletInfo["io.dropwizard.jetty.MutableServletContextHandler.1xx-responses"]["count"], servletInfo["io.dropwizard.jetty.MutableServletContextHandler.2xx-responses"]["count"], servletInfo["io.dropwizard.jetty.MutableServletContextHandler.3xx-responses"]["count"], servletInfo["io.dropwizard.jetty.MutableServletContextHandler.4xx-responses"]["count"], servletInfo["io.dropwizard.jetty.MutableServletContextHandler.5xx-responses"]["count"]];
                var categoryList = ['1**', '2**', '3**', '4**', '5**'];

                var timers = response["timers"];

                var resourcesNames = [];
                var resourcesDataCount = [];
                var resourcesDataMean = [];
                var resourcesDataMax = [];
                var resourcesDataMin = [];
                for (var key in timers) {
                    if (timers.hasOwnProperty(key))
                        if (key.match("^io.robe.admin.resources")) {
                            var names = key.split('.');
                            resourcesNames.push(names[names.length - 1]);
                            resourcesDataCount.push(timers[key]["count"]);
                            resourcesDataMean.push(timers[key]["mean"]);
                            resourcesDataMax.push(timers[key]["max"]);
                            resourcesDataMin.push(timers[key]["min"]);
                        }
                }
                var seriesTime = [
                    {
                        name: 'Mean',
                        data: resourcesDataMean
                    },
                    {
                        name: 'Max',
                        data: resourcesDataMax
                    },
                    {
                        name: 'Min',
                        data: resourcesDataMin
                    }
                ];
                var seriesCount = [
                    {
                        name: 'Count',
                        data: resourcesDataCount
                    }
                ];

                Charts.pie("memory", memoryData, "Ram");
                Charts.gauge("threads", [connUsed, connMax], "Threads");
                Charts.pie("allLogback", logBackData, "LOGBACK");
                Charts.pie("heapMemory", heapMemoryData, "Heap Memory");
                Charts.column("responseCount", httpResponseCounts, 'Http Response Code', " Response Counts", "HTTP Response Code Counts", serverUptime, categoryList);
                Charts.row("robeAdminResourcesCount", resourcesNames, seriesCount, "Count");
                Charts.row("robeAdminResourcesTime", resourcesNames, seriesTime, "Second");
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
    };

    return Dashboard;
});