angular.module('senseItWeb', null, null).directive('siwMenuItem', function () {
    return {
        templateUrl: 'partials/widgets/menu-item.html',
        scope: {
            'menuActiveState' : '=?',
            'menuLinkedState': '=',
            'menuLabel': '='
        },
        controller: function ($scope, $state) {
            $scope.menuActiveState = $scope.menuActiveState ? $scope.menuActiveState : $scope.menuLinkedState;
            $scope.isActive = function() {
                return $state.current.name.lastIndexOf($scope.menuActiveState, 0) === 0;
            }
        }
    };
});
