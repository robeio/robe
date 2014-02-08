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
            var memoryData = [
                ["Boş", (data["totalMax"] - data["totalUsed"])],
                ["Kullanılan", (data["totalUsed"])]
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

            Charts.pie("memory", memoryData, "Ram");
            Charts.gauge("threads", [connUsed, connMax], "Threads");
            Charts.pie("allLogback", logBackData, "LOGBACK")
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(arguments);
            alert('HTTP Error: ' + errorThrown + ' | Error Message: ' + textStatus);
            return;
        }

    });

}