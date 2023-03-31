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

filterForm = $('#filter');

function filterTable() {
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
    filterForm.find(":input").val("");
    $.get(ctx.ajaxUrl, data => {
        fillTable(data);
        successNoty("Filter cleared");
    });
}
