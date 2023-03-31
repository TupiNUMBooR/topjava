const userAjaxUrl = "admin/users/";

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
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
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
                    "asc"
                ]
            ]
        })
    );

    $(".user-enabler").click(function (e) {
        var tr = $(this).closest('tr');
        var checkbox = this;
        var enabled = checkbox.checked;
        checkbox.disabled = true;
        $("body").css("cursor", "progress");

        $.ajax({
            url: ctx.ajaxUrl + tr.attr("id") + "?enabled=" + enabled,
            type: "PATCH",
            // data: {"enabled": enabled} // так и не понял почему не работает
        }).done(e => {
            successNoty(enabled ? "Enabled" : "Disabled");
            tr.toggleClass("text-warning", !enabled)
        }).fail(e => {
            checkbox.checked = !enabled
        }).always(e => {
            checkbox.disabled = false;
            $("body").css("cursor", "default");
        });
    });
});

function updateTable() {
    $.get(ctx.ajaxUrl, function (data) {
        fillTable(data);
    });
}
