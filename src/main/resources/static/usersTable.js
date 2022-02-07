userTable();

function userTable() {
    let tBody = document.getElementById("UsersTableFun");
    tBody.innerHTML = "";
    fetch('http://localhost:8080/admin/getListUsers')
        .then(response => response.json())
        .then(users => {
            users.forEach(function (user) {
                let row = tBody.insertRow();
                row.setAttribute("id", user.id);
                let cell0 = row.insertCell();
                cell0.innerHTML = user.id;
                let cell1 = row.insertCell();
                cell1.innerHTML = user.firstName;
                let cell2 = row.insertCell();
                cell2.innerHTML = user.lastName;
                let cell3 = row.insertCell();
                cell3.innerHTML = user.age;
                let cell4 = row.insertCell();
                cell4.innerHTML = user.username;
                let cell5 = row.insertCell();
                cell5.innerHTML = listRoles(user).textContent;

                let cell6 = row.insertCell();
                cell6.innerHTML =
                    '<button type="button" class="btn btn-primary btn-sm" data-toggle="modal" onclick="getModalEdit(' + user.id + ')" >Edit</button>';

                let cell7 = row.insertCell();
                cell7.innerHTML =
                    '<button type="button" class="btn btn-danger btn-sm" data-toggle="modal" onclick="getModalDelete(' + user.id + ')" >Delete</button>';
            })
        });
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function getModalEdit(id) {

    fetch('http://localhost:8080/admin/getUserById/' + id)
        .then(response => response.json())
        .then(user => {

            let adminSelect = "";
            let userSelect = "";

            for (let i = 0; i < user.roles.length; i++) {
                if (user.roles[i].role === "ROLE_ADMIN") {
                    adminSelect = "selected";
                }
                if (user.roles[i].role === "ROLE_USER") {
                    userSelect = "selected";
                }
            }

            let modal = document.getElementById('modalWindow');
            modal.innerHTML =
                '<div id="modalEdit" class="modal fade" tabIndex="-1" role="dialog" ' +
                     'aria-labelledby="exampleModalLabel" aria-hidden="true" ' +
                     'data-backdrop="static" data-keyboard="false">' +
                    '<div class="modal-dialog" role="document"> ' +
                        '<div class="modal-content"> ' +
                            '<div class="modal-body"> ' +
                                '<form id="formEditUser" style="width: 200px;" ' +
                                    'class="form-signin mx-auto font-weight-bold text-center"> ' +
                                    '<div class="form-group"> ' +
                                        '<label >ID</label> ' +
                                        '<input id = "editedId" type="text" class="form-control" name="id" value="' + user.id + '" readOnly> ' +
                                    '</div> ' +
                                    '<div class="form-group"> ' +
                                        '<label>firstName</label> ' +
                                        '<input id = "editedFirstName" type="text" class="form-control" name="firstName" value="' + user.firstName + '" placeholder="First name.." required> ' +
                                    '</div> ' +
                                    '<div class="form-group"> ' +
                                        '<label>lastName</label> ' +
                                        '<input id = "editedLastName" type="text" class="form-control" name="lastName" value="' + user.lastName + '" placeholder="Last name.." required> ' +
                                    '</div> ' +
                                    '<div class="form-group"> ' +
                                        '<label>Age</label> ' +
                                        '<input id = "editedAge" type="number" class="form-control" name="age" value="' + user.age + '" placeholder="Age.." required> ' +
                                    '</div> ' +
                                    '<div class="form-group"> ' +
                                        '<label>Email</label> ' +
                                        '<input id = "editedUserName" type="email" class="form-control" name="username" value="' + user.username + '" placeholder="Email.." required> ' +
                                    '</div> ' +
                                    '<div class="form-group"> ' +
                                        '<label>Password</label> ' +
                                        '<input id = "editedPassword" type="password" class="form-control" name="password" value="' + user.password + '" placeholder="Password.." required> ' +
                                    '</div> ' +

                                    '<div class="form-group"> ' +
                                        '<label for="sel3m">Role</label> ' +
                                        '<select id = "editedRoles" name="authorities" multiple class="form-control" size="2" aria-readonly="true"> ' +
                                            '<option value="ROLE_ADMIN"' + adminSelect + ' >ADMIN</option> ' +
                                            '<option value="ROLE_USER"' + userSelect + '>USER</option> ' +
                                        '</select> ' +
                                    '</div> ' +
                                '</form> ' +
                            '</div> ' +
                            '<div class="modal-footer"> ' +
                                '<button type="button" class="btn btn-secondary" ' +
                                    'data-dismiss="modal">Close ' +
                                '</button> ' +
                                '<button type="button" class="btn btn-primary" ' +
                                    'data-dismiss="modal" ' +
                                    'onclick="editUser()">Edit ' +
                                '</button> ' +
                            '</div> ' +
                        '</div> ' +
                    '</div> ' +
                '</div>';
            $("#modalEdit").modal();
        });
}

function editUser() {

    let form = window.formEditUser.editedRoles;
    let newRoles = "";

    let rolesList = document.createElement('ul');

    for (let i = 0; i < form.length; i++) {
        let option = form.options[i];
        let role = document.createElement('li');
        if (option.selected) {
            newRoles = newRoles.concat(option + (i < (form.length - 1) ? "," : ""));
            role.textContent = option.value.substring(5, option.value.length) + " ";
            rolesList.appendChild(role);
        }
    }

    let id = window.formEditUser.editedId.value;

    fetch('http://localhost:8080/admin/userEdit', {
        method: 'PUT',
        body: JSON.stringify({
            id: window.formEditUser.editedId.value,
            firstName: window.formEditUser.editedFirstName.value,
            lastName: window.formEditUser.editedLastName.value,
            age: window.formEditUser.editedAge.value,
            username: window.formEditUser.editedUserName.value,
            password: window.formEditUser.editedPassword.value,
            roles: newRoles
        }),
        headers: {"Content-type": "application/json; charset=UTF-8"}
    })
        .then(response => {
            $('#' + id).replaceWith('<tr id=' + id + '>' +
                '<td>' + id + '</td>' +
                '<td>' + window.formEditUser.editedFirstName.value + '</td>' +
                '<td>' + window.formEditUser.editedLastName.value + '</td>' +
                '<td>' + window.formEditUser.editedAge.value + '</td>' +
                '<td>' + window.formEditUser.editedUserName.value + '</td>' +
                '<td>' + rolesList.textContent + '</td>' +
                '<td> <button type="button" onclick="getModalEdit(' + id + ')" class="btn btn-primary btn-sm">Edit</button> </td>' +
                '<td> <button type="button" onclick="getModalDelete(' + id + ')" class="btn btn-danger btn-sm">Delete</button> </td>' +
                '</tr>');
        });
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
function getModalDelete(id) {

    fetch('http://localhost:8080/admin/getUserById/' + id)
        .then(response => response.json())
        .then(user => {

            let adminSelect = "";
            let userSelect = "";

            for (let i = 0; i < user.roles.length; i++) {
                if (user.roles[i].role == "ROLE_ADMIN") {
                    adminSelect = "selected";
                }
                if (user.roles[i].role == "ROLE_USER") {
                    userSelect = "selected";
                }
            }

            let modal = document.getElementById('modalWindow');
            modal.innerHTML =
                '<div id="modalDelete" class="modal fade" tabIndex="-1" role="dialog" aria-labelledby="TitleModalLabel" aria-hidden="true" ' +
                    'data-backdrop="static" data-keyboard="false"> ' +
                    '<div class="modal-dialog" role="document"> ' +
                        '<div class="modal-content"> ' +
                            '<div class="modal-body"> ' +
                                '<form id="formDeleteUser" style="width: 200px;" ' +
                                    'class="form-signin mx-auto font-weight-bold text-center"> ' +
                                    '<div class="form-group"> ' +
                                        '<label id="id2" class="d-flex justify-content-center font-weight-bold">ID</label> ' +
                                        '<input type="text" id="id2" class="form-control" value="' + user.id + '" readOnly> ' +
                                    '</div> ' +
                                    '<div class="form-group"> ' +
                                        '<label id="firstname2" class="d-flex justify-content-center font-weight-bold">First Name</label> ' +
                                        '<input type="text" id="firstname2" class="form-control" value="' + user.firstName + '" readOnly> ' +
                                    '</div> ' +
                                    '<div class="form-group"> ' +
                                        '<label id="lastname2" class="d-flex justify-content-center font-weight-bold">Last Name</label> ' +
                                        '<input type="text" id="lastname2" class="form-control" value="' + user.lastName + '" readOnly> ' +
                                    '</div> ' +
                                    '<div class="form-group"> ' +
                                        '<label id="age2" class="d-flex justify-content-center font-weight-bold">Age</label> ' +
                                        '<input type="number" id="age2" class="form-control" value="' + user.age + '" readOnly> ' +
                                    '</div> ' +
                                    '<div class="form-group"> ' +
                                        '<label id="email2" class="d-flex justify-content-center font-weight-bold">Email</label> ' +
                                        '<input type="email" id="email2" class="form-control" value="' + user.username + '" readOnly> ' +
                                    '</div> ' +
                                    '<div class="form-group"> ' +
                                        '<label id="password2" class="d-flex justify-content-center font-weight-bold">Password</label> ' +
                                        '<input type="password" id="password2" class="form-control" value="' + user.password + '" readOnly> ' +
                                    '</div> ' +
                                    '<div class="form-group"> ' +
                                        '<label for="sel3m">Role</label> ' +
                                        '<select multiple class="form-control" size="2" aria-readonly="true"> ' +
                                           '<option value="ADMIN"' + adminSelect + ' >ADMIN</option> ' +
                                           '<option value="USER"' + userSelect + '>USER</option> ' +
                                       '</select> ' +
                                    '</div> ' +
                                '</form> ' +
                            '</div> ' +
                            '<div class="modal-footer"> ' +
                                '<button type="button" class="btn btn-secondary" ' +
                                        'data-dismiss="modal">Close ' +
                                '</button> ' +
                                '<button class="btn btn-danger" data-dismiss="modal" ' +
                                        'onclick="deleteUser(' + user.id + ')">Delete ' +
                                '</button> ' +
                            '</div> ' +
                        '</div> ' +
                    '</div> ' +
                '</div>';

            $("#modalDelete").modal();

        });
}

function deleteUser(id) {
    fetch('http://localhost:8080/admin/delete/' + id, {
        method: 'DELETE',
        headers: {"Content-type": "application/json; charset=UTF-8"}
    })
        .then(response => {
            $('#' + id).remove();
        });
}