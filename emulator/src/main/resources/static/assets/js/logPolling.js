document.addEventListener("DOMContentLoaded", function() {
    function fetchLogs() {
        fetch('/logs')
            .then(response => response.text())
            .then(html => {
            // Temporäres Element erstellen, um das HTML zu parsen
            let tempDiv = document.createElement('div');
            tempDiv.innerHTML = html;

            // Nur die `<tr>`-Elemente extrahieren
            let tableRows = tempDiv.querySelectorAll('tbody tr');
            let newTableContent = '';

            tableRows.forEach(row => {
                newTableContent += row.outerHTML;
            });

            // Nur die neuen Tabellenzeilen einfügen
            document.querySelector(".table tbody").innerHTML = newTableContent;
            //            console.log(newTableContent);
        });
    }

    setInterval(fetchLogs, 2000); // update every second
});