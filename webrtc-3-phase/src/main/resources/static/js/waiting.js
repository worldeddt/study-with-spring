
const ROOMCODE = "ferimRoom";
let roomList = [];

const apiRequestGet = (_url) => {
    axios.get(_url)
        .then(function (response) {

            window.roomList = roomList;
            response.data.list.map(property => {
                roomList.push(JSON.parse(property));
            })

            settingListBody();

        }).catch(function(reason) {
            console.log("reason", reason);
    });
}

function settingListBody() {
    const tbody = document.getElementById("room-body");

    for (room of roomList) {
        console.log(room);
        let tr= document.createElement('tr');
        let td= document.createElement('td');

        td.innerHTML =  room.roomName;
        tr.append(td);

        tbody.append(tr);
    }
}


apiRequestGet(`https://localhost:8081/room/all/${ROOMCODE}`);
