angular.module('senseItWeb', null, null).controller('ProjectViewAnalysisCtrl', function ($scope, ProjectDataService) {

    $scope.dataReady = false;
    $scope.tableData = {
        items: [],
        sort: {}
    };

    $scope.voteManager = {
        votingEnabled: true,
        getPath: function(target) {
            return 'api/project/' + $scope.project.id + '/' + $scope.project.type + '/analysis/vote/' + target.id;
        }
    };

    $scope.getAnalysisTableTemplate = function () {
        switch ($scope.project.type) {
            case 'senseit':
                return 'partials/data-analysis-table-senseit.html';
        }

        return null;
    };


    ProjectDataService.loadAnalysis($scope.project.id, $scope.project.type).then(function (data) {
        $scope.tableData.items = data;
        $scope.dataReady = true;
    });

    $scope.createAnalysis = function() {
        ProjectDataService.createAnalysis($scope.project.id, $scope.project.type).then(function(data) {
            $scope.tableData.items = data;
            $scope.dataReady = true;
        });
    };

});