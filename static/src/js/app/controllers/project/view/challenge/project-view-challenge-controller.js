angular.module('senseItWeb', null, null).controller('ProjectViewChallengeCtrl', function ($scope, ProjectChallengeParticipantService) {

    $scope.challengeParticipant = ProjectChallengeParticipantService.challengeParticipant($scope.projectWatcher);

});

