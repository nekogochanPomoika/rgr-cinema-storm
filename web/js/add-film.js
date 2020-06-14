(function() {
    let fields = document.querySelectorAll('textarea');
    fields.forEach(function (txt, i) {
        fn = function () {
            setTimeout(function () {
                txt.style.height = 'auto';
                txt.style.height = txt.scrollHeight + 'px';
            }, 0);
        };
        txt.addEventListener('keydown', fn, false);
    })
})();

function addFilm() {
    let returning;

    returning = (findTextInputErrors() || findIntInputErrors() || findCategoriesError() || findUrlInputError());

    if (returning) return;

    doReq('POST', '/rgr_cinema_storm_war_exploded/add-film-base-info', true, getBaseAttributes(), function(){});

    sendFiles();
}

function sendFiles() {
    let image = document.querySelector('#image');
    let video = document.querySelector('#video');

    let data = new FormData();

    data.append('file', image.files[0]);

    let request = new XMLHttpRequest();
    request.open('post', '/rgr_cinema_storm_war_exploded/upload-servlet', false);

    request.send(data);

    let data2 = new FormData();

    data2.append('file', video.files[0]);

    let request2 = new XMLHttpRequest();
    request2.open('post', '/rgr_cinema_storm_war_exploded/upload-servlet', false);

    request2.send(data2);
}

function findTextInputErrors() {
    let findError = false;
    let textInputs = document.querySelectorAll('.text-input');
    textInputs.forEach(function (element) {
        let span = document.querySelector('#' + element.getAttribute('id') + '-error');

        span.innerText = '';

        if (element.classList.contains('mandatory')) {
            if (element.value.length == null || element.value.trim().length == 0) {
                span.innerText = 'Поле пустое!';
                findError = true;
            }
        }
    })
    return findError;
}

function findIntInputErrors() {
    let findError = false;
    let intFields = document.querySelectorAll('.int-input');

    intFields.forEach(function (element) {
        let span = document.querySelector('#' + element.getAttribute('id') + '-error');
        span.innerText = '';

        if (element.value != null) {
            if (isNaN(element.value)) {
                span.innerText = 'Поле не является числом!';
                findError = true;
            }
        }
    })
    return findError;
}

function findUrlInputError() {
    let url = document.querySelector('#url').value;
    let findError = false;
    let span = document.querySelector('#url-error');
    span.innerText = '';

    if (url == null || url.trim().length == 0) {
        span.innerText = 'Поле пустое!';
        return true;
    }

    doReq('POST', '/rgr_cinema_storm_war_exploded/check-url-uniqueness', false, url, function(resp) {
        if (resp == 'url-not-unique') {
            findError = true;
            span.innerText = 'url не является уникальным!';
        }
    });

    return findError;
}

function getBaseAttributes() {
    let str = '';
    str += 'name='
    str += document.querySelector('#name').value

    let fields = document.querySelectorAll('.field');

    fields.forEach(function (field) {
        str += '&';
        str += field.getAttribute('id');
        str += '=';
        str += field.value;
    });

    let categories = document.querySelectorAll('select');

    str += '&category=';
    str += categories[0].value;

    for (i = 1; i < categories.length; i++) {
        str += ',' + categories[i].value;
    }
    return str;
}

var n = 1;

function addField() {
    if (n > 3) return;

    let root = document.querySelector('#select-fields-root');

    let div = document.createElement('div');
    div.setAttribute('id', n);

    let newField = document.createElement('select');
    let newFieldOption = document.createElement('option');

    newFieldOption.innerText = 'Дополнительная категория';
    newFieldOption.disabled = true;

    newField.appendChild(newFieldOption);

    div.appendChild(newField);

    let deleteField = document.createElement('button');
    deleteField.innerText = '-----';
    deleteField.setAttribute('onclick', 'deleteField(' + n + ')');
    div.appendChild(deleteField);

    let hiddenSpan = document.createElement('span');
    hiddenSpan.setAttribute('class', 'select-value');
    hiddenSpan.hidden = true;
    div.appendChild(hiddenSpan);

    root.appendChild(div);

    getSelectOptions(newField);
    newField.addEventListener('click', function(){
        if (change) getSelectOptions(newField);
        change = true;
    });
    newField.addEventListener('change', function(){setTextToSpan(newField)});

    n++;
}

function deleteField(n) {
    let element = document.getElementById(n);
    element.parentNode.removeChild(element);
    window.n--;
}

let change = true;

function getSelectOptions(select) {
    let allOptions = [
        'биография',
        'боевик',
        'вестерн',
        'военный',
        'детектив',
        'документальный',
        'драма',
        'история',
        'комедия',
        'короткометражка',
        'криминал',
        'мелодрама',
        'музыка',
        'мультфильм',
        'мюзикл',
        'приключения',
        'семейный',
        'сериал',
        'спорт',
        'триллер',
        'ужасы',
        'фантстика',
        'нуар',
        'фэнтези'
    ];

    let selectedOptions = document.getElementsByClassName('select-value');

    select.innerHTML = '';
    allOptions.forEach(function (option) {
        for (i = 0; i < selectedOptions.length; i++) {
            if (option == selectedOptions[i].innerText) return;
        }
        let newOption = document.createElement('option');
        newOption.innerText = option;
        newOption.setAttribute('value', option);
        select.appendChild(newOption);
    })

    select.value = select.parentNode.lastChild.innerText;
}

function setTextToSpan(select) {
    change = false;
    select.parentNode.lastChild.innerText = select.value;
}

function findCategoriesError() {
    let errorSpan = document.querySelector('#categories-error');
    errorSpan.innerText = '';

    if (n === 1) {
        errorSpan.innerText = 'Не выбрана ни одна категория!';
    }
}