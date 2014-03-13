angular.module('senseItWeb', null, null).controller('ProjectViewSenseItCtrl', function ($scope) {

    $scope.templates.home = 'partials/project-view-senseit-home.html';
    $scope.templates.dataTable = 'partials/data-table-senseit.html';
    $scope.templates.analysis = 'partials/project-view-senseit-analysis.html';
    $scope.templates.analysisTable = 'partials/data-analysis-table-senseit.html';

    $scope.transformations = new SiwSenseItTransformations($scope.project.activity.profile.sensorInputs, $scope.project.activity.profile.tx);

    $scope.dataInfo = {
        type: 'senseit',
        tableTemplateURL: 'partials/data-table-senseit.html',
        tableVariables: $scope.transformations.nonSequenceVariables(),
        plotVariables: $scope.transformations.sequenceVariables()
    };


});