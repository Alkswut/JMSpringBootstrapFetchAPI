// const name = alex
// let age = 29
//
//
// console.log(age)

userInformation();

function userInformation(user) {
    fetch('http://localhost:8080/user/getUser')
        .then(response => response.json())
        .then(user => {
            let tBody = document.getElementById("userLoginFun");
            tBody.innerHTML = "";

            let row = tBody.insertRow(0);
            let cell0 = row.insertCell(0);
            cell0.innerHTML = user.id;
            let cell1 = row.insertCell(1);
            cell1.innerHTML = user.firstName;
            let cell2 = row.insertCell(2);
            cell2.innerHTML = user.lastName;
            let cell3 = row.insertCell(3);
            cell3.innerHTML = user.age;
            let cell4 = row.insertCell(4);
            cell4.innerHTML = user.username;
            let cell5 = row.insertCell(5);
            cell5.innerHTML = listRoles(user).textContent;
        });
}

