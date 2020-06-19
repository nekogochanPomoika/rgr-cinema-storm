(function() {
    let fields = document.querySelectorAll('textarea');
    fields.forEach(function (txt) {
        fn = function () {
            setTimeout(function () {
                txt.style.height = 'auto';
                txt.style.height = txt.scrollHeight + 'px';
            }, 0);
        };
        txt.addEventListener('keydown', fn, false);
    })
})();

//base request function, used in 999999% cases
function doReq(Type, url, async, par, func) {
    let xhr = new XMLHttpRequest();
    xhr.open(Type, url, async);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function() {
        if(xhr.readyState == 4) {
            if(xhr.status == 200) {
                func(xhr.responseText);
            } else {
                alert('Ajax error, status - ' + xhr.status);
            }
        }
    }
    xhr.send(par);
}

function getUrlArgs() {
    return window.location.href.split('?')[1];
}

//base info about user:
(function() {
    doReq('POST', '/rgr_cinema_storm_war_exploded/current-user-name-and-status', false, '', function(JSONStr) {

        let a = document.querySelector('#search-panel-button-anchor');
        let button = document.querySelector('#search-panel-button');
        let nav = document.querySelector('#navUl');

        if (JSONStr == "not-logged-in") {
            window.login = '';
            window.moderator = false;

            button.innerText = 'Вход';
            a.href = 'login.html';
        } else {
            let data = JSON.parse(JSONStr);

            window.login = data['login'];
            window.moderator = data['moderator'] === 'true';

            button.innerText = 'Мой аккаунт';
            a.href = 'user.html?login=' + window.login;

            nav.style.width = '800px';

            let logOut = document.createElement('a');
            logOut.href = '#';
            logOut.innerText = 'Выйти из аккаунта';
            logOut.onclick = function() {
                doReq('POST', '/rgr_cinema_storm_war_exploded/logout', false, '', function() {
                    location.reload();
                })
            }

            nav.appendChild(logOut);

            let fieldsForModerator = document.querySelectorAll('.forModerator');

            if (window.moderator) {
                fieldsForModerator.forEach(function (field) {
                    field.style.display = 'block';
                })
            }
        }
    });
})();

//for search panel and items-group
let bool = true;

function hideGroup() {
    let group = document.querySelector('#search-panel-group');
    if (bool) {
        group.style.display = 'none';
        group.style.borderWidth = '0px';
    }
    bool = true;
}

function showGroup() {
    let group = document.querySelector('#search-panel-group');
    group.style.display = 'block';
    group.style.borderWidth = '1px';
    bool = false;
}

function getGroupList() {
    let searchPanel = document.querySelector("#search-panel");
    let groupList = document.querySelector('#search-panel-group');

    groupList.innerHTML = '';

    if (searchPanel.value == '' || searchPanel.value.trim().length == 0) {
        groupList.innerText = 'Введите название фильма...';
        return;
    }

    doReq('POST', '/rgr_cinema_storm_war_exploded/for-search-panel', true, 'tag=' + searchPanel.value, function(JSONStr) {
        if (JSONStr == 'no result') {
            groupList.innerText = 'Поиск не дал результатов...';
            return;
        }

        let data = JSON.parse(JSONStr);

        data.forEach(function (element) {
            let tr = document.createElement('tr');

            let td = document.createElement('td');
            let img = document.createElement('img');

            let imgPath;
            if (element['imageUrl'] == 'null') imgPath = 'resources/images/filmsPosters/not-found.jpg';
            else imgPath = 'resources/images/filmsPosters/' + element['imageUrl'];

            img.src = imgPath;
            img.style.width = '50px';
            img.style.height = '100px';
            td.appendChild(img);

            tr.appendChild(td);

            td = document.createElement('td');
            let a = document.createElement('a');

            a.setAttribute('href', 'film.html?name=' + element['url']);
            a.innerText = element['name'];
            td.appendChild(a);

            tr.appendChild(td);

            groupList.appendChild(tr);
        });
    });
}