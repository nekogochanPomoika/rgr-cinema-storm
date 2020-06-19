let filmId;
let filmMark;

(function () {
    getFilm();
    getMark();
    getStars();
})();

function getFilm() {
    doReq('POST', '/rgr_cinema_storm_war_exploded/get-film', false, getUrlArgs(), function (JSONStr) {
        let data = JSON.parse(JSONStr);
        document.querySelector('title').innerText = data['name'];
        document.querySelector('#name').innerText = data['name'];
        filmId = data['id'];

        let imgDiv = document.querySelector('#img-div');
        let image = document.querySelector('#image');
        let mainInfo = document.querySelector('#main-info');
        let mainInfoDiv = document.querySelector('#main-info-div');

        image.setAttribute('src', 'resources/images/filmsPosters/' + data['imageUrl']);

        image.onload = function() {
            if (image.height / image.width > 1.2) {
                imgDiv.style.float = 'left';
                image.style.width = '400px';
                image.style.margin = '15px'
                mainInfoDiv.style.marginLeft = '30px';
            } else {
                imgDiv.style.width = '100%';
                image.style.display = 'block';
                image.style.width = '700px';
                image.style.margin = '30px auto';
                mainInfoDiv.style.width = '600px';
                mainInfoDiv.style.margin = 'auto';
            }
        }

        let fields = data['fields'];
        let keys = Object.keys(fields);

        keys.forEach(function (key) {
            if (fields[key] == 'null') return;

            let tr = document.createElement('tr');

            let td = document.createElement('td');
            td.innerText = key;
            td.style.padding = '10px';
            td.style.flexWrap = 'wrap';
            tr.appendChild(td);

            td = document.createElement('td');
            td.innerText = fields[key];
            td.style.paddingLeft = '100px';
            tr.appendChild(td);

            mainInfo.appendChild(tr);
        })

        let trailer = document.querySelector('#trailer');

        if (data['trailerUrl'] == 'null') {
            let noVideoImage = document.createElement('img');
            noVideoImage.setAttribute('src', 'resources/images/video-trailer-no-found.png');
            noVideoImage.width = 640;
            noVideoImage.margin = 'auto';
            noVideoImage.style.display = 'block';
            trailer.appendChild(noVideoImage);
        } else {
            let video = document.createElement('video');
            video.width = 640;
            video.height = 400;
            video.margin = 'auto';
            video.innerText = 'Ваш бразуер не поддерживает html5 video';
            video.style.display = 'block';
            video.style.margin = 'auto';

            let source = document.createElement('source');
            source.src = 'resources/videos/' + data['trailerUrl'];
            video.appendChild(source);

            trailer.appendChild(video);
        }

        let description = document.querySelector('#description');
        description.style.flexWrap = 'wrap';
        description.innerText = data['description'];

        let ratingAndMarksCount = document.querySelector('#rating');

        let rating = document.createElement('span');
        rating.innerText = data['rating'];
        ratingAndMarksCount.appendChild(rating);

        let marksCount = document.createElement('sub');
        marksCount.innerText = data['marksCount'];
        ratingAndMarksCount.appendChild(marksCount);
    });
}

function getMark() {
    doReq('POST', '/rgr_cinema_storm_war_exploded/get-mark', false, 'filmId=' + filmId, function(resp) {
        if (resp === "not-logged-in") {
            filmMark = 0;
        } else {
            filmMark = resp;
        }
    });
}

function addMark(mark) {
    doReq('POST', '/rgr_cinema_storm_war_exploded/add-mark', true, 'mark=' + mark + '&filmId=' + filmId, function(resp) {
        if (resp === "not-logged-in") {
            window.location.replace('login.html');
            alert('Чтобы добавить оценку, нужно войти в аккаунт или зарегестрироваться');
        } else {
            filmMark = mark;
            getStars();
        }
    })
}

function getStars() {
    let stars = document.querySelector('#stars');
    stars.innerHTML = '';

    for (let i = 1; i <= 10; i++) {
        let star = document.createElement('img');
        if (i <= filmMark) star.setAttribute('src', 'resources/images/star1.png');
        else star.setAttribute('src', 'resources/images/star0.png');

        star.setAttribute('onclick', 'addMark(' + i + ')');

        stars.append(star);
    }
}

function getComments() {
    doReq('POST', '/rgr_cinema_storm_war_exploded/get-comments', true, 'filmId=' + filmId, function(JSONStr) {
        let comments = document.querySelector('#comments');
        comments.innerHTML = '';

        if (JSONStr == 'no result') {
            comments.innerText = 'Нет комментариев';
        } else {
            let data = JSON.parse(JSONStr);

            data.forEach(function (comment) {
                let com = document.createElement('div');
                com.className = 'firstLvlComment';
                com.style.padding = '10px';

                let button;
                if (window.moderator || window.login === comment['user']) {
                    button = document.createElement('button');
                    button.setAttribute('onclick', 'deleteComment(' + comment['id'] + ')');
                    button.innerText = 'Удалить комментарий';
                    com.appendChild(button);
                }

                com.appendChild(document.createElement('br'));

                let span = document.createElement('span');
                span.hidden = true;
                span.className = 'id';
                span.innerText = comment['id'];
                com.appendChild(span);

                let author = document.createElement('a');
                author.className = 'author';
                author.innerText = comment['user'];
                author.href = 'user.html?login=' + comment['user'];
                com.appendChild(author);

                let header = document.createElement('h3');
                header.innerText = comment['header'];
                com.appendChild(header);

                let text = document.createElement('p');
                text.innerText = comment['data'];
                com.appendChild(text);

                let mark = document.createElement('p');
                mark.className = 'mark';
                mark.innerText = comment['rating'];
                com.appendChild(mark);

                if (comment['rating'] < 4) {
                    com.style.background = "rgba(120,13,13,0.3)";
                } else if (comment['rating'] < 7) {
                    com.style.background = "rgba(109,109,109,0.3)";
                } else {
                    com.style.background = "rgba(20,120,13,0.3)";
                }

                let hr = document.createElement('hr');
                com.appendChild(hr);

                let input = document.createElement('input');
                input.id = 'addComment' + comment['id'];
                com.appendChild(input);

                span = document.createElement('span');
                span.id = input.id + '-error';
                com.appendChild(span);

                com.appendChild(document.createElement('br'));

                button = document.createElement('button');
                button.innerText = 'Добавить комментарий';
                button.setAttribute('onclick', 'addComment2(' + comment['id'] + ')');
                com.appendChild(button);

                comments.appendChild(com);

                let lvl2Comments = document.createElement('div');

                if (comment['comments2'] !== 'no result') {
                    comment['comments2'].forEach(function (comment2) {
                        let com2 = document.createElement('div');
                        com2.className = "secondLvlComment";

                        let span2 = document.createElement('span');
                        span2.hidden = true;
                        span2.className = 'id2';
                        span2.innerText = comment2['id'];
                        com2.appendChild(span2);

                        let author2 = document.createElement('a');
                        author2.className = 'author';
                        author2.innerText = comment2['user'];
                        author2.href = 'user.html?login=' + comment2['user'];
                        com2.appendChild(author2);

                        if (window.moderator || window.login === comment2['user']) {
                            let button2 = document.createElement('button');
                            button2.setAttribute('onclick', 'deleteComment2(' + comment2['id'] + ')');
                            button2.innerText = 'Удалить комментарий';
                            button2.style.marginLeft = '100px';
                            com2.appendChild(button2);
                        }

                        let text2 = document.createElement('p');
                        text2.innerText = comment2['data'];
                        com2.appendChild(text2);

                        lvl2Comments.appendChild(com2);
                    });

                    lvl2Comments.appendChild(document.createElement('br'))
                    comments.appendChild(lvl2Comments);
                }
            });
        }
    });
}

function newComment() {
    let newComment = document.querySelector('#newComment');

    newComment.style.display = 'block';
}

function addComment() {
    let title = document.querySelector('#comment-title').value;
    let rating = document.querySelector('#comment-rating').value;
    let text = document.querySelector('#comment-text').value;

    if (findTitleErrors(title) || findRatingErrors(rating) || findTextErrors(text)) return;

    doReq('POST', '/rgr_cinema_storm_war_exploded/add-comment', true,
        'title=' + title + '&rating=' + rating + '&text=' + text + "&filmId=" + filmId, function(resp) {
        if (resp == "not-logged-in") {
            window.location.replace('login.html');
            alert('Чтобы добавить комментарий, нужно войти в аккаунт или зарегестрироваться');
        } else {
            alert('Комментарий добавлен!');
            document.querySelector('#newComment').style.display = 'none';
            getComments();
        }
    })
}

function addComment2(commentId) {
    let text = document.querySelector('#addComment' + commentId).value;
    let span = document.querySelector('#addComment' + commentId + '-error');

    span.innerText = '';

    if (text == null || text.trim().length == 0) {
        span.innerText = 'Пустое поле!';
        return;
    }

    doReq('POST', '/rgr_cinema_storm_war_exploded/add-second-lvl-comment', true,
        'text=' + text + '&commentId=' + commentId, function(resp) {
            if (resp == "not-logged-in") {
                window.location.replace('login.html');
                alert('Чтобы добавить комментарий, нужно войти в аккаунт или зарегестрироваться');
            } else {
                alert('Комментарий добавлен!');
                getComments();
            }
        })
}

function findTitleErrors(title) {
    let span = document.querySelector('#comment-title-error');
    span.innerText = '';
    if (title == null || title.trim().length == 0) {
        span.innerText = 'Пустое поле!';
        return true;
    } else if (title.length > 100) {
        span.innerText = 'Длина поля должна быть меньше ста!';
        return true;
    }
    return false;
}

function findRatingErrors(rating) {
    let span = document.querySelector('#comment-rating-error');
    span.innerText = '';
    if (Number.isInteger(rating)) {
        span.innerText = 'Поле не является целым числом!';
        return true;
    } else if (rating > 10 || rating < 1) {
        span.innerText = 'Оценка должна быть в диапазоне от 1 до 10';
        return true;
    }
    return false;
}

function findTextErrors(text) {
    let span = document.querySelector('#comment-text-error');
    span.innerText = '';
    if (text == null || text.trim().length == 0) {
        span.innerText = 'Пустое поле!';
        return true;
    }
    return false;
}

function deleteFilm() {
    doReq('POST', '/rgr_cinema_storm_war_exploded/delete-film', false, 'filmId=' + filmId, function(resp) {
        if (resp == 'no result') {
            alert('Недостаточно прав!');
        } else {
            window.location.replace('main.html');
        }
    })
}

function deleteComment(id) {
    doReq('POST', '/rgr_cinema_storm_war_exploded/delete-film', false, 'id=' + id, function(resp) {
        if (resp == 'no result') {
            alert('Нужно обладать правами администратора!');
        } else {
            alert('Комментарий удален!');
            getComments();
        }
    })
}

function deleteComment2(id) {
    doReq('POST', '/rgr_cinema_storm_war_exploded/delete-film', false, 'id=' + id, function(resp) {
        if (resp == 'no result') {
            alert('Нужно обладать правами администратора!');
        } else {
            alert('Комментарий удален!');
            getComments();
        }
    })
}

function changeFilmInfo() {
    doReq('POST', '/rgr_cinema_storm_war_exploded/set-updated-film', true, 'id=' + filmId, function(resp) {
        if (resp == 'no result') {
            alert('Недостаточно прав!');
        } else {
            window.location.replace('add-film.html');
        }
    })
}