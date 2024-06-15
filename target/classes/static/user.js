const API = {};

API.getAllUsers = () => fetch('http://localhost:8080/api/users')
    .then(response => response.json());

API.getUserById = (id) => fetch(`http://localhost:8080/api/users/${id}`)
    .then(response => response.json());

API.addUser = (user) => fetch('http://localhost:8080/api/users', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json;charset=utf-8'
    },

    body: JSON.stringify(user)
}).then(response => response.json());


API.editUser = (user, id) => fetch(`http://localhost:8080/api/users/${id}`, {
    method: 'PATCH',
    headers: {
        'Content-Type': 'application/json;charset=utf-8'
    },
    body: JSON.stringify(user)
}).then(response => response.json());

API.deleteUserById = (id) => fetch(`http://localhost:8080/api/users/${id}`, {
    method: 'DELETE',
});

API.getAllRoles = () => fetch('http://localhost:8080/api/roles')
    .then(response => response.json());

//DOM manipulations
const createUserListRow = (user) => {

    const row = $(`
    <tr id="user-row-${user.id}">
        <td><span>${user.name}</span></td>
        <td><span>${user.surname}</span></td>
        <td><span>${user.age}</span></td>
        <td><span>${user.email}</span></td>
        <td>
            <div>
                ${user.roles.map(role => role.title).join(", ")}
            </div>
        </td>
        <td>
            <div>
                <button type="button" class="btn btn-primary" data-toggle="modal" data-action="edit" data-user-id="${user.id}">
                    Edit
                </button>
            </div>
        </td>
        <td>
            <div>
                <button type="button" class="btn btn-danger" data-toggle="modal" data-action="delete" data-user-id="${user.id}">
                    Delete
                </button>
            </div>
        </td>
    </tr>
    `);
    $('#user-list-table tbody').append(row);
}

const fillUsersTable = users => users.forEach(createUserListRow);

const deleteUserListRow = (userId) => $(`#user-row-${userId}`).remove();


//Application
API.getAllUsers().then(fillUsersTable);

const roles = {};

API.getAllRoles().then(result => result.forEach(role => roles[role.name]=role));

const deleteUser = (id) => API.deleteUserById(id).then(()=>{
    deleteUserListRow(id);
})

let userId;

$('#user-list-table tbody').on("click", "button", (event) => {
    const button = $(event.target);
    const action = button.data('action');
    userId = button.data('user-id');
    API.getUserById(userId).then(user => {
        if (action === "delete") {
            $('#DELETE').modal('show');
            fillForm(user, $('#DELETE form'));
        }
        if (action === "edit") {
            $('#PATCH').modal('show');
            fillForm(user, $('#PATCH form'));
        }
    });
})

$('#DELETE .btn-danger').on("click", event => {
    event.preventDefault();
    deleteUser(userId).then(() => {
        $('#DELETE').modal('hide');
    })
})

$('#PATCH .btn-primary').on("click", event => {
    event.preventDefault();
    API.editUser(getDataFromForm($("#PATCH form")), userId).then(() => {
        API.getAllUsers().then(users => {
            $('#user-list-table tr').remove();
            fillUsersTable(users);
            $('#PATCH').modal('hide');
        })
    })
})

$('.btn-success').on("click", event => {
    event.preventDefault();
    API.addUser(getDataFromForm($("#new-user form"))).then(user => {
        createUserListRow(user);
        clearForm($("#new-user form"));
        $('[data-target="#users-table"]').click();
    })
})

const fillForm = (user, form ) => {
    form.find('[name="id"]').val(user.id);
    form.find('[name="name"]').val(user.name);
    form.find('[name="password"]').val(user.password);
    form.find('[name="surname"]').val(user.surname);
    form.find('[name="age"]').val(user.age);
    form.find('[name="email"]').val(user.email);
    form.find('[name="roles"]').val(user.roles.map(role => role.name));
}

const clearForm = (form) => {
    fillForm({roles:[]},form);
}

const getDataFromForm = (form) => {
    const data = {};
    form.find('input, textearea, select').each((_, field) => {
        const value = $(field).val();
        if (field.name === "roles") {
            data[field.name] = value.map(role => roles[role])
        } else {
            data[field.name] = value;
        }
    });
    return data;
}