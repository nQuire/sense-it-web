


angular.module('senseItWeb', null, null).controller('ProjectFilterSidebarCtrl', function ($scope, $state) {

    $scope.menuItemClass = function(key, value) {
        return $state.params[key] == value ? 'selected' : '';
    };

});

