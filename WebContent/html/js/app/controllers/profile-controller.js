angular.module('senseItWeb', null, null).controller(
		'ProfileCtrl',
		function($scope, $window, OpenIdService) {
			$scope.openIdService = OpenIdService;

			$scope.$watch('openIdService.status', function() {
				$scope.status = $scope.openIdService.status;
				if ($scope.status.profile) {
					$scope.form.setObject($scope.status.profile);
				}
			}, true);

			$scope.form = new siwFormManager(null, [ 'name' ], function() {
				$scope.status.newUser = false;
			});

			$scope.newUser = function() {
				return $scope.status.ready && $scope.status.logged
						&& $scope.status.newUser;
			};

			$scope.logout = function() {
				$scope.openIdService.logout();
			};

			$scope.login = function(provider) {
				window.handleOpenIdResponse = function() {
					$scope.openIdService.update();
				};

				$scope.loginWindow = $window.open('login/redirect?p='
						+ provider);
			};
		});
