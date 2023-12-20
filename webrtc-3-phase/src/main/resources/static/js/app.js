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

    ws.connect({}, onConnected, onError);

    const messageInput = document.getElementById("send_message_text");
    const sendButton = document.getElementById("send_message");


    ws.connect({}, function (frame) {
        ws.send("/chat/enterUser", {},
          JSON.stringify({
              type: 'ENTER',
              roomId: roomId,
              sender: "eddy"
          }));
    });

    sendButton.addEventListener("click", function(e) {
        console.log(e);
        as.send(JSON.stringify({
            "id": "chat",
            roomId,
            subsId,
            "message": messageInput.value,
            "sender" : "eddy"
        }));
    })
})();

const apiRequestGet = (_url) => {
    axios.get(_url)
        .then(function (response) {
           console.log(response);
        });
}

function onConnected() {
    // sub 할 url => /sub/chat/room/roomId 로 구독한다
    ws.subscribe('/sub/chat/room/' + roomId, onMessageReceived);

    // 서버에 username 을 가진 유저가 들어왔다는 것을 알림
    // /pub/chat/enterUser 로 메시지를 보냄
    ws.send("/pub/chat/enterUser",
        {},
        JSON.stringify({
            "roomId": roomId,
            sender: "eddy",
            type: 'ENTER'
        })
    )
}

function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function onMessageReceived(payload) {
    //console.log("payload 들어오냐? :"+payload);
    var chat = JSON.parse(payload.body);

    console.log(chat);

    var messageElement = document.createElement('li');

    if (chat.type === 'ENTER') {  // chatType 이 enter 라면 아래 내용
        messageElement.classList.add('event-message');
        chat.content = chat.sender + chat.message;
        getUserList();

    } else if (chat.type === 'LEAVE') { // chatType 가 leave 라면 아래 내용
        messageElement.classList.add('event-message');
        chat.content = chat.sender + chat.message;
        getUserList();

    } else { // chatType 이 talk 라면 아래 내용
        messageElement.classList.add('chat-message');

        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(chat.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(chat.sender);

        messageElement.appendChild(avatarElement);

        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(chat.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }

    var contentElement = document.createElement('p');

    // 만약 s3DataUrl 의 값이 null 이 아니라면 => chat 내용이 파일 업로드와 관련된 내용이라면
    // img 를 채팅에 보여주는 작업
    if(chat.s3DataUrl != null){
        var imgElement = document.createElement('img');
        imgElement.setAttribute("src", chat.s3DataUrl);
        imgElement.setAttribute("width", "300");
        imgElement.setAttribute("height", "300");

        var downBtnElement = document.createElement('button');
        downBtnElement.setAttribute("class", "btn fa fa-download");
        downBtnElement.setAttribute("id", "downBtn");
        downBtnElement.setAttribute("name", chat.fileName);
        downBtnElement.setAttribute("onclick", `downloadFile('${chat.fileName}', '${chat.fileDir}')`);


        contentElement.appendChild(imgElement);
        contentElement.appendChild(downBtnElement);

    }else{
        // 만약 s3DataUrl 의 값이 null 이라면
        // 이전에 넘어온 채팅 내용 보여주기기
        var messageText = document.createTextNode(chat.message);
        contentElement.appendChild(messageText);
    }

    messageElement.appendChild(contentElement);

    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}
