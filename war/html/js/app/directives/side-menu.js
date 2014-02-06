angular.module('senseItWeb', null, null).directive('siwSideMenu', function ($state) {
    return {
        templateUrl: 'html/partials/side-menu.html',

        controller: function($scope, $state) {
            var elements = $('#menuLink, #menu, #layout');

            $scope.state = $state;

            $scope.active = function(state) {
                return state === $scope.state.current.name;
            };

            $scope.open = function(state) {
                elements.removeClass('active');
                $state.go(state);
            }
        }
    };
});
