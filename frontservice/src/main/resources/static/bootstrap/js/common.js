$(document).ready(function() {
    navbarNavActiveFunc();
})

function navbarNavActiveFunc() {
    //모든 리스트의 active class 제거
    $("ul.nav.navbar-nav > li").removeClass("active");
    let urlPath = window.location.pathname;
    $("ul.nav.navbar-nav > li").each(function() {
        if("/" + $(this).find("> a").text() == urlPath.toUpperCase()) {
            $(this).addClass("active");
        }
    });
}
