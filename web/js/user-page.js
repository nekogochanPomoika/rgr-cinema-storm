let page = 1;
let orderBy = 'mark';
let desc = true;
let userId;

(function() {
    doReq('POST', '/rgr_cinema_storm_war_exploded/get-user-info', false, getUrlArgs(), function(JSONStr) {
        let user = JSON.parse(JSONStr);

        document.querySelector('#user-name').innerText = user['name'];

        userId = user['id'];
    });

    doReq('POST', '/rgr_cinema_storm_war_exploded/get-user-marks-count', false, 'userId=' + userId, function(count) {
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
    doReq('POST', '/rgr_cinema_storm_war_exploded/user-page-get-table', true,
        'userId=' + userId + '&page=' + page + '&orderBy=' + orderBy + '&desc=' + desc, function(JSONStr) {
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
            td.innerText = element['mark'];

            tr.appendChild(td);

            tbody.appendChild(tr);
        });
    })
}

function deleteUser() {
    doReq('POST', '/rgr_cinema_storm_war_exploded/delete-film', false, 'userId=' + userId, function(resp) {
        if (resp == 'no result') {
            alert('Нужно обладать правами администратора!');
        } else {
            alert('Комментарий удален!');
            getComments();
        }
    })
}