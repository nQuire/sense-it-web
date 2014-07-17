angular.module('senseItWeb', null, null).controller('ProjectViewChallengeAnswersCtrl', function ($scope) {

    $scope.voteManager = {
        votingEnabled: $scope.answerData.votingEnabled,
        getPath: function(target) {
            return 'api/project/' + $scope.projectData.project.id + '/challenge/answers/' + target.id + '/vote';
        }
    };



    $scope.itemView = {
        isOpen: false,
        answer: null,
        isNew: false,
        open: function (answer) {
            this.answer = answer;
            this.isNew = false;
            this.isOpen = true;
        },
        openById: function (answerId) {
            for (var i = 0; i < $scope.answerData.answers.length; i++) {
                if ($scope.answerData.answers[i].id == answerId) {
                    this.open($scope.answerData.answers[i]);
                    break;
                }
            }
        },
        openNew: function () {
            this.answer = {fieldValues: {}, published: false};
            this.isNew = true;
            this.isOpen = true;
        },
        close: function () {
            this.isOpen = false;
        },
        updateAnswers: function (answers) {
            $scope.answerData.answers = answers;
        }
    };

});