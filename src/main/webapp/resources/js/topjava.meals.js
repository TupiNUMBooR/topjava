const userAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        })
    );
});

var filterForm = $('#filter');
var filtered = false;

function updateTable() { // своеобразный override
    if (filtered) {
        $.ajax({
            url: ctx.ajaxUrl + "filter",
            type: "get",
            data: filterForm.serialize()
        }).done(data => {
            fillTable(data);
        });
    } else {
        filterForm.find(":input").val("");
        $.get(ctx.ajaxUrl, data => {
            fillTable(data);
        });
    }
}

function filterTable() {
    filtered = true;
    $.ajax({
        url: ctx.ajaxUrl + "filter",
        type: "get",
        data: filterForm.serialize()
    }).done(data => {
        fillTable(data);
        successNoty("Filtered");
    });
}

function unfilterTable() {
    filtered = false;
    filterForm.find(":input").val("");
    $.get(ctx.ajaxUrl, data => {
        fillTable(data);
        successNoty("Filter cleared");
    });
}
