


angular.module('senseItWeb', null, null).controller('ProjectEditMenuCtrl', function ($scope, $state) {

    $scope.itemClass = function(state) {
        return $state.current.name === state ? 'pure-menu-selected' : '';
    };

    $scope.type = function(type) {
        return $scope.project.type == type;
    };


});

