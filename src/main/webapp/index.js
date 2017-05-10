'use strict';

$(document).ready(function () {
    loadTasks();
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
        $.post('todo', $('#newForm').serialize());
        document.getElementById('newForm').reset();
        loadTasks();
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
        data: JSON.stringify({done: 'toggle'}),
        success: function () {
            loadTasks();
        }
    });
}

function loadTasks() {
    $.getJSON("todo", function (data) {
        let htmlList = '';
        if (data.length > 0) {
            data.forEach(function (task) {
                const done = task.done ? 'disabled' : '';
                let listElement = `<li class="list-group-item ${done}" id="${task.id}">`;
                listElement += `<button type="button" class="close listButtons" onclick="toggleTask(${task.id})">` +
                    `<span class="glyphicon glyphicon-ok" aria-hidden="true"></span>` +
                    `</button>`;
                listElement += `<button type="button" class="close listButtons" onclick="removeTask(${task.id})">` +
                    `<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>` +
                    `</button>`;
                listElement += task.done ? '<s>' : '';
                listElement += task.title != '' ? `<h4 class="list-group-item-heading">${task.title}</h4>` : '';
                listElement += `<p class="list-group-item-text">${task.content}</p>`;
                listElement += task.done ? '</s>' : '';
                listElement += `</li>\n`;
                htmlList = listElement + htmlList;
            });
        } else {
            htmlList = '<li class="list-group-item list-group-item-info">';
            htmlList += 'You did it! Good job!';
            htmlList += '</li>\n';
        }
        document.getElementById('todoList').innerHTML = htmlList;
    });
}
