angular.module('senseItWeb', null, null).filter('trustedResource', ['$sce', function ($sce) {
  return function (val) {
    return $sce.trustAsResourceUrl(val);
  };
}]);