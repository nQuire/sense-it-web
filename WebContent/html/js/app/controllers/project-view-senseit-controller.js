angular.module('senseItWeb', null, null).controller('ProjectViewSenseItCtrl', function ($scope) {

    $scope.templates.home = 'partials/project-view-senseit-home.html';
    $scope.templates.dataTable = 'partials/data-table-senseit.html';
    $scope.templates.analysis = 'partials/project-view-senseit-analysis.html';
    $scope.templates.analysisTable = 'partials/data-analysis-table-senseit.html';

    $scope.dataInfo = {
        type: 'senseit',
        tableTemplateURL: 'partials/data-table-senseit.html'
    };

});