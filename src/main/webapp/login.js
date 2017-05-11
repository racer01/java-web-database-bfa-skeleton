$(document).ready(function () {
    $("#loginform").keyup(function (event) {
        if (event.keyCode == 13) {
            $("#loginbtn").click();
        }
    });
});

function login() {
    const passObj = $('#loginpass');
    const pass = passObj.val();
    const hashedPass = sha1(pass);
    console.log(hashedPass);
    passObj.val(hashedPass);
    $('#loginform').submit();
}

function register() {
    const passObj = $('#regpass');
    const pass = passObj.val();
    const hashedPass = sha1(pass);
    console.log(hashedPass);
    passObj.val(hashedPass);
    $('#regform').submit();
}

