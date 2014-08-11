angular.module('senseItWeb', null, null).controller('NavBarCtrl', function ($scope, $state, $stateParams) {

    $scope.params = $stateParams;

    $scope.$on('$destroy', $scope.$watch('params', function() {
        $scope.keyword = $scope.params.kw || null;
    }, true));

    $scope.active = function (state) {
        return $state.current.name.indexOf(state) === 0;
    };

    $scope.search = function() {
        $scope.params.kw = $scope.keyword || null;
        $state.go('home', $scope.params);
    };

    $scope.searchButtonAction = function() {
        if ($scope.params.kw) {
            $scope.keyword = "";
        }

        $scope.search();
    };
});
