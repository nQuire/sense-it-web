


angular.module('senseItWeb', null, null).controller('DataProjectMenuCtrl', function ($scope, $state) {

    $scope.itemClass = function(state) {
        return $state.current.name === state ? 'pure-menu-selected' : '';
    };

});

