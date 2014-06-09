angular.module('senseItWeb', null, null).directive('siwSideMenuButton', function () {
    return {
        template: '<span></span>',

        link: function (scope, element, attrs) {
            var button = element;
            var menu = $('#menu');
            var layout = $('#layout');

            button.addClass('menu-link');

            button.click(function () {
                button.toggleClass('active');
                menu.toggleClass('active');
                layout.toggleClass('active');
            });
        }
    };
});
