var ajaxUrl = "ajax/profile/meals/";
var datatableApi;

function updateTable() {
    $.ajax({
        type: "POST",
        url: ajaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxUrl, updateTableByData);
}

$(function () {
    datatableApi = $("#datatable").DataTable({
        "ajax": {
            "url": ajaxUrl,
            "dataSrc": ""
        },
        "paging": false,
        "info": true,
        "columns": [
            {
                "data": "dateTime",
                "render": function (date, type, row) {
                    if (type === "display") {
                        return formatDate(date);
                    }
                    return date;
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
            if (data.exceed) {
                $(row).addClass("exceeded");
            } else $(row).addClass("normal");
        },
        "initComplete": makeEditable
    });

    var startDate = $('#startDate');
    var endDate = $('#endDate');

    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i'
    });

    $('#startTime, #endTime').datetimepicker({
        datepicker: false,
        format: 'H:i'
    });

   startDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        formatdate: 'Y-m-d',
        onShow: function (ct) {
            this.setOptions({
             maxDate: endDate.val()? endDate.val() : false})
        }
    });

    endDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        formatdate: 'Y-m-d',
        onShow: function (ct) {
            this.setOptions({
            minDate: startDate.val()? startDate.val() : false})
        }
    });
});

