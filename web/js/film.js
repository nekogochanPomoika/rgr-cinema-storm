(function () {
    getFilms();
})();

function getFilms() {
    //alert("The URL of this page is: " + window.location.href);
    doReq('POST', '/rgr_cinema_storm_war_exploded/get-back', true, getUrlArgs(), function (JSONStr) {
        let data = JSON.parse(JSONStr);
        document.querySelector("title").innerText = data.name;
        document.querySelector("#name").innerText = data.name;
        document.querySelector("#image").setAttribute('src', 'resources/images/filmsPosters/' + data.imageUrl);

        let mainInfo = document.querySelector("#main-info");
        let fields = data.fields;
        let keys = Object.keys(fields);
        keys.forEach(function (key) {
            if (fields[key] == 'null') return;

            let field = document.createElement('div');

            let fieldKey = document.createElement('span');
            fieldKey.setAttribute('class', 'field-key');
            fieldKey.innerText = key;
            field.appendChild(fieldKey);

            let fieldValue = document.createElement('span');
            fieldValue.setAttribute('class', 'field-value');
            fieldValue.innerText = fields[key];
            field.appendChild(fieldValue);

            mainInfo.appendChild(field);
        })

        let trailer = document.querySelector('#trailer');

        if (data.trailer == 'null') {
            let noVideoImage = document.createElement('img');
            noVideoImage.setAttribute('src', 'resources/images/logo-png.png');
            trailer.appendChild(noVideoImage);
        } else {
            //tut videa
        }

        document.querySelector('#description').innerText = data.description;

        //stars

        let ratingAndMarksCount = document.querySelector('#rating');

        let rating = document.createElement('span');
        rating.innerText = data.rating;
        ratingAndMarksCount.appendChild(rating);

        let marksCount = document.createElement('sub');
        marksCount.innerText = data.marksCount;
        ratingAndMarksCount.appendChild(marksCount);
    });
}

function getUrlArgs() {
    let args = window.location.href.split('?')[1];
    return args;
}
