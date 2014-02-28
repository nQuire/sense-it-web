angular.module('senseItWeb', null, null).controller('ProjectViewDataCtrl', function ($scope, ProjectDataService) {

    $scope.dataReady = false;
    $scope.dataList = {
        items: [],
        sort: {}
    };

    $scope.analysisReady = false;
    $scope.analysisList = {
        items: [],
        sort: {},
        selected: null,
        selectCallback: function(item, button) {
            console.log(item);
            console.log(button);
        }
    };

    $scope.analysisView = {
        isOpen: false,
        item: null,
        open: function(item) {
            this.item = item;
            this.isOpen = true;
        },
        openNew: function() {
            this.item = null;
            this.isOpen = true;
        },
        close: function() {
            this.item = null;
            this.isOpen = false;
        }
    };


    $scope.dataVoteManager = {
        votingEnabled: true,
        getPath: function(target) {
            return 'api/project/' + $scope.project.id + '/' + $scope.project.type + '/data/vote/' + target.id;
        }
    };

    $scope.analysisVoteManager = {
        votingEnabled: true,
        getPath: function(target) {
            return 'api/project/' + $scope.project.id + '/' + $scope.project.type + '/analysis/vote/' + target.id;
        }
    };


    ProjectDataService.loadData($scope.project).then(function (data) {
        $scope.dataList.items = data;
        $scope.dataReady = true;
    });

    $scope.createData = function() {
        ProjectDataService.createData($scope.project).then(function(data) {
            $scope.dataList.items = data.items;
            $scope.dataReady = true;
        });
    };

    ProjectDataService.loadAnalysis($scope.project).then(function (data) {
        $scope.analysisList.items = data;
        $scope.analysisReady = true;
    });

    $scope.createAnalysis = function() {
        ProjectDataService.createAnalysis($scope.project, {}).then(function(data) {
            $scope.analysisList.items = data.items;
            $scope.analysisReady = true;
        });
    };


    $scope.analysisById = function(id) {
        for (var i = 0; i < $scope.analysisList.items.length; i++) {
            if ($scope.analysisList.items[i].id == id) {
                return $scope.analysisList.items[i];
            }
        }

        return null;
    };

});