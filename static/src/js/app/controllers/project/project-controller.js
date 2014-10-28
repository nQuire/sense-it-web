angular.module('senseItWeb', null, null).controller('ProjectCtrl', function ($scope, $state, ProjectService, $location) {

  $scope.templates = {};

  $scope.projectMenu = {
    isActive: function (state) {
      return $state.current.name.indexOf('project.' + state) === 0;
    }
  };

  ProjectService.watchProject($scope, $state.params['projectId']);


  $scope.commentThread = {
    thread: null,
    title: 'Mission discussion',
    path: 'project/' + $state.params['projectId'],
    postingEnabled: function () {
      return $scope.status.logged &&
        ($scope.projectData.access.admin ||
        ($scope.projectData.access.member && $scope.projectData.project.open));
    },
    postingDisabledTemplate: 'partials/project/comments/posting-disabled.html'
  };

  $scope.commentVoteManager = {
    votingEnabled: $scope.commentThread.postingEnabled,
    getPath: function (target) {
      return 'api/project/' + $scope.projectData.project.id + '/comments/' + target.id + '/vote';
    }
  };

  $scope.socialPosting = {
    content: function (provider, name) {
      switch (provider) {
        case 'twitter':
          return {
            tweet: 'See the latest activity on this #nquireit mission: ' + $scope.projectData.project.title +
            "\n" + $location.absUrl()
          };
        case 'facebook':
          return {
            link: $location.absUrl(),
            linkName: 'nQuire-it: ' + $scope.projectData.project.title,
            caption: 'See the latest activity on this nQuire-it mission: ' + $scope.projectData.project.title,
            description: 'nQuire-it link to mission ' + $scope.projectData.project.title
          };
        default:
          return null;
      }
    }
  };

});
