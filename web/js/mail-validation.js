(function() {
    doReq('POST', '/rgr_cinema_storm_war_exploded/register-end', true, getUrlArgs(), function(resp) {
        document.querySelector('#msg').innerText = resp;
    });
})();

function getUrlArgs() {
    let args = window.location.href.split('?')[1];
    alert(args);
    return args;
}