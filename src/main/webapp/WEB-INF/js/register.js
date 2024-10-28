function disableRegister() {
    $("#register").prop("disabled", true);
    timer = setTimeout(enableRegister, 3000);
}

function enableRegister() {
    $("#register").prop("disabled", false);
}

// $(() => {
//     $("#register").click(disableRegister);
// })