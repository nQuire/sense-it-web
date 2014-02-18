angular.module('senseItWeb', null, null).controller('ProjectViewChallengeAnswersCtrl', function ($scope) {

    $scope.view = {
        itemOpen: false,
        open: function (answer) {
            $scope.itemView.answer = answer;
            this.itemOpen = true;
        },
        openById: function(answerId) {
            for (var i = 0; i < $scope.tableView.answers.length; i++) {
                if ($scope.tableView.answers[i].id == answerId) {
                    this.open($scope.tableView.answers[i]);
                    break;
                }
            }
        },
        openNew: function() {
            $scope.itemView.answer = false;
            this.itemOpen = true;
        },
        close: function () {
            this.itemOpen = false;
        },
        showItem: function () {
            return this.itemOpen;
        },
        showTable: function () {
            return !this.itemOpen;
        }
    };

});