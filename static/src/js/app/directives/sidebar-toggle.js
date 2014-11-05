angular.module('senseItWeb').directive('siwSidebarToggle', function () {
    return {
        link: function (scope, element) {
            var sidebar = $(element);
            var button = $('<div></div>').addClass('sidebar-toggle');
            button.append('<i class="fa fa-angle-right"></i>');
            button.append('<i class="fa fa-angle-left"></i>');
            sidebar.append(button);

            button.click(function() {
                sidebar.toggleClass('sidebar-open');
            });
        }
    };
});
