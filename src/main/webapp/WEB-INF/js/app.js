//a channel to retrieve user specific data
const stompUserClient = new StompJs.Client({
    brokerURL: 'ws://' + window.location.host + '/websocket',
});

//a channel to subscribe to chats
const stompChatClient = new StompJs.Client({
    brokerURL: 'ws://' + window.location.host + '/websocket',
});

let currentChatId;

function getCookieValue(cookieName) {
    const cookies = document.cookie.split('; ');
    for (let cookie of cookies) {
        const [name, value] = cookie.split('=');
        if (name === cookieName) {
            return decodeURIComponent(value);
        }
    }
    return null;
}

stompUserClient.onConnect = (frame) => {
    console.log("Connected user channel: " + frame);
    stompUserClient.subscribe("/user/queue/reply", (message) => {
        let data = JSON.parse(message.body);
        console.log(data);
        $("#current-chat-name").text(`${data.name} (id: ${currentChatId})`);
        $("#current-chat-name-container").show();
        loadMessages(data.messages);
    })
};

stompChatClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompChatClient.subscribe("/topic/chat/" + currentChatId, (message) => {
        let data = JSON.parse(message.body);
        receiveMessage(data);
    }, {
        chatId: currentChatId
    });
};

stompUserClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
    stompUserClient.deactivate();
};

stompUserClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
    stompUserClient.deactivate();
};

stompChatClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
    stompChatClient.deactivate();
    setConnected(false);
};

stompChatClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
    stompChatClient.deactivate();
    setConnected(false);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $("#chat").html("");
}

function connect() {
    currentChatId = $("#chat-id-box").val();
    stompUserClient.activate();
    stompChatClient.activate();
    console.log("test");
    setConnected(true);
}

function disconnect() {
    stompChatClient.deactivate();
    currentChatId = "";
    setConnected(false);
    $("#current-chat-name-container").hide();
    console.log("Disconnected");
}

function createChat() {
    console.log("attempting to create a chat...");
    stompChatClient.deactivate();
    $.ajax({
        url: "/create-chat",
        type: "POST",
        headers: {
            "X-XSRF-TOKEN": getCookieValue("XSRF-TOKEN")
        },
        data: {
            name: $("#chat-name-box").val()
        },
        success: response => {
            currentChatId = response.id;
            stompUserClient.activate();
            stompChatClient.activate();
            setConnected(true);
        }
    })
}

function sendMessage() {
    stompChatClient.publish({
        destination: "/app/send-message",
        body: JSON.stringify({'contents': $("#message-box").val()})
    });
}

function loadMessages(messages) {
    for (let message of messages) {
        receiveMessage(message);
    }
}

function receiveMessage(message) {
    $("#chat").append(`
        <div class="message">
            <div class="message-info">
                <div class="username">
                    <h1>${message.senderName}</h1>
                </div>
                <div class="date">
                    <h1>${message.date}</h1>
                </div>
            </div>
            <div class="message-contents">
                <p>
                    ${message.contents}
                </p>
            </div>
        </div>`
    );
    let element = document.getElementById("chat");
    element.scrollTop = element.scrollHeight;
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#create-chat" ).click(() => createChat());
    $( "#send" ).click(() => sendMessage());

    stompUserClient.activate();
});