angular.module('senseItWeb', null, null).controller('ProjectViewDataCtrl', function ($scope, ProjectDataService, ModalService) {

    $scope.dataReady = false;
    $scope.dataList = {
        items: [],
        sort: {
            'title': function (a, b) {
                return siwCompare.string(a.title, b.title);
            },
            'author': function (a, b) {
                return siwCompare.string(a.author.username, b.author.username);
            },
            'votes': function (a, b) {
                return siwCompare.voteCount(a.voteCount, b.voteCount);
            }
        }
    };

    $scope.dataVoteManager = {
        votingEnabled: true,
        getPath: function (target) {
            return 'api/project/' + $scope.projectData.project.id + '/' + $scope.projectData.project.type + '/data/vote/' + target.id;
        }
    };


    $scope.dataService = ProjectDataService.dataService($scope.projectWatcher);
    $scope.dataService.loadData().then(function (data) {
        $scope.dataList.items = data;
        $scope.dataReady = true;
    });

    $scope.updateData = function (itemId, data) {
        $scope.dataService.updateData(data).then(function (updatedData) {
            if (updatedData) {
                for (var index = 0; index < $scope.dataList.items.length; index++) {
                    if ($scope.dataList.items[index].id == updatedData.id) {
                        $scope.dataList.items[index] = updatedData;
                        break;
                    }
                }
            }
            $scope.dataReady = true;
        });
    };

    $scope.deleteData = function (data) {
        ModalService.open({
            title: 'Delete data',
            bodyTemplate: 'partials/project/view/data/project-view-data-delete-dlg.html',
            item: data,
            ok: function () {
                $scope.dataService.deleteData(data.id).then(function (deletedDataId) {
                    if (deletedDataId) {
                        for (var index = 0; index < $scope.dataList.items.length; index++) {
                            if ($scope.dataList.items[index].id == deletedDataId) {
                                $scope.dataList.items.splice(index, 1);
                                break;
                            }
                        }
                    }
                    $scope.dataReady = true;
                });
            }
        });
    };

});