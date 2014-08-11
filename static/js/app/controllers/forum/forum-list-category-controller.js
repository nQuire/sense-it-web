angular.module('senseItWeb', null, null).controller('ForumListCategoryCtrl', function ($scope) {

    if ($scope.adminAccess) {
        $scope.isNew = !$scope.category;
        if ($scope.isNew) {
            $scope.category = {};
        }

        $scope.form = new SiwFormManager(function () {
            return $scope.category;
        }, ['title', 'metadata'], function () {
            if ($scope.isNew) {
                $scope.forum.createCategory($scope.category);
            } else {
                $scope.forum.updateNode($scope.category);
            }
        });
    }
});