let as = new WebSocket("wss://localhost:8081/chat");

window.as = as;
console.log(as);


$(document).on("click", "#createRoomBtn", function(e) {
    console.log("전송 시작");

    as.send(JSON.stringify({
        id : "requestRoom",
        senderId : "eddySender",
        subsId : "eddysubsIddd",
        roomName : "다같이 놀자 동네 두 바퀴"
    }));
});