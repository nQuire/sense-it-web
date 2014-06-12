angular.module('senseItWeb', null, null).controller('ProjectEditSenseItAnalysisCtrl', function ($scope) {

    $scope.availableTransformations = {};
    for (var t in SiwSenseItSensorData.transformations) {
        if (SiwSenseItSensorData.transformations.hasOwnProperty(t)) {
            $scope.availableTransformations[t] = SiwSenseItSensorData.transformations[t].name;
        }
    }

    $scope.txForm = new SiwFormManager(function () {
        return $scope.projectData.project.activity.profile;
    }, ['tx'], function () {
        $scope.senseitEditor.updateProfile($scope.projectData.project.activity.profile);
    }, function () {
        $scope.txEdit.reset();
    });

    $scope.txEdit = {
        tx: function () {
            return $scope.txForm.values.tx;
        },
        open: function () {
            $scope.txForm.open();
            this.updateFormValue();
        },
        updateFormValue: function () {
            $scope.transformations.setTx(this.tx());
        },
        reset: function () {
            $scope.transformations.setTx($scope.project.activity.profile.tx);
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
            var id = 1;
            var weight = 0;
            var tx = this.tx();
            if (tx) {
                for (var i = 0; i < tx.length; i++) {
                    id = Math.max(id, 1 + parseInt(tx[i].id.substr(3)));
                    weight = Math.max(weight, 1 + tx[i].weight);
                }
            }

            this.tx().push({
                id: "tx:" + id,
                weight: weight,
                name: '',
                type: type,
                inputs: []
            });

            this.updateFormValue();
        },
        indexOf: function (variable) {
            var tx = this.tx();
            for (var i = 0; i < tx.length; i++) {
                if (tx[i].id == variable.tx.id) {
                    return i;
                }
            }
            return -1;
        },
        editable: function (variable) {
            return $scope.txForm.isOpen() && variable.editable;
        },
        setInputVariable: function (tx, vId, index) {
            tx.inputs[index] = vId;
            this.updateFormValue();
        }
    }
});