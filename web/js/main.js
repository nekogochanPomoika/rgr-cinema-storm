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

//for search panel and items-group


let bool = true;

function hideGroup() {
    alert(bool);
    let group = document.querySelector('#search-panel-group');
    if(bool)
        group.style.display = 'none';
    bool = true;
}

function showGroup() {
    let group = document.querySelector('#search-panel-group');
    group.style.display = 'block';
    bool = false;
    alert(bool);
}

function getGroupList() {
    let searchPanel = document.querySelector("#search-panel");
    let groupList = document.querySelector('#search-panel-group');

    doReq('GET', '/rgr_cinema_storm_war_exploded/for-search-panel', true, searchPanel.value, function() {
        
    });
}