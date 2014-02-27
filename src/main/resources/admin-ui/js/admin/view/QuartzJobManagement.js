//@ sourceURL=QuartzJobManagement.js
var QuartzJobManagement = robe.util.inherit(robe.view.Page, {
    name : "QuartzJobManagement",
    htmlPath : "./html/QuartzJobManagement.html",
    initialize: function() {
        $("#gridJobs").kendoGrid({
            dataSource: QuartzJobDataSource.get(),
            sortable: true,
            editable: true,
            editable:"popup",
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
                    title : "Cron Expression",
                    editor: this.cronExpressionEditor
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
                            name: "fire",
                            text : "R",
                            className : "grid-command-iconfix",
                            click : fire
                        }
                    ],
                    title : "&nbsp;",
                    width : "80px"
                }
            ],
        edit: this.onEdit

        });
    },
    onEdit: function (e) {
            var editWindow = this.editable.element.data("kendoWindow");
            editWindow.wrapper.css({ width: 400 });
    },
    cronExpressionEditor: function(container, options) {
    var minutes=["Every Minute"];
    var hours=["Every Hour"];
    var days=["Every Day","Last Day of Month"];
    var months=["Every Month"];

    for(var i=1; i<32;i++){
        days.push(i);
    }
    for(var i=1;i<13;i++){
        months.push(i);
    }
    for(var i=1;i<61;i++){
        minutes.push(i);
    }
    for(var i=1;i<25;i++){
        hours.push(i);
    }

    $('<div class="cron" style ="width:220px; height: auto; text-align:center "></div>').appendTo(container);
    $('<span>Minute</span><select id="minute" multiple="multiple"></select>').appendTo(".cron");
    $('<span>Hour</span><select id="hour" multiple="multiple"></select>').appendTo(".cron");
    $('<span>Day</span><select id="day" multiple="multiple"></select>').appendTo(".cron");
    $('<span>Month</span><select id="month" multiple="multiple"></select>').appendTo(".cron");
    $('<div class="well well-sm" style="margin-top:10px;"><span id="generatedCron"> Your Cron Expression </span><br/><span id="minutesCron"></span><span id="hoursCron"></span><span id="daysCron"></span><span id="monthsCron"></span></div>').appendTo(".cron");
    $('<input id="hiddenCron" class="k-textbox" name="cronExpression" data-bind/>').appendTo(".cron");


    var minutes = $("#minute").kendoMultiSelect({
        dataSource: minutes,
        change: onChange,


    });
    var minutes = $("#hour").kendoMultiSelect({
        dataSource: hours,
        change: onChange,
    });
    var days = $("#day").kendoMultiSelect( {
        dataSource: days,
        change: onChange,

    });
    var months = $("#month").kendoMultiSelect({
        dataSource: months,
        change: onChange
    });

    if(options.model.cronExpression!="")
        setDefaultValues(options.model.cronExpression);
    else
        setDefaultValues("1 1 1 1");

}
})

function fire(e) {
    $.ajax({
        type: "POST",
        url: AdminApp.getBackendURL() + "quartzJob/fire",
        dataType: "json",
        data: kendo.stringify(this.dataItem($(e.currentTarget).closest("tr"))),
        contentType: "application/json; charset=utf-8",

    });
}


function onChange(e) {

        var cronString = "";

        var test = $("#hiddenCron").val();
        console.log(" hidden Cron "+test);

        //Minute cron builder and controller

        var multiselect = $("#minute").data("kendoMultiSelect");
        if (multiselect.value().indexOf("Every Minute") != -1){
            multiselect.value(["Every Minute"]);
            cronString += "* ";
        }else if(multiselect.value().length < 1) {
            multiselect.value(1);
        }else {
            cronString = multiselect.value() + " ";
        }

        //Hour cron builder and controller

        var multiselect = $("#hour").data("kendoMultiSelect");
        if(multiselect.value().indexOf("Every Hour") != -1){
            multiselect.value(["Every Hour"]);
            cronString += "* ";
        }
        else if(multiselect.value().length < 1) {
            multiselect.value(1);
        }else{
            cronString += (multiselect.value() + " ");
        }

        //Day cron builder and controller

        var multiselect = $("#day").data("kendoMultiSelect");
        if( multiselect.value().indexOf("Every Day") != -1 ){
            var multiselect = $("#day").data("kendoMultiSelect");
            multiselect.value(["Every Day"]);
            cronString += "* ";
        }else if(multiselect.value().length < 1) {
            multiselect.value(1);
        }
        else {
            cronString += ( multiselect.value() + " ");
        }

        //Month cron builder and controller

        var multiselect = $("#month").data("kendoMultiSelect");
        if(multiselect.value().indexOf("Every Month")!=-1) {
            var multiselect = $("#month").data("kendoMultiSelect");
            multiselect.value(["Every Month"]);
            cronString += "* ";
        }
        else if(multiselect.value().length < 1) {
            multiselect.value(1);
        }else {
            cronString +=( multiselect.value() + " ");
        }

        $("#hiddenCron").val(cronString);
        $("#hiddenCron").trigger("change");
    }

function setDefaultValues(exCron) {
    var allValues = exCron.split(" ");
    var minuteValues= allValues[0].split(",")
    var hourValues = allValues[1].split(",")
    var dayValues = allValues[2].split(",")
    var monthValues = allValues[3].split(",")

    console.log(minuteValues + " -  "+ hourValues +" -  " +dayValues+" -  " +monthValues+ "values"+ allValues)

    if(minuteValues.indexOf("*")!=-1) {
        $("#minute").data("kendoMultiSelect").value("Every Minute");
    }else{
        $("#minute").data("kendoMultiSelect").value(minuteValues);
    }

    if(hourValues.indexOf("*")!=-1){
        $("#hour").data("kendoMultiSelect").value("Every Hour");
    }else {
        $("#hour").data("kendoMultiSelect").value(hourValues);
    }

    if(dayValues.indexOf("*")!=-1){
        $("#day").data("kendoMultiSelect").value("Every Day");
    }else {
        $("#day").data("kendoMultiSelect").value(dayValues);
    }

    if(monthValues.indexOf("*")!=-1){
        $("#month").data("kendoMultiSelect").value("Every Month");
    }else {
        $("#month").data("kendoMultiSelect").value(monthValues);
    }

}





