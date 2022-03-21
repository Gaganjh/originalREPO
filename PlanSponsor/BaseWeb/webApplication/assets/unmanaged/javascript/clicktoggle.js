
$.fn.clicktoggle = function(a, b) {
    return this.each(function() {
        var clicked = false;
        $(this).on("click",function() {
            if (clicked) {
                clicked = false;
                return b.apply(this, arguments);
            }
            clicked = true;
            return a.apply(this, arguments);
        });
    });
};
