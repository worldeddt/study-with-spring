const ROOMCODE = "ferimRoom";
const HOST = "https://localhost:8081"
const ROOM_ALL_URI = "/room/all"
let roomList = [];
let as = new WebSocket("wss://localhost:8081/chat");

async function apiRequestGet(_url) {
  return await axios.get(_url);
}

async function apiRequestPost(_url, _data) {
  return await axios.post(_url, _data)
}

(function () {
  const createRoomButton = document.getElementById("room_create");

  apiRequestGet(`${HOST}${ROOM_ALL_URI}/${ROOMCODE}`)
    .then(function (response) {
      window.roomList = roomList;
      response.data.list.map(property => {
          roomList.push(JSON.parse(property));
      });


    }).catch(function (reason) {
      console.log("reason", reason);
    }).finally(function (e) {
      settingListBody();
  });

  createRoomButton.addEventListener("click", function(e) {
    Swal.fire({
      title: "주의",
      text : "방을 새로 생성 하시겠습니까?",
      showConfirmButton : true,
      confirmButtonText : "확인",
      showCancelButton : true,
      cancelButtonText : "취소"
    }).then(value => {

      if (!document.getElementById("room_title").value) {
        $.toast({
          heading: 'Information',
          text: '방 제목란이 비어있습니다.',
          showHideTransition: 'slide',
          icon: 'info'
        })
        return;
      }
      if (value.isConfirmed) {
        as.send(JSON.stringify({
          id : "requestRoom",
          senderId : "eddySender",
          subsId : "ferimRoom",
          roomName : document.getElementById("room_title").value || ""
        }));
      }
    });
  });
})();

function settingListBody() {
    const tbody = document.getElementById("room-body");

    for (room of roomList) {
      let tr= document.createElement('tr');
      let titleTd= document.createElement('td');
      let peopleNumberTd = document.createElement("td");

      titleTd.innerHTML =  room.roomName;
      tr.append(titleTd);

      tbody.append(tr);
    }
}