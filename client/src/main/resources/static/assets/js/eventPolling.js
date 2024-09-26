$(document).ready(function () {
    //    // Get the CSRF token and header from the meta tags
    //    var csrfToken = $('meta[name="_csrf"]').attr('content');
    //    var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    function updateEventsTable() {
        $.ajax({
            url: '/service_client/events', // API-Endpoint, will return only events table
            type: 'GET',
            success: function (data) {
                // update content of table
                // Get the server response (data) and convert it into a jQuery object
                var response = $(data);
                // Find the #events element in the server response
                var newContent = response.find('#events').html();
                //                console.log(newContent);

                // Update the current #events element in the page with the new content
                $('#events').html(newContent);
            },
            error: function (err) {
                console.error("Error while updating the Events table: ", err);
            }
        });
    }

    // update every 2s
    setInterval(updateEventsTable, 2000);
});