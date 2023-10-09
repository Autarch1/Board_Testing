$(document).ready(function () {
    // Initialize sortable list for main tasks
    $(".main-task-list").sortable({
        connectWith: ".main-task-list",
        placeholder: "ui-state-highlight",
        update: function (event, ui) {
            saveTaskOrder();
        }
    });

    // Initialize sortable list for subtasks
    $(".subtasks").sortable({
        connectWith: ".subtasks",
        placeholder: "ui-state-highlight",
        update: function (event, ui) {
            const mainTaskId = $(this).closest(".main-task").data("task-id");
            console.log("Main Task ID:", mainTaskId); // Log the mainTaskId to the console
            saveSubtaskOrder(ui.item, mainTaskId);
        }
    }).disableSelection();

    // Make subtasks draggable between main tasks
    $(".sortable").sortable({
        connectWith: ".sortable",
        placeholder: "ui-state-highlight",
        receive: function (event, ui) {
            const subtaskId = ui.item.data("subtask-id");
            const newMainTaskId = $(this).closest(".main-task").data("task-id");
            moveSubtask(subtaskId, newMainTaskId);
        }
    }).disableSelection();
});




function addMainTask() {
    const mainTaskInput = $("#main-task-input");
    const mainTaskText = mainTaskInput.val().trim();
    if (mainTaskText === "") {
        return;
    }

    // Send the main task to the server via AJAX
    $.ajax({
        url: "/api/tasks/addMainTask",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ name: mainTaskText }),
        success: function (response) {
            console.log("IN MAIN TASK " + response.id);

            // Main task saved successfully, you can update the UI or perform other actions here if needed
            const mainTaskItem = $("<li>")
                .addClass("ui-state-default")
                .html(`<h2>${mainTaskText} <img src="/image/icon.png" alt=""></h2>
                    <input type="text" class="subtask-input" data-main-task-id="${response.id}" placeholder="Add a Subtask" >
                    <button onclick="addSubtask(this)">Add Subtask</button>
                    <ul class="sortable subtasks">
                        <!-- Subtasks for this main task will be dynamically added here using JavaScript -->
                    </ul>`);

            // Store the task ID in the DOM
            mainTaskItem.data("task-id", response.id);

            // Update the data-main-task-id attribute of the subtask input
            mainTaskItem.find(".subtask-input").data("main-task-id", response.id);

            // Append the main task item to the list
            $("#main-task-list").append(mainTaskItem);

            // Make the subtasks sortable within this main task's subtasks list
            const subtaskList = mainTaskItem.find(".subtasks").sortable({
                connectWith: ".subtasks",
                placeholder: "ui-state-highlight",
                update: function (event, ui) {
                    const mainTaskId = mainTaskItem.data("task-id");
                    console.log("Main Task ID:", mainTaskId);
                    saveSubtaskOrder(ui.item, mainTaskId);
                }
            }).disableSelection();

            // Store the main task ID in the subtask input data attribute
            subtaskList.prev(".subtask-input").data("main-task-id", response.id);

            // Clear the main task input
            mainTaskInput.val("");

            // Save the task order
            saveTaskOrder();
        }
    });
}


function addSubtask(button) {
    const subtaskInput = $(button).prev(".subtask-input");
    const subtaskText = subtaskInput.val().trim();
    if (subtaskText === "") {
        return;
    }

    // Declare mainTaskId in the scope of the function
    let mainTaskId;

    // Get the main task ID from the subtask input's data attribute
    mainTaskId = subtaskInput.data("main-task-id");
    console.log(" LEE IS : " + mainTaskId)

    // Find the subtask list within the same main task item
    const mainTaskItem = $(button).closest("li.ui-state-default");
    const subtaskList = mainTaskItem.find("ul.sortable.subtasks");

    const subtaskItem = $("<li>")
        .addClass("ui-state-default")
        .text(subtaskText)
        .attr("data-subtask-id", generateSubtaskId());

    subtaskList.append(subtaskItem);

    subtaskInput.val("");
    saveTaskOrder();
    console.log("IN MAIN TASK " + mainTaskId);

    // Send the subtask to the server via AJAX with the associated main task ID
    $.ajax({
        url: `/api/tasks/addSubtask?mainTaskId=${mainTaskId}`, // Include mainTaskId in the URL
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ name: subtaskText }),
        success: function (response) {
            // Subtask saved successfully, you can update the UI or perform other actions here if needed
            subtaskItem.data("subtask-id", response.id); // Store the subtask ID in the DOM
        }
    });
}



function generateSubtaskId() {
    return "subtask-" + new Date().getTime();
}

function moveSubtask(subtaskId, newMainTaskId) {
    // Find the subtask element by its unique ID
    const subtask = $(`[data-subtask-id="${subtaskId}"]`);

    // Find the new main task by its unique ID
    const newMainTask = $(`[data-task-id="${newMainTaskId}"]`);

    // Append the subtask to the new main task's subtasks list
    newMainTask.find(".subtasks").append(subtask);

    saveTaskOrder();
}
function saveTaskOrder() {
    const taskOrder = [];
    $(".main-task-list").each(function () {
        const mainTaskId = $(this).closest(".main-task").data("task-id");
        taskOrder.push(mainTaskId);

        const subtaskOrder = $(this).find(".subtasks").sortable("toArray", { attribute: "data-subtask-id" });
        updateSubtaskOrder(mainTaskId, subtaskOrder);
    });

    // Send the updated task order to the server via AJAX
    $.ajax({
        url: "/api/tasks/updateTaskOrder",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(taskOrder),
        success: function () {
            // Task order saved successfully
        }
    });
}

function saveSubtaskOrder(subtask, mainTaskId) {
    const subtaskId = subtask.data("subtask-id");

    // Send the updated subtask order and main task ID to the server via AJAX
    $.ajax({
        url: "/api/tasks/updateSubtaskOrder",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({
            mainTaskId: mainTaskId,
            subtaskId: subtaskId
        }),
        success: function () {
            // Subtask order saved successfully
        }
    });

    // Function to validate mainTaskId
    function validateMainTaskId(mainTaskId) {
        fetch(`/validateMainTaskId?mainTaskId=${mainTaskId}`)
            .then(response => response.json())
            .then(data => {
                if (data.isValid) {
                    // MainTaskId is valid, proceed with your task
                    // You can add your logic here
                    alert("MainTaskId is valid!");
                } else {
                    // MainTaskId is not valid, display an error message
                    alert("MainTaskId is not valid!");
                }
            })
            .catch(error => {
                console.error('Error:', error);
            });
    }

// Example usage: Call validateMainTaskId on a button click


}