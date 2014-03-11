angular.module('senseItWeb', null, null).controller('ProjectViewSenseItAnalysisCtrl', function ($scope, $state, ProjectDataService) {

    $scope.analysis = $scope.analysisById($state.params['analysisId']);

    $scope.transformations = new SiwSenseItTransformations($scope.project.activity.profile.sensorInputs, $scope.analysis.tx);

    var save = function () {
        ProjectDataService.updateAnalysis($scope.project, $scope.analysis.id, {
            text: $scope.analysis.text,
            tx: $scope.analysis.tx
        }).then(function (data) {
                console.log(data);
            });
    };

    $scope.availableTransformations = {};
    for (var t in SiwSenseItSensorData.transformations) {
        if (SiwSenseItSensorData.transformations.hasOwnProperty(t)) {
            $scope.availableTransformations[t] = SiwSenseItSensorData.transformations[t].name;
        }
    }


    $scope.txForm = new SiwFormManager($scope.analysis, ['tx'], save, function() {
        $scope.txEdit.reset();
    });

    $scope.analysisForm = new SiwFormManager($scope.analysis, ['text'], save);

    $scope.txEdit = {
        tx: function () {
            return $scope.txForm.values.tx;
        },
        open: function() {
            $scope.txForm.open();
            this.updateFormValue();
        },
        updateFormValue: function () {
            $scope.transformations.setTx(this.tx());
        },
        reset: function () {
            $scope.transformations.setTx($scope.analysis.tx);
        },
        deleteVariable: function (variable) {
            if (variable.editable) {
                var index = this.indexOf(variable);
                if (index >= 0) {
                    this.tx().splice(index, 1);
                    this.updateFormValue();
                }
            }
        },
        createVariable: function (type) {
            this.tx().push({
                id: this.newTxId(),
                name: '',
                type: type,
                inputs: []
            });
            this.updateFormValue();
        },
        indexOf: function (variable) {
            var tx = this.tx();
            for (var i = 0; i < tx.length; i++) {
                if (tx[i].id = variable.tx) {
                    return i;
                }
            }
            return -1;
        },
        newTxId: function () {
            var id = 1;
            var tx = this.tx();
            for (var i = 0; i < tx.length; i++) {
                id = Math.max(id, 1 + parseInt(tx[i].id.substr(3)));
            }
            return "tx:" + id;
        },
        editable: function(variable) {
            return $scope.txForm.isOpen() && variable.editable;
        },
        setInputVariable: function(tx, vId, index) {
            tx.inputs[index] = vId;
            this.updateFormValue();
        }
    }
})
;