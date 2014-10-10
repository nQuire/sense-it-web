angular.module('senseItWeb', null, null).filter('youtubeVideo', ['$sce', function ($sce) {
  return function (val) {
    return $sce.trustAsResourceUrl('//www.youtube.com/embed/' + val);
  };
}]);