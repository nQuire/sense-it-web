angular.module('senseItWeb', null, null).controller('ProjectViewDataCtrl', function ($scope, ProjectDataService, SortedDataService, ModalService, $state) {

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
            },
            'date': function (a, b) {
                return a.date - b.date;
            }
        },
        filters: []
    };

    $scope.sortedData = SortedDataService.get(function () {
        return $scope.dataList.items;
    }, $scope.dataList.sort, null, $scope, 'dataList.items', false);

    $scope.sortedData.sort('date', false);


    $scope.dataVoteManager = {
        votingEnabled: function () {
            return $scope.status.logged && $scope.projectData.access.member && $scope.projectData.project.open;
        },
        getPath: function (target) {
            return 'api/project/' + $scope.projectData.project.id + '/' + $scope.projectData.project.type + '/data/vote/' + target.id;
        }
    };


    $scope.dataService = ProjectDataService.dataService($scope.projectWatcher);
    $scope.dataService.loadData().then(function (data) {
        $scope.dataReady = false;
        $scope.dataList.items = data;

        var last = null;
        var ts = 0;
        for (var i = 0; i < $scope.dataList.items.length; i++) {
            if ($scope.dataList.items[i].date > ts) {
                last = $scope.dataList.items[i];
                ts = last.date;
            }
        }

        $scope.dataList.last = last;
        $scope.dataReady = true;

        return last;
    });

    $scope.updateData = function (itemId, data) {
        return $scope.dataService.updateData(itemId, data).then(function (updatedData) {
            if (updatedData) {
                for (var index = 0; index < $scope.dataList.items.length; index++) {
                    if ($scope.dataList.items[index].id == updatedData.id) {
                        $scope.dataList.items[index] = updatedData;
                        break;
                    }
                }
            }
            $scope.dataReady = true;

            return updatedData;
        });
    };

    $scope.createData = function (data, files, convertToMultipart) {
        $scope.dataService.uploadData(data, files, convertToMultipart).then(function (updatedData) {
            if (updatedData && updatedData.newItemId) {
                $scope.dataReady = false;
                $scope.dataList.items = updatedData.items;
                $scope.dataReady = true;
                $state.go('project.view.data-list', {item: updatedData.newItemId});
            }
        });
    };

    $scope.deleteData = function (data, goto) {
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

                        if (goto) {
                            $state.go(goto);
                        }
                    }
                    $scope.dataReady = true;
                });
                return true;
            }
        });
    };

    $scope.goToItem = function (id) {
        $state.go('project.view.data-item', {itemId: id});
    };

});