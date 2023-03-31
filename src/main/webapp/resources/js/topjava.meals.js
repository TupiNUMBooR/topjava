const mealsAjaxUrl = "profile/meals/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: mealsAjaxUrl
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
        filterForm[0].reset();
        $.get(ctx.ajaxUrl, data => {
            fillTable(data);
        });
    }
}

function filterTable() {
    filtered = true;
    updateTable();
}

function unfilterTable() {
    filtered = false;
    updateTable();
}
