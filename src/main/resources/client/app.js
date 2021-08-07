var stompClient = null;

function setConnected(connected) {
  $("#connect").prop("disabled", connected);
  $("#disconnect").prop("disabled", !connected);
  if (connected) {
    $("#log").show();
  } else {
    $("#log").hide();
  }
  $("#message").html("");
}

function connect() {
  var socket = new SockJS('http://localhost:8080/demo');
  stompClient = Stomp.over(socket);
  stompClient.connect({}, function (frame) {
    setConnected(true);
    console.log('Connected: ' + frame);

    stompClient.subscribe('/app/status/1337', function (message) {
      showMessage(JSON.stringify(JSON.parse(message.body)));
    });

    stompClient.subscribe('/topic/status/1337', function (message) {
      showMessage(JSON.stringify(JSON.parse(message.body)));
    });
  });
}

function disconnect() {
  if (stompClient !== null) {
    stompClient.disconnect();
  }
  setConnected(false);
  console.log("Disconnected");
}

function showMessage(message) {
  $("#message").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
  $("form").on('submit', function (e) {
    e.preventDefault();
  });
  $("#connect").click(function () {
    connect();
  });
  $("#disconnect").click(function () {
    disconnect();
  });
});
