//@ sourceURL=QuartzJobManagement.js
var QuartzJobManagement;

define([
    'text!html/QuartzJobManagement.html',
    'admin/data/DataSources',

    'kendo/kendo.grid.min',
    'robe/view/Page'
], function (view) {

    QuartzJobManagement = new RobeView("QuartzJobManagement", view, "container");

    QuartzJobManagement.render = function () {
        $('#container').append(view);
        QuartzJobManagement.initialize();
    };

    QuartzJobManagement.initialize = function () {
        $("#gridJobs").kendoGrid({
            dataSource: QuartzJobDataSource.get(),
            sortable: true,
            editable: "popup",
            columns: [
                {
                    field: "schedulerName",
                    title: "Scheduler Name"
                },
                {
                    field: "jobClassName",
                    title: "Job Name"
                },
                {
                    field: "cronExpression",
                    title: "Cron Expression",
                    editor: cronExpressionEditor
                },
                {
                    field: "active",
                    title: " Active"
                },
                {
                    command: [
                        {
                            name: "edit",
                            text: {
                                edit: "&nbsp;"
                            },
                            className: "grid-command-iconfix"
                        },
                        {
                            name: "fire",
                            text: "R",
                            className: "grid-command-iconfix",
                            click: fire
                        }
                    ],
                    title: "&nbsp;",
                    width: "80px"
                }
            ]
        });

        function fire(e) {
            $.ajax({
                type: "POST",
                url: AdminApp.getBackendURL() + "quartzJob/fire",
                dataType: "json",
                data: kendo.stringify(this.dataItem($(e.currentTarget).closest("tr"))),
                contentType: "application/json; charset=utf-8"

            });
        }

        function onChange(e) {

            var cronString = "";

            var test = $("#hiddenCron").val();
            console.log(" hidden Cron " + test);

            //Second cron builder and controller

            var multiselectSecond = $("#second").data("kendoMultiSelect");
            if (multiselectSecond.value().indexOf("Every Second") != -1) {
                multiselectSecond.value(["Every Second"]);
                cronString += "* ";
            } else if (multiselectSecond.value().length < 1) {
                multiselectSecond.value(0);
                cronString += multiselectSecond.value() + " ";
            } else {
                cronString += multiselectSecond.value() + " ";
            }

            //Minute cron builder and controller

            var multiselectMinute = $("#minute").data("kendoMultiSelect");
            if (multiselectMinute.value().indexOf("Every Minute") != -1) {
                multiselectMinute.value(["Every Minute"]);
                cronString += "* ";
            } else if (multiselectMinute.value().length < 1) {
                multiselectMinute.value(0);
                cronString += multiselectMinute.value() + " ";
            } else {
                cronString += multiselectMinute.value() + " ";
            }

            //Hour cron builder and controller

            var multiselectHour = $("#hour").data("kendoMultiSelect");
            if (multiselectHour.value().indexOf("Every Hour") != -1) {
                multiselectHour.value(["Every Hour"]);
                cronString += "* ";
            }
            else if (multiselectHour.value().length < 1) {
                multiselectHour.value(0);
                cronString += multiselectHour.value() + " ";
            } else {
                cronString += multiselectHour.value() + " ";
            }

            //Day cron builder and controller

            var multiselectDay = $("#day").data("kendoMultiSelect");
            if (multiselectDay.value().indexOf("Every Day") != -1) {
                multiselectDay.value(["Every Day"]);
                cronString += "* ";
            } else if (multiselectDay.value().length < 1) {
                multiselectDay.value(1);
                cronString += multiselectDay.value() + " ";
            }
            else {
                cronString += multiselectDay.value() + " ";
            }

            //Month cron builder and controller

            var multiselectMonth = $("#month").data("kendoMultiSelect");
            if (multiselectMonth.value().indexOf("Every Month") != -1) {
                multiselectMonth.value(["Every Month"]);
                cronString += "* ";
            } else if (multiselectMonth.value().length < 1) {
                multiselectMonth.value(1);
                cronString += multiselectMonth.value() + " ";
            } else {
                cronString += multiselectMonth.value() + " ";
            }

            //Days of week cron builder and controller

            var multiselectDayOfWeek = $("#dayOfWeek").data("kendoMultiSelect");
            if (multiselectDayOfWeek.value().indexOf("?") != -1) {
                multiselectDayOfWeek.value("?");
                cronString += "?";
            } else if (multiselectDayOfWeek.value().length < 1) {
                multiselectDayOfWeek.value(1);
                cronString += multiselectDayOfWeek.value() + " ";
            } else {
                cronString += multiselectDayOfWeek.value();
            }

            $("#hiddenCron").val(cronString);
            $("#hiddenCron").trigger("change");
        }

        function setDefaultValues(exCron) {
            var allValues = exCron.split(" ");
            var secondValues = allValues[0].split(",");
            var minuteValues = allValues[1].split(",");
            var hourValues = allValues[2].split(",");
            var dayValues = allValues[3].split(",");
            var monthValues = allValues[4].split(",");
            var dayOfWeekValues = allValues[5].split(",");

            console.log(secondValues + " - " + minuteValues + " -  " + hourValues + " -  " + dayValues + " -  " + monthValues + "values" + allValues)

            if (secondValues.indexOf("*") != -1) {
                $("#second").data("kendoMultiSelect").value("Every Second");
            } else {
                $("#second").data("kendoMultiSelect").value(secondValues);
            }

            if (minuteValues.indexOf("*") != -1) {
                $("#minute").data("kendoMultiSelect").value("Every Minute");
            } else {
                $("#minute").data("kendoMultiSelect").value(minuteValues);
            }

            if (hourValues.indexOf("*") != -1) {
                $("#hour").data("kendoMultiSelect").value("Every Hour");
            } else {
                $("#hour").data("kendoMultiSelect").value(hourValues);
            }

            if (dayValues.indexOf("*") != -1) {
                $("#day").data("kendoMultiSelect").value("Every Day");
            } else {
                $("#day").data("kendoMultiSelect").value(dayValues);
            }

            if (monthValues.indexOf("*") != -1) {
                $("#month").data("kendoMultiSelect").value("Every Month");
            } else {
                $("#month").data("kendoMultiSelect").value(monthValues);
            }

            if (dayOfWeekValues.indexOf("?") != -1) {
                $("#dayOfWeek").data("kendoMultiSelect").value("?");
            } else {
                $("#dayOfWeek").data("kendoMultiSelect").value(dayOfWeek);
            }

        }

        function changeCheckBox() {
            if ($(this).is(':checked')) {
                $("#dayOfWeek").data("kendoMultiSelect").enable(true);
                $("#dayOfWeek").data("kendoMultiSelect").value(6);
                $("#day").data("kendoMultiSelect").value("?");
                $("#day").data("kendoMultiSelect").enable(false);
            }
            else {
                $("#dayOfWeek").data("kendoMultiSelect").enable(false);
                $("#dayOfWeek").data("kendoMultiSelect").value("?");
                $("#day").data("kendoMultiSelect").enable(true);
            }
            onChange();
        }

        function cronExpressionEditor(container, options) {
            var seconds = ["Every Second"]
            var minutes = ["Every Minute"];
            var hours = ["Every Hour"];
            var days = ["Every Day", "?", "Last Day of Month"];
            var months = ["Every Month"];
            var daysOfWeek = [
                {name: " ? ", value: "?"},
                {name: "Monday", value: "2"},
                {name: "Tuesday", value: "3"},
                {name: "Wednesday", value: "4"},
                {name: "Thursday", value: "5"},
                {name: "Friday", value: "6"},
                {name: "Saturday", value: "7"},
                {name: "Sunday", value: "1"}
            ];

            // Paramters for cron builder (Days of week,Months, Days, Hours, Minutes, Seconds)

            for (var i = 0; i < 60; i++) {
                seconds.push(i);
            }
            for (var i = 1; i < 32; i++) {
                days.push(i);
            }
            for (var i = 1; i < 13; i++) {
                months.push(i);
            }
            for (var i = 0; i < 60; i++) {
                minutes.push(i);
            }
            for (var i = 0; i < 24; i++) {
                hours.push(i);
            }

            $('<div class="cron" "></div>').appendTo(container);
            $('<div class="header">Second</div><select id="second" multiple="multiple"></select>').appendTo(".cron");
            $('<div class="header">Minute</div><select id="minute" multiple="multiple"></select>').appendTo(".cron");
            $('<div class="header">Hour</div><select id="hour" multiple="multiple"></select>').appendTo(".cron");
            $('<div class="header">Day of Month</div><select id="day" multiple="multiple"></select>').appendTo(".cron");
            $('<div class="header">Month</div><select id="month" multiple="multiple"></select>').appendTo(".cron");
            $('<input type="checkbox" id="enableDaysOfWeek" name="enable" value="Enable Days of Week"><div class="header">Day of Week</div><select id="dayOfWeek" multiple="multiple"></select>').appendTo(".cron");
            $('<div class="well well-sm" style="margin-top:10px; margin-bottom: 10px;"><div class="header"> Your Cron Expression </div></div>').appendTo(".cron");
            $('<input id="hiddenCron" class="k-textbox" name="cronExpression" data-bind/>').appendTo(".cron");
            $('input:checkbox').change(changeCheckBox);


            var seconds = $("#second").kendoMultiSelect({
                dataSource: seconds,
                change: onChange
            });

            var minutes = $("#minute").kendoMultiSelect({
                dataSource: minutes,
                change: onChange
            });

            var minutes = $("#hour").kendoMultiSelect({
                dataSource: hours,
                change: onChange
            });

            var days = $("#day").kendoMultiSelect({
                dataSource: days,
                change: onChange
            });

            var months = $("#month").kendoMultiSelect({
                dataSource: months,
                change: onChange
            });

            var dayOfWeek = $("#dayOfWeek").kendoMultiSelect({
                dataSource: daysOfWeek,
                dataTextField: 'name',
                dataValueField: 'value',
                change: onChange,
                enable: false
            })

            if (options.model.cronExpression != "") {
                setDefaultValues(options.model.cronExpression);
            } else {
                setDefaultValues("0 0 0 0 0 ?");
                onChange();
            }
        }
    };

    return QuartzJobManagement;
});









