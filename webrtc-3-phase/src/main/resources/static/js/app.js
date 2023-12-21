let as = new WebSocket("wss://localhost:8081/chat");
var connectingElement = document.querySelector('.connecting');

const socketJS = new SockJS('/message');
let ws = Stomp.over(socketJS);

window.as = as;
window.ws = ws;

let roomId;
let subsId;

(function () {
    const URLSearch = new URLSearchParams(location.search);

    if (URLSearch.get("v")) roomId = URLSearch.get("v");
    if (URLSearch.get("subsId")) subsId = URLSearch.get("subsId");

    const messageInput = document.getElementById("send_message_text");
    const sendButton = document.getElementById("send_message");

    console.log("roomId : {}", roomId);
    ws.connect({}, function (frame) {
        console.log("roomId on connect :"+ roomId);
        ws.subscribe('/sub/chat/room2/' + roomId, function(message) {
            receiveMessage(JSON.parse(message.body));
        });

        ws.send("/pub/chat/enterUser", {},
          JSON.stringify({
              type: 'ENTER',
              roomId: roomId,
              sender: "eddy"
          }));
    });

    console.log("roomId : {}", roomId);

    sendButton.addEventListener("click", function(e) {
        console.log("message text:", messageInput.value);

        ws.send("/pub/chat/sendMessage", {}, JSON.stringify({
            type: 'TALK',
            subsId,
            roomId,
            sender : "eddy",
            message: messageInput.value
        }));
        messageInput.value = '';
    })
})();

const apiRequestGet = (_url) => {
    axios.get(_url)
        .then(function (response) {
           console.log(response);
        });
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function receiveMessage(message) {
    console.log(message);
    const messageBoard = document.getElementById("chat_board");
    let messageBox = document.createElement("span");
    messageBox.innerHTML = `[${message.sender}] ${message.message}`;

    messageBoard.append(messageBox);
}

$(document).on("keydown", "#send_message_text", function(e) {
    if (e.keyCode == 13) $("#send_message").click();
});
