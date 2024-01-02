

let socket;
window.socket = socket;

(function() {

  console.log("start");
  socket = new WebSocket("wss://localhost:8090/chat");

  socket.addEventListener('open', (event) => {
    console.log('WebSocket 연결이 열렸습니다.');
    // 추가적인 작업 수행 가능
  });

  socket.addEventListener('message', (event) => {
    console.log('서버로부터 메시지 수신:', event.data);
  });
})();

$(document).on("keydown", "#websocket_message", function (e) {
  console.log(e.keyCode);
  if (e.keyCode == 13) $("#transfer_message").click();
})

$(document).on("click", "#transfer_message", function (e) {
  console.log("메세지 전송");
  socket.send(JSON.stringify({
    message: document.getElementById("websocket_message").value
  }));
})