
angular
		.module('senseItWeb', null, null)
		.controller(
				'LoginCtrl',
				function($scope, $state, OpenIdService) {
					$scope.openIdService = OpenIdService;

					$scope.$watch('openIdService.status', function() {
						$scope.status = $scope.openIdService.status;
						if ($scope.status.logged && $scope.status.newUser) {
							$state.go('profile');
						}
					}, true);

					$scope.menuItemLabel = function() {

						return $scope.status.ready ? ($scope.status.logged ? ($scope.status.profile.name ? $scope.status.profile.name
								: 'My profile')
								: "Login")
								: "...";
					};

				});
