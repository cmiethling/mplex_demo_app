var ws;

//function setConnected(connected) {
//    document.getElementById("connect").disabled = connected;
//    document.getElementById("disconnect").disabled = !connected;
//}

function connect() {
    ws = new WebSocket('ws://localhost:8091/hwAPI');

    ws.onopen = function() {
        console.log("WebSocket connected");
        setConnected(true);
    };

    ws.onmessage = function(event) {
        console.log("Received message: " + event.data);
        // Sende die empfangene Nachricht zurück an den Server
        ws.send(event.data);
        // Zeige die Nachricht in der Webseite an
        document.getElementById("messages").innerText += "Received and sent back: " + event.data + "\n";
    };

    ws.onclose = function(event) {
    console.log("why disconnecting????????");
    console.log(event);

        console.log("WebSocket disconnected");
        setConnected(false);
    };

    ws.onerror = function(error) {
        console.log("WebSocket error: " + error.message);
    };
}

function disconnect() {

    if (ws != null) {
        ws.close();
    }
    setConnected(false);
}

document.getElementById("connect").onclick = connect;
document.getElementById("disconnect").onclick = disconnect;