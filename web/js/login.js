function login() {
    if (findTextInputErrors()) return;

    let login = document.querySelector('#login').value;
    let password = document.querySelector('#password').value;

    let loginSpan = document.querySelector('#login-error');
    let passwordSpan = document.querySelector('#password-error');

    doReq('POST', '/rgr_cinema_storm_war_exploded/login', false, 'login=' + login + '&password=' + password, function(result) {
        switch (result) {
            case 'login-not-found':
                loginSpan.innerText = 'Логин не найден!';
                break;
            case 'password-not-correct':
                passwordSpan.innerText = 'Пароль не верный!';
                break;
            case 'success':
                window.location.replace('main.html');
                break;
        }
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