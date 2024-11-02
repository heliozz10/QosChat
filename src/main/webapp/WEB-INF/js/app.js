const stompClient = new StompJs.Client({
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

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe("/topic/chat/" + currentChatId, (message) => {
        receiveMessage(JSON.parse(message.body));
    }, {
        chatId: currentChatId
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
    stompClient.deactivate();
    setConnected(false);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
    stompClient.deactivate();
    setConnected(false);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $("#chat").html("");
}

function connect() {
    currentChatId = $("#chat-id-box").val();
    stompClient.activate();
    setConnected(true);
}

function disconnect() {
    stompClient.deactivate();
    currentChatId = "";
    setConnected(false);
    console.log("Disconnected");
}

function createChat() {
    console.log("attempting...");
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
            stompClient.activate();
            setConnected(true);
        }
    })
}

function sendMessage() {
    stompClient.publish({
        destination: "/app/send-message",
        body: JSON.stringify({'contents': $("#message-box").val()})
    });
}

function receiveMessage(message) {
    $("#chat").append(`
        <div class="message">
            <div class="message-info">
                <div class="username">
                    <h1>${message.sender.name}</h1>
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
});