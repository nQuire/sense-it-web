angular.module('senseItWeb', null, null).controller('ForumListCtrl', function ($scope) {

    $scope.forum.getList();

    if ($scope.status.profile && $scope.status.profile.admin) {
        $scope.form = new SiwFormManager(function() {
            if ($scope.form.getMode() === 'new')
        })
    }
    $scope.form

});