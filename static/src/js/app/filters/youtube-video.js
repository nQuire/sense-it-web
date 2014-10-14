angular.module('senseItWeb', null, null).filter('youtubeVideo', ['$sce', function ($sce) {
  return function (value) {
    // Parse for a video ID.
	var m = value.match(/v=([^&]+)/);
    return $sce.trustAsResourceUrl('//www.youtube.com/embed/' + (m ? m[1] : value));
  };
}]);