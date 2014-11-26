angular.module('senseItWeb', null, null).controller('ForumListCategoryCtrl', function ($scope, ModalService) {

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

        $scope.deleteForumCategory = function (category) {
            ModalService.open({
                body: '<p>Are you sure you want forum group "' + category.title + '"?</p>' +
                '<p>This will remove all the forums and threads in it.</p>',
                title: 'Delete forum',
                ok: function () {
                    $scope.forum.deleteNode(category.id);
                    return true;
                }
            });
        };

    }
});