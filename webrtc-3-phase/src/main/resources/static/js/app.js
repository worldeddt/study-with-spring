let as = new WebSocket("wss://localhost:8081/chat");
let ms = new WebSocket("wss://localhost:8081/message");

window.as = as;
window.ms = ms;
console.log(ms);
console.log(as);

$(document).on("click", "#createRoomBtn", function(e) {
    console.log("전송 시작");

    as.send(JSON.stringify({
        id : "requestRoom",
        senderId : "eddySender",
        subsId : "ferimRoom",
        roomName : "다같이 놀자 동네 두 바퀴"
    }));
});

const apiRequestGet = (_url) => {
    axios.get(_url)
        .then(function (response) {
           console.log(response);
        });
}

apiRequestGet("https://localhost:8081/room/all/ferimRoom");