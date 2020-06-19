let page = 1;
let orderBy = 'rating/marks_count';
let desc = true;

(function() {
    let button = document.querySelector('#films-req');

    if (window.login == '') {
        button.style.display = 'none';
    } else if (!window.moderator) {
        button.innerText = 'Отправить заявку на добавление фильма';
    } else {
        button.innerText = 'Просмотреть присланные заявки';
    }

    doReq('GET', '/rgr_cinema_storm_war_exploded/get-films-count', false, '', function(count) {
        let pagesCount = Math.trunc((count - 1) / 10);
        let pagesLinks = document.querySelector('#table-page');

        for (i = 1; i <= pagesCount + 1; i++) {
            let a = document.createElement('a');
            a.innerText = i + ' ';
            a.href = '#';

            a.setAttribute('onclick', 'setPage(' + i + ')');

            pagesLinks.appendChild(a);
        }
    });
    updateTable();
})();

function setPage(thisPage) {
    page = thisPage;
    updateTable();
}

function setOrder(thisOrder) {
    if (orderBy == thisOrder) desc = !desc;
    else desc = true;
    orderBy = thisOrder;
    page = 1;
    updateTable();
}

function updateTable() {
    doReq('POST', '/rgr_cinema_storm_war_exploded/main-page-get-table', true, 'page=' + page + '&orderBy=' + orderBy + '&desc=' + desc, function(JSONStr) {
        let data = JSON.parse(JSONStr);

        let tbody = document.querySelector('#films-table');

        tbody.innerHTML = '';

        data.forEach(function (element, index) {
            let tr = document.createElement('tr');

            let td = document.createElement('td');
            td.innerText = index + 1;

            tr.appendChild(td);

            td = document.createElement('td');
            let a = document.createElement('a');
            a.innerText = element['name'];
            a.href = 'film.html?name=' + element['url'];
            td.appendChild(a);

            tr.appendChild(td);

            td = document.createElement('td');
            td.innerText = element['rating'];

            tr.appendChild(td);

            td = document.createElement('td');
            td.innerText = element['marksCount'];

            tr.appendChild(td);

            tbody.appendChild(tr);
        });
    })
}