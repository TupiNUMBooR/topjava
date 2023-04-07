const mealAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealAjaxUrl,
    updateTable: function () {
        $.ajax({
            type: "GET",
            url: mealAjaxUrl + "filter",
            data: $("#filter").serialize()
        }).done(updateTableByData);
    }
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    $("#dateTime").datetimepicker({
        format: 'Y-m-d H:i'
    });

    var startDate = $("#startDate");
    var endDate = $("#endDate");
    startDate.datetimepicker({
        format: 'Y-m-d',
        timepicker: false,
        onShow: d => startDate.datetimepicker({maxDate: dtConstraint(endDate)})
    });
    endDate.datetimepicker({
        format: 'Y-m-d',
        timepicker: false,
        onShow: d => endDate.datetimepicker({minDate: dtConstraint(startDate)})
    });

    var startTime = $("#startTime");
    var endTime = $("#endTime");
    startTime.datetimepicker({
        format: 'H:i',
        datepicker: false,
        onShow: d => startTime.datetimepicker({maxTime: dtConstraint(endTime)})
    });
    endTime.datetimepicker({
        format: 'H:i',
        datepicker: false,
        onShow: d => endTime.datetimepicker({minTime: dtConstraint(startTime)})
    });

    makeEditable(
        $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (data, type, row) {
                        if (type === "display") {
                            return data.replace(/(\d{4}-\d{2}-\d{2})T(\d{2}:\d{2}):\d{2}/, "$1 $2");
                        }
                        return data;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-meal-excess", data.excess);
            }
        })
    );
});

function dtConstraint(input) {
    return input.val() ? input.val() : false;
}