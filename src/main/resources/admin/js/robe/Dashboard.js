function initializeDashboard() {
    $("#btnHeapDump").kendoButton({
        click: function (e) {
            e.preventDefault();
            window.open(getBackendURL() + "system/heapdump");
            return false;
        }
    })
    $.ajax({
        type: "GET",
        url: "http://127.0.0.1:8080/admin/metrics",
        dataType: "json",
        crossDomain: true,

        contentType: "application/json; charset=utf-8",
        success: function (response) {
            var data = response["jvm"]["memory"];
            var servletInfo =  response ["org.eclipse.jetty.servlet.ServletContextHandler"];
            var serverUptime = "Server Uptime : " + response["jvm"]["uptime"] + " seconds";


            var memoryData = [
                ["Unused", (data["totalMax"] - data["totalUsed"])],
                ["Used", (data["totalUsed"])]
            ];

            var connPool = response["org.eclipse.jetty.util.thread.QueuedThreadPool"];
            var connMax = connPool["active-threads"]["value"] + connPool["idle-threads"]["value"];
            var connUsed = connPool["active-threads"]["value"];

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

            var heapMemoryData = [
                ["Unused", (data["heapMax"] - data["heapUsed"])],
                ["Used", (data["heapUsed"])]
            ];
            var vmInfo = response["jvm"]["vm"];
            var vmName = vmInfo["name"];
            var vmVersion = vmInfo["version"];
            var heapUsage = data["heap_usage"];
            var nonHeapUsage = data["non_heap_usage"];
            var httpResponseCounts = [servletInfo["1xx-responses"]["count"],servletInfo["2xx-responses"]["count"],servletInfo["3xx-responses"]["count"],servletInfo["4xx-responses"]["count"],servletInfo["5xx-responses"]["count"]];






            Charts.pie("memory", memoryData, "Ram");
            Charts.gauge("threads", [connUsed, connMax], "Threads");
            Charts.pie("allLogback", logBackData, "LOGBACK");
            Charts.pie("heapMemory",heapMemoryData,"Heap Memory");
            Charts.column("responseCount",httpResponseCounts," Response Counts",serverUptime);
            document.getElementById("vmName").innerHTML = vmName;
            document.getElementById("vmVersion").innerHTML = vmVersion;
            document.getElementById("heapUsage").innerHTML ="%" + parseInt(heapUsage * 100) ;
            document.getElementById("nonHeapUsage").innerHTML ="%" + parseInt(nonHeapUsage * 100);

        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(arguments);
            alert('HTTP Error: ' + errorThrown + ' | Error Message: ' + textStatus);
            return;
        }

    });

}