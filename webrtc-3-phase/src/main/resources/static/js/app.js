const ROOMCODE = "3478";
const PORT = "8081"
const HOST = `https://localhost:${PORT}`
let as = new WebSocket("wss://localhost:8081/chat");
var connectingElement = document.querySelector('.connecting');

const socketJS = new SockJS('/message');
let ws = Stomp.over(socketJS);

window.as = as;
window.ws = ws;

let roomId;
let subsId;
let sender;

(function () {
    const URLSearch = new URLSearchParams(location.search);

    if (URLSearch.get("v")) roomId = URLSearch.get("v");
    if (URLSearch.get("subsId")) subsId = URLSearch.get("subsId");
    if (URLSearch.get("sender")) sender = URLSearch.get("sender");

    if (!roomId || !subsId || !sender) {
        Swal.fire({
            title : "필수값이 누락되었습니다.",
            allowEscapeKey : false,
            allowOutsideClick : false,
            showConfirmButton :true,
            confirmButtonText : "확인"
        }).then(function (response) {
            if (response.isConfirmed) {
                window.location.href =
                  `https://localhost:${PORT}/waiting.html`;
            }
        })
    }

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
              roomId,
              sender
          }));
    });

    sendButton.addEventListener("click", function(e) {
        console.log("message text:", messageInput.value);

        ws.send("/pub/chat/sendMessage", {}, JSON.stringify({
            type: 'TALK',
            subsId,
            roomId,
            sender,
            message: messageInput.value
        }));
        messageInput.value = '';
    })
})();

function receiveMessage(message) {
    const messageBoard = document.getElementById("chat_board");
    let messageBox = document.createElement("span");
    messageBox.innerHTML = `[${message.sender}] ${message.message}`;

    messageBoard.append(messageBox);
}

$(document).on("keydown", "#send_message_text", function(e) {
    if (e.keyCode == 13) $("#send_message").click();
});
