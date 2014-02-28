angular.module('senseItWeb', null, null).controller('ProjectViewSenseItAnalysisCtrl', function ($scope, $state, ProjectDataService) {

    $scope.analysis = $scope.analysisById($state.params['analysisId']);

    $scope.analysisForm = new siwFormManager($scope.analysis, ['text'], function () {
        ProjectDataService.updateAnalysis($scope.project, $scope.analysis.id, {
            text: $scope.analysis.text,
            tx: []
        }).then(function (data) {
                console.log(data);
            });
    });

});