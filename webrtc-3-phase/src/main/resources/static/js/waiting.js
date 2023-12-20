const ROOMCODE = "3478";
const PORT = "8081"
const HOST = `https://localhost:${PORT}`
const ROOM_ALL_URI = "/room/all"
let roomList = [];
let as = new WebSocket("wss://localhost:8081/chat");
let roomId = "";

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
          subsId : ROOMCODE,
          roomName : document.getElementById("room_title").value || ""
        }));
        location.reload();
      }
    });
  });
})();

function settingListBody() {
    const tbody = document.getElementById("room-body");

    if (roomList.length > 0) document.getElementById("room_list_empty").style.display = "none";

    for (room of roomList) {
      let tr= document.createElement('tr');
      tr.dataset.roomId = room.roomId;
      tr.style.cursor ="pointer"

      tr.addEventListener("click", function(e) {
        e.preventDefault();
        e.stopPropagation();
        Swal.fire({
          title:"",
          text: "입장 ㄱ?",
          showConfirmButton : true,
          confirmButtonText : "확인",
          showCancelButton : true,
          cancelButtonText : "취소"
        }).then(function(value) {
          if (value.isConfirmed)
            window.location = `https://localhost:${PORT}/index.html?v=${tr.getAttribute("data-room-id")}`;
        }).catch(function(error) {
          console.error(error);
          window.reload();
        });
      });

      let titleTd= document.createElement('td');
      let peopleNumberTd = document.createElement("td");

      let deleteTd = document.createElement('td');
      deleteTd.style.width = "10%";

      let deleteButton = document.createElement("button");
      deleteButton.innerHTML = "삭제";
      deleteButton.setAttribute("class", "btn btn-danger");
      deleteButton.addEventListener("click", function(e) {
        e.preventDefault();
        e.stopPropagation();
        Swal.fire({
          title:"",
          text:"삭제 하시겠습니까?",
          showConfirmButton : true,
          confirmButtonText : "확인",
          showCancelButton : true,
          cancelButtonText : "취소"
        }).then(function(value) {

          if (value.isConfirmed) {
            as.send(JSON.stringify({
              id: "deleteRoom",
              senderId : "eddy",
              subsId : ROOMCODE,
              roomId : room.roomId
            }));
          }
        });
      });

      deleteTd.append(deleteButton);
      titleTd.innerHTML =  room.roomName;
      tr.append(titleTd);
      tr.append(deleteTd);

      tbody.append(tr);
    }
}