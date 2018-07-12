// listen for messages
let websocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/chat");
websocket.onmessage = message => update(message);
websocket.onclose = () => update("connection lost");

// add event listeners for controls
// mouse clicked on send
document.getElementById("send").addEventListener("click", () => sendMessage(document.getElementById("message").value));
// enter pressed in message box
document.getElementById("message").addEventListener("keypress", function(e) {
    if (e.keyCode == 13) {
        sendMessage(e.target.value);
    }
});

// send message
function sendMessage(message) {
    if (message !== "") {
        // send message to server
        websocket.send(message);
        // clear message box
        document.getElementById("message").value = "";
    }
}

// update for messages
function update(message) {
    console.log(message)

    // parse JSON
    let data = JSON.parse(message.data);

    // display message
    document.getElementById("chat-panel").insertAdjacentHTML("beforeend",
        "<div class='message'>" +
            "<p>" + data.sender + "</p>" +
            "<p>" + data.message + "</p>" +
        "</div>"

    );
}