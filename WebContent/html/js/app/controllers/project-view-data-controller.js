angular.module('senseItWeb', null, null).controller('ProjectViewDataCtrl', function ($scope, ProjectDataService) {

    $scope.dataReady = false;
    $scope.tableData = {
        items: [],
        sort: {}
    };

    $scope.voteManager = {
        votingEnabled: true,
        getPath: function(target) {
            return 'api/project/' + $scope.project.id + '/' + $scope.project.type + '/data/vote/' + target.id;
        }
    };

    $scope.getTableTemplateURL = function () {
        switch ($scope.project.type) {
            case 'senseit':
                return 'partials/data-table-senseit.html';
        }

        return null;
    };


    ProjectDataService.loadData($scope.project.id, $scope.project.type).then(function (data) {
        $scope.tableData.items = data;
        $scope.dataReady = true;
    });

    $scope.createData = function() {
        ProjectDataService.createData($scope.project.id, $scope.project.type).then(function(data) {
            $scope.tableData.items = data;
            $scope.dataReady = true;
        });
    };

});