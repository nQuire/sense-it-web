angular.module('senseItWeb', null, null).controller('ProjectViewSenseItCtrl', function ($scope) {

    $scope.templates.menu = 'partials/project/view/senseit/senseit-view-menu.html';
    $scope.templates.dataTable = 'partials/project/view/senseit/data-table-senseit.html';


    $scope.transformations = new SiwSenseItTransformations($scope.projectData.project.activity.profile.sensorInputs, $scope.projectData.project.activity.profile.tx);

    $scope.dataInfo = {
        type: 'senseit',
        tableVariables: $scope.transformations.nonSequenceVariables(),
        plotVariables: $scope.transformations.sequenceVariables(),
        tableValue: function (item, variable) {
            return item.varValue[variable.id].v[0].toPrecision(3)
        }
    };

    $scope.edit = {
        selected: null,
        open: function(item) {
            this.selected = item;
            $scope.form.open(item.id);
        },

        update: function() {
            $scope.updateData(this.selected.id, {title: this.selected.title});
        }
    };

    $scope.form = new SiwFormManager(function() {
        return $scope.edit.selected;
    }, ['title'], function() {
        $scope.edit.update();
    });


    var createSortFunction = function (id) {
        return function (a, b) {
            return a.varValue[id].v[0] - b.varValue[id].v[0];
        };
    };

    for (var i = 0; i < $scope.dataInfo.tableVariables.length; i++) {
        var id = $scope.dataInfo.tableVariables[i].id;
        $scope.dataList.sort[id] = createSortFunction(id);
    }


    if ($scope.projectData.project.activity.profile.geolocated) {
        $scope.mapData = {
            mapVariables: $scope.dataInfo.tableVariables.map(function (v) {
                return {id: v.id, label: v.label(), weight: v.weight};
            }),
            value: function (item, v) {
                return item.varValue[v.id].v[0];
            },
            location: function (item) {
                try {
                    return JSON.parse(item.geolocation);
                } catch (e) {
                    console.log(e);
                    console.log(item.geolocation);
                }
                return null;
            }
        };
    }


    $scope.plots = {
        _open: {},
        plotOpen: function (item) {
            return !!this._open[item.id];
        },
        togglePlot: function (item) {
            this._open[item.id] = !this._open[item.id];
        },
        url: function (item, v) {
            return 'api/project/' + $scope.projectData.project.id + '/senseit/data/' + item.id + '/' + v.id + '.png';
        }
    }


});