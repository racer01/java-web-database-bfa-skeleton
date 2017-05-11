'use strict';

$(document).ready(function () {
    loadTasks();
    $("#newForm").keyup(function (event) {
        if (event.keyCode == 13) {
            $("#addbtn").click();
        }
    });
});

function addTask() {
    let requiredItems = document.getElementById('newForm').querySelectorAll("[required]");
    let validForm = true;
    requiredItems.forEach(function (item) {
        if (item.value == '') {
            validForm = false;
        }
    });
    if (validForm) {
        $.post('todo', $('#newForm').serialize(), function (newTaskJSON) {
            let todoList = document.getElementById('todoList').innerHTML;
            const newTask = JSON.parse(newTaskJSON);
            document.getElementById('todoList').innerHTML = generateTaskHtml(newTask) + todoList;
            console.log(newTask);
            document.getElementById('newForm').reset();
            // loadTasks();
        });
    }
}

function removeTask(id) {
    $.ajax({
        url: `todo/${id}`,
        type: 'DELETE',
        success: function () {
            loadTasks();
        }
    });
}

function toggleTask(id) {
    $.ajax({
        url: `todo/${id}`,
        type: 'PUT',
        data: JSON.stringify({status: 'toggle'}),
        success: function () {
            loadTasks();
        }
    });
}

function generateTaskHtml(task) {
    let status = '';
    switch (task.status) {
        case 'NEW':
            status = '';
            break;
        case 'IN_PROGRESS':
            status = 'list-group-item-warning';
            break;
        case 'DONE':
            status = 'list-group-item-success';
            break;
    }
    let listElement = `<li class="list-group-item ${status}" id="${task.id}">`;
    listElement += `<button type="button" class="close listButtons" onclick="toggleTask(${task.id})">` +
        `<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>` +
        `</button>`;
    listElement += `<button type="button" class="close listButtons" onclick="removeTask(${task.id})">` +
        `<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>` +
        `</button>`;
    listElement += task.status == 'DONE' ? '<s>' : '';
    listElement += task.title != '' ? `<h4 class="list-group-item-heading">${task.title}</h4>` : '';
    listElement += `<p class="list-group-item-text">${task.content}</p>`;
    listElement += task.status == 'DONE' ? '</s>' : '';
    listElement += `</li>\n`;
    return listElement;
}

function loadTasks() {
    $.getJSON("todo", function (data) {
        let htmlList = '';
        if (data.length > 0) {
            data.forEach(function (task) {
                htmlList = generateTaskHtml(task) + htmlList;
            });
        }
/*
        else {
            htmlList = '<li class="list-group-item list-group-item-info">';
            htmlList += 'You did it! Good job!';
            htmlList += '</li>\n';
        }
*/
        document.getElementById('todoList').innerHTML = htmlList;
    });
}
