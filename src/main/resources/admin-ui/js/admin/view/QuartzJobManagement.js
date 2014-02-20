var QuartzJobManagement = robe.util.inherit(robe.view.Page, {
    name : "QuartzJobManagement",
    htmlPath : "./html/QuartzJobManagement.html",
    initialize: function() {
        $("#gridJobs").kendoGrid({
            dataSource: QuartzJobDataSource.get(),
            sortable: true,
            editable:"inline",
            columns: [
                {
                    field : "schedulerName",
                    title : "Scheduler Name"
                },
                {
                    field : "jobClassName",
                    title : "Job Name"
                },
                {
                    field : "cronExpression",
                    title : "Cron Expression"
                },
                {
                    command: [
                        {
                            name : "edit",
                            text : {
                                edit : "&nbsp;"
                            },
                            className: "grid-command-iconfix"
                        },
                        {
                            name : "destroy",
                            text : "&nbsp;",
                            className: "grid-command-iconfix"
                        }
                    ],
                    title : "&nbsp;",
                    width : "80px"
                }
            ]
        });
    }
})