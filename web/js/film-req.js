(function() {
    let main = document.querySelector('main');
    alert(!(window.moderator) + ' ' + window.moderator + ' ' + !window.moderator + ' ' + window.moderator);
    if (window.login == '') {
        alert(1);
        main.innerHTML = '<h1>Для получения доступа к заявкам необходимо войти в аккаунт</h1>';
    } else if (!window.moderator) {
        main.innerHTML = '<h1>Отправить заявку на добавление фильма:</h1>\n' +
            '    <hr>\n' +
            '    <p>Название:</p>\n' +
            '    <input id="name" class="text-input"><span class="error" id="name-error"></span><br>\n' +
            '\n' +
            '    <p>Год:</p>\n' +
            '    <input id="year" class="text-input"><span class="error" id="year-error"></span><br>\n' +
            '\n' +
            '    <button onclick="filmReq()">Продолжить</button>\n' +
            '    <br>';
    } else {
        let h1 = document.createElement('h1');
        h1.innerText = 'Заявки на добавление фильмов';
        main.appendChild(h1);
        doReq('POST', '/rgr_cinema_storm_war_exploded/get-film-reqs', false, '', function(JSONStr) {
            if (JSONStr === 'no result') {
                let p = document.createElement('p');
                p.innerText = 'Заявок нет...';
                main.appendChild(p);
            } else {
                let data = JSON.parse(JSONStr);

                data.forEach(function (element) {
                    let div = document.createElement('div');
                    div.className = 'film-req';
                    div.id = 'film-req-' + element['id'];

                    let userId = document.createElement('p');
                    userId.innerText = element['userLogin'];
                    div.appendChild(userId);

                    let name = document.createElement('p');
                    name.innerText = element['name'];
                    div.appendChild(name);

                    let year = document.createElement('p');
                    year.innerText = element['year'];
                    div.appendChild(year);

                    let p = document.createElement('p');
                    p.innerText = 'После обработки заявок рекомендуется их удалять:';
                    div.appendChild(p);

                    div.appendChild(document.createElement('br'));

                    let button = document.createElement('button');
                    button.setAttribute('onclick', 'deleteFilmReq(' + element['id'] + ')');
                    button.innerText = 'Удалить заявку';
                    div.appendChild(button);

                    data.appendChild(div);
                })
            }
        })
    }
})();

function filmReq() {
    let name = document.querySelector('#name').value;
    let year = Number(document.querySelector('#year').value);

    if (findNameErrors(name) || findYearErrors(year)) return;

    doReq('POST', '/rgr_cinema_storm_war_exploded/send-film-req', true,
        'name=' + name + '&year=' + year, function(resp) {
        if (resp === 'not-logged-in') {
            window.location.replace('login.html');
            alert('Чтобы добавить заявку, нужно войти в аккаунт или зарегестрироваться');
        } else {
            alert('Заявка отправлена');
        }
    })
}

function deleteFilmReq(id) {
    doReq('POST', '/rgr_cinema_storm_war_exploded/delete-film-req', true, 'id=' + id, function(resp) {
        if (resp === 'no-moderator') {
            alert('Недостаточно прав');
        } else {
            let filmReq = document.querySelector('film-req-' + id);

            filmReq.parentNode.removeChild(filmReq);
        }
    })
}

function findNameErrors(name) {
    let span = document.querySelector('#name-error');
    span.innerText = '';

    if (name == null || name.trim().length == 0) {
        span.innerText = 'Пустое поле!';
        return true;
    }
    return false;
}

function findYearErrors(year) {
    let span = document.querySelector('#year-error');
    span.innerText = '';

    if (year == null || year.trim().length == 0) {
        span.innerText = 'Пустое поле!';
        return true;
    } else if (!Number.isInteger(year)) {
        span.innerText = 'Поле не является целым числом!';
        return true;
    } else if (year < 1901 || year > 2155) {
        span.innerText = 'Год должен находиться в диапазоне от 1901 до 2155';
        return true;
    }
    return false;
}
