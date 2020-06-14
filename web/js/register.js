function sendRegisterData() {
    let returning;

    returning = (findTextInputErrors() || findEmailErrors() || findPasswordErrors());

    if (returning) return;

    doReq('POST', '/rgr_cinema_storm_war_exploded/register-get-info', false, getArgs(), function () {
        window.location.replace('register-email-sent.html');
    });
}

function findTextInputErrors() {
    let findError = false;
    let textInputs = document.querySelectorAll('.text-input');

    textInputs.forEach(function (element) {
        let span = document.querySelector('#' + element.getAttribute('id') + '-error');
        span.innerText = '';

        if (element.value.length == null || element.value.trim().length == 0) {
            span.innerText = 'Поле пустое!';
            findError = true;
        }
    })
    return findError;
}

function findEmailErrors() {
    let re = /\S+@\S+\.\S+/;
    let mail = document.querySelector('#mail');
    let span = document.querySelector('#mail-error');
    span.innerText = '';

    if (!re.test(mail.value)) {
        span.innerText = 'Почта должна соответствовать стандарту str@str.str';
        return true;
    }
    return false;
}

function findPasswordErrors() {
    let password = document.querySelector('#password');
    let passwordConfirm = document.querySelector('#password_confirm');
    let PCspan = document.querySelector('#password_confirm-error');
    PCspan.innerText = '';

    if (password.value != passwordConfirm.value) {
        PCspan.innerText = 'Пароли не совпадают!';
        return true;
    }
    return false;
}

function getArgs() {
    let str = '';
    str += 'login='
    str += document.querySelector('#login').value

    let fields = document.querySelectorAll('.field');

    fields.forEach(function (field) {
        str += '&';
        str += field.getAttribute('id');
        str += '=';
        str += field.value;
    });

    return str;
}