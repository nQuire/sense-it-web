angular.module('senseItWeb', null, null).controller('ProjectViewChallengeCtrl', function ($scope, ProjectChallengeParticipantService) {

    $scope.templates.menu = 'partials/project/view/challenge/challenge-view-menu.html';

    $scope.challengeParticipant = ProjectChallengeParticipantService.challengeParticipant($scope.projectWatcher);

});

