angular.module('senseItWeb', null, null).controller('ProjectViewChallengeCtrl', function ($scope, ProjectChallengeParticipantService) {

    $scope.templates.menu = 'partials/project/view/challenge/challenge-view-menu.html';
    $scope.templates.projectData = 'partials/project/view/challenge/challenge-project-data.html';
    $scope.templates.outlineData = 'partials/project/view/challenge/challenge-view-outline.html';

    $scope.challengeParticipant = ProjectChallengeParticipantService.challengeParticipant($scope.projectWatcher);

});

