angular.module('senseItWeb', null, null).directive('siwUserLink', function () {
  return {
    templateUrl: 'partials/profile/user-link.html',
    scope: {
      'user': '=siwUserLink'
    }
  };
});
