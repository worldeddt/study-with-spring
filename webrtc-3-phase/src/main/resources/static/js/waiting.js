const ROOMCODE = "ferimRoom";
const HOST = "https://localhost:8081"
const ROOM_ALL_URI = "/room/all"
let roomList = [];

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
        console.log(e);
    })
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