let socket;
let socketJS;
let stompClient;
window.socket = socket;
window.socketJS = socketJS;
window.stompClient = stompClient;

(function () {
  console.log("start");
  socket = new WebSocket("wss://localhost:8090/chat");

  socket.addEventListener('open', (event) => {
    console.log('WebSocket 연결이 열렸습니다.');
    // 추가적인 작업 수행 가능
  });

  socket.addEventListener('message', (event) => {
    console.log('서버로부터 메시지 수신:', event.data);
  });

  let roomId = Math.random();

  socketJS = new SockJS("/message");
  stompClient = Stomp.over(socket);

  // 연결 시
  stompClient.connect({}, function (frame) {
    stompClient.subscribe('/sub/room2/' + roomId, function(message) {
      console.log(JSON.parse(message.body));
    });

    // 메시지 보내기
    stompClient.send("/pub/sendMessage", {},
      JSON.stringify({'message': 'Hello, Server!'}
      ));
  });
})();

$(document).on("keydown", "#websocket_message", function (e) {
  console.log(e.keyCode);
  if (e.keyCode == 13) $("#transfer_message").click();
})

// $(document).on("click", "#transfer_message", function (e) {
//   console.log("메세지 전송");
//   socket.send(JSON.stringify({
//     message: document.getElementById("websocket_message").value
//   }));
// });

$(document).on("click", "#socket_message", function (e) {
  stompClient.send("/pub/sendMessage", {
      "userType" : "안녕 난 userType이야 header에 위치하고 있지"
    },
    JSON.stringify({
      messsage: document.getElementById("websocket_message").value
    }));
});