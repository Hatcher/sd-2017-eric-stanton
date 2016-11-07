var stickySidebar = $('.sticky');

if (stickySidebar.length > 0) {
    var stickyHeight = stickySidebar.height(),
        sidebarTop = stickySidebar.offset().top;
}

// on scroll move the sidebar
$(window).scroll(function () {
    if (stickySidebar.length > 0) {
        var scrollTop = $(window).scrollTop();

        if (sidebarTop < scrollTop) {
            stickySidebar.css('top', scrollTop - sidebarTop);
        }
        else {
            stickySidebar.css('top', '0');
        }
    }
});

$(window).resize(function () {
    if (stickySidebar.length > 0) {
        stickyHeight = stickySidebar.height();
    }
});