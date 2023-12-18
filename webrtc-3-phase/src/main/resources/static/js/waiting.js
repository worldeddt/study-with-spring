
const ROOMCODE = "fermiRoom";

const apiRequestGet = (_url) => {
    axios.get(_url)
        .then(function (response) {
            console.log(response);
        });
}


apiRequestGet(`https://localhost:8081/room/all/${ROOMCODE}`);