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
        plotVariables: $scope.transformations.sequenceVariables(),
        tableValue: function(item, variable) {
            return item.varValue[variable.id].v[0].toPrecision(3)
        }
    };


    if ($scope.project.activity.profile.geolocated) {
        $scope.mapData = {
            mapVariables: $scope.dataInfo.tableVariables.map(function(v) {
                return {id: v.id, label: v.label(), weight: v.weight};
            }),
            value: function(item, v) {
                return item.varValue[v.id].v[0];
            },
            location: function(item) {
                return JSON.parse(item.geolocation);
            }
        };
    }


    $scope.plots = {
        _open: {},
        plotOpen: function(item) {
            return !! this._open[item.id];
        },
        togglePlot: function(item) {
            this._open[item.id] = ! this._open[item.id];
        },
        url: function(item, v) {
            return 'api/project/' + $scope.project.id + '/senseit/data/' + item.id + '/' + v.id + '.png';
        }
    }


});