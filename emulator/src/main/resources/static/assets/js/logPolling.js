document.addEventListener("DOMContentLoaded", function() {
    function fetchLogs() {
        fetch('/logs')
            .then(response => response.text())
            .then(html => {
            // create temporary element to parse HTML
            let tempDiv = document.createElement('div');
            tempDiv.innerHTML = html;

            // extract only `<tr>`-elements
            let tableRows = tempDiv.querySelectorAll('tbody tr');
            let newTableContent = '';

            tableRows.forEach(row => {
                newTableContent += row.outerHTML;
            });

            // add all table rows
            document.querySelector(".table tbody").innerHTML = newTableContent;
            //            console.log(newTableContent);
        });
    }

    setInterval(fetchLogs, 2000); // update every x seconds
});