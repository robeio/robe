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
    		success: function(response){
    			var data = response["jvm"]["memory"];
				var memoryData = [["Boş",(data["totalMax"]-data["totalUsed"])],["Kullanılan",(data["totalUsed"])]];

				var connPool = response["org.eclipse.jetty.util.thread.QueuedThreadPool"];
				var connMax = connPool["active-threads"]["value"] + connPool["idle-threads"]["value"];
				var connUsed = connPool["active-threads"]["value"];
				Charts.pie("memory",memoryData,"Ram");
				Charts.gauge("memory2",[connUsed,connMax],"Threads");
    		},
    		error: function(jqXHR, textStatus, errorThrown){
                    console.log(arguments);
                    alert('HTTP Error: '+errorThrown+' | Error Message: '+textStatus);
                    return;
                }

    });

}